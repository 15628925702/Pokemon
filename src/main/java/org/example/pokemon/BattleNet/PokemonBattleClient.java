package org.example.pokemon.BattleNet;

import java.io.*;
import java.net.*;

// 用于与服务器进行通信的客户端类
public class PokemonBattleClient {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int PORT = 12345; // 服务器端口
    private Socket socket; // 套接字，用于与服务器连接
    private BufferedReader in; // 输入流，用于接收来自服务器的数据
    private PrintWriter out; // 输出流，用于向服务器发送数据
    public volatile boolean isMyTurn = false; // 标记是否轮到玩家操作（线程安全）
    private Thread communicationThread; // 用于运行通信的线程
    private ClientCallback callback; // 用于回调 UI 更新的接口

    // 构造函数：初始化客户端套接字
    public PokemonBattleClient(ClientCallback callback) throws IOException {
        this.callback = callback;
        socket = new Socket(SERVER_ADDRESS, PORT); // 连接到服务器
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 初始化输入流
        out = new PrintWriter(socket.getOutputStream(), true); // 初始化输出流
        startCommunication(); // 启动通信线程
    }

    // 启动通信线程
    private void startCommunication() {
        communicationThread = new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    handleServerMessage(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace(); // 打印异常信息
            } finally {
                try {
                    socket.close(); // 关闭套接字
                } catch (IOException e) {
                    e.printStackTrace(); // 打印异常信息
                }
            }
        });
        communicationThread.setDaemon(true); // 设置为守护线程
        communicationThread.start(); // 启动线程
    }

    // 处理来自服务器的消息
    private void handleServerMessage(String serverMessage) {
        if (serverMessage.equals("Your turn")) {
            isMyTurn = true; // 标记玩家可以操作
            // 调用回调通知 UI 更新
            if (callback != null) {
                callback.onYourTurn();
            }
        } else if (serverMessage.equals("Game over")) {
            System.out.println("Game over! Exiting...");
            // 调用回调通知游戏结束
            if (callback != null) {
                callback.onGameOver();
            }
        } else {
            System.out.println(serverMessage); // 打印服务器的其他消息
        }
    }

    // 向服务器发送动作指令
    public void sendAction(String action) {
        if (isMyTurn) { // 只有在轮到玩家时才发送动作
            out.println(action); // 发送指令
            isMyTurn = false; // 发送后重置标记
        }
    }

    // 关闭客户端连接
    public void close() throws IOException {
        socket.close(); // 关闭套接字
    }

    // 回调接口
    public interface ClientCallback {
        void onYourTurn();
        void onGameOver();
    }
}
