package org.example.pokemon.BattleNet;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class PokemonBattleClient {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int PORT = 12345; // 服务器端口
    private Socket socket; // 套接字，用于与服务器连接
    private BufferedReader in; // 输入流，用于接收来自服务器的数据
    private PrintWriter out; // 输出流，用于向服务器发送数据
    public volatile boolean isMyTurn = false; // 标记是否轮到玩家操作（线程安全）
    private Thread communicationThread; // 用于运行通信的线程
    private ClientCallback callback; // 用于回调 UI 更新的接口

    // 添加用于存储血量的属性
    private int healthMe; // 宝可梦A的血量
    private int healthEn; // 宝可梦B的血量

    // 构造函数：初始化客户端套接字
    public PokemonBattleClient(ClientCallback callback) throws IOException {
        this.callback = callback;
        System.out.println("正在连接到服务器：" + SERVER_ADDRESS + "，端口：" + PORT);
        try {
            socket = new Socket(SERVER_ADDRESS, PORT); // 连接到服务器
            System.out.println("成功连接到服务器。");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 初始化输入流
            out = new PrintWriter(socket.getOutputStream(), true); // 初始化输出流
            startCommunication(); // 启动通信线程
        } catch (IOException e) {
            System.err.println("连接到服务器失败：" + e.getMessage());
            throw e;
        }
    }

    // 启动通信线程
    private void startCommunication() {
        communicationThread = new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println("收到来自服务器的消息： " + serverMessage); // 打印接收到的服务器消息
                    handleServerMessage(serverMessage);
                }
            } catch (IOException e) {
                System.err.println("通信异常：" + e.getMessage()); // 打印异常信息
            } finally {
                try {
                    System.out.println("关闭客户端连接。");
                    socket.close(); // 关闭套接字
                } catch (IOException e) {
                    System.err.println("关闭连接时出错：" + e.getMessage()); // 打印异常信息
                }
            }
        });
        communicationThread.setDaemon(true); // 设置为守护线程
        communicationThread.start(); // 启动线程
    }

    // 处理来自服务器的消息
    private void handleServerMessage(String serverMessage) {
        try {
            JSONObject json = new JSONObject(serverMessage);
            String type = json.getString("type");

            if (type.equals("YourTurn")) {
                isMyTurn = true; // 标记玩家可以操作
                System.out.println("轮到你操作了。"); // 打印轮到玩家的提示
                // 调用回调通知 UI 更新
                if (callback != null) {
                    callback.onYourTurn();
                }
            } else if (type.equals("GameOver")) {
                System.out.println("游戏结束！"); // 打印游戏结束的信息
                // 调用回调通知游戏结束
                if (callback != null) {
                    callback.onGameOver();
                }
            } else if (type.equals("HealthUpdate")) {
                String healthInfo = json.getString("healthInfo");
                System.out.println("服务器消息 - " + healthInfo); // 打印血量更新的信息

                // 解析血量信息并更新属性
                String[] healthParts = healthInfo.split(", ");
                this.healthMe = Integer.parseInt(healthParts[0].split(": ")[1]); // 提取A的血量
                this.healthEn = Integer.parseInt(healthParts[1].split(": ")[1]); // 提取B的血量

                // 打印更新后的血量
                System.out.println("更新后的血量 - 宝可梦A: " + healthMe+ ", 宝可梦B: " + healthEn);

                // 可以在此处更新 UI 以显示血量
                if (callback != null) {
                    callback.onHealthUpdate(healthInfo);
                }
            } else {
                System.out.println("服务器消息： " + serverMessage); // 打印服务器的其他消息
            }
        } catch (Exception e) {
            System.err.println("处理服务器消息时出错：" + e.getMessage()); // 打印异常信息
        }
    }

    // 向服务器发送动作指令
    public void sendAction(String action) {
        if (isMyTurn) { // 只有在轮到玩家时才发送动作
            JSONObject json = new JSONObject();
            json.put("action", action); // 将动作封装为 JSON 对象
            System.out.println("发送动作指令：" + json.toString()); // 打印要发送的动作指令
            out.println(json.toString()); // 发送 JSON 字符串
            isMyTurn = false; // 发送后重置标记
        } else {
            System.out.println("当前不是你的回合，无法发送动作。"); // 打印当前不是玩家回合的提示
        }
    }

    // 关闭客户端连接
    public void close() throws IOException {
        System.out.println("正在关闭客户端连接...");
        try {
            socket.close(); // 关闭套接字
            System.out.println("客户端连接已关闭。");
        } catch (IOException e) {
            System.err.println("关闭连接时出错：" + e.getMessage()); // 打印异常信息
            throw e;
        }
    }

    // 回调接口
    public interface ClientCallback {
        void onYourTurn();
        void onGameOver();
        void onHealthUpdate(String healthInfo);
    }
}