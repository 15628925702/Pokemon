package org.example.pokemon.BattleNet;

import javafx.application.Platform;
import org.example.pokemon.battle.BattleController;

import java.io.*;
import java.net.Socket;

public class PokemonBattleClient {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 12345; // 服务器端口号

    private Socket socket; // 套接字
    private BufferedReader in; // 输入流
    private PrintWriter out; // 输出流
    private BattleController controller; // JavaFX 控制器

    public PokemonBattleClient(BattleController controller) {
        this.controller = controller; // 构造函数传入控制器
    }

    // 连接到服务器
    public void connectToServer() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // 创建连接
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 初始化输入流
        out = new PrintWriter(socket.getOutputStream(), true); // 初始化输出流

        // 启动一个线程来接收消息
        new Thread(this::receiveMessages).start();
    }

    // 接收服务器消息
    private void receiveMessages() {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                // 根据服务器返回的消息更新UI或处理战斗逻辑
                handleServerResponse(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close(); // 关闭连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 处理服务器返回的消息
    private void handleServerResponse(String response) {
        Platform.runLater(() -> { // 确保UI更新在JavaFX主线程中执行
            if (response.contains("请求开始战斗")) {
                controller.showBattleRequestDialog(); // 显示战斗请求对话框
            } else if (response.contains("战斗接受，开始准备战斗")) {
                controller.promptForPetName(); // 提示输入宠物名字
            } else if (response.contains("拒绝战斗")) {
                controller.showBattleDeclinedMessage(); // 显示战斗被拒绝消息
            } else if (response.contains("轮到你行动")) {
                controller.promptForAction(); // 提示输入行动指令
            } else if (response.contains("战斗结束")) {
                controller.showBattleEndMessage(); // 显示战斗结束消息
            } else if (response.contains("对方宠物")) {
                controller.updateOpponentPet(response); // 更新对方宠物信息
            } else if (response.contains("更新血量")) {
                controller.updateHealthStatus(response); // 更新血量信息
            }
        });
    }

    // 发送消息到服务器
    public void sendToServer(String message) {
        if (out != null) {
            out.println(message); // 发送消息
        }
    }
}
