package org.example.pokemon.BattleNet;

import java.io.*;
import java.net.*;

import org.json.JSONException;
import org.json.JSONObject;

public class PokemonBattleClient {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int PORT = 12345; // 服务器端口
    private Socket socket; // 套接字，用于与服务器连接
    private BufferedReader in; // 输入流，用于接收来自服务器的数据
    private PrintWriter out; // 输出流，用于向服务器发送数据
    public volatile boolean isMyTurn = false; // 标记是否轮到玩家操作（线程安全）
    public volatile boolean ifMiss = false; // 标记是否轮到玩家操作（线程安全）
    public volatile boolean ifCom = false; // 标记是否轮到玩家操作（线程安全）
    public volatile boolean ifIamA = false; // 标记是否轮到玩家操作（线程安全）
    public volatile String PokeAName; // 标记是否轮到玩家操作（线程安全）
    public volatile String PokeBName; // 标记是否轮到玩家操作（线程安全）
    public volatile String PokeMeName; // 当前玩家的宝可梦名字（线程安全）
    private Thread communicationThread; // 用于运行通信的线程
    private ClientCallback callback; // 用于回调 UI 更新的接口

    // 添加用于存储血量的属性
    public int healthMe; // 宝可梦A的血量
    public int healthEn; // 宝可梦B的血量

    // 构造函数：初始化客户端套接字
    public PokemonBattleClient(ClientCallback callback, String pokeMeName) throws IOException {
        this.callback = callback;
        this.PokeMeName = pokeMeName; // 初始化宝可梦名字
        System.out.println("正在连接到服务器：" + SERVER_ADDRESS + "，端口：" + PORT);
        try {
            socket = new Socket(SERVER_ADDRESS, PORT); // 连接到服务器
            System.out.println("成功连接到服务器。");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 初始化输入流
            out = new PrintWriter(socket.getOutputStream(), true); // 初始化输出流

            startCommunication(); // 启动通信线程
            sendPokeName(); // 发送宝可梦名字
        } catch (IOException e) {
            System.err.println("连接到服务器失败：" + e.getMessage());
            throw e;
        }
    }

    // 发送宝可梦名字到服务器
    private void sendPokeName() {
        JSONObject json = new JSONObject();
        json.put("type", "PokeName");
        json.put("pokeName", PokeMeName);
        out.println(json.toString());
        System.out.println("发送宝可梦名字：" + PokeMeName);
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

    private void handleServerMessage(String serverMessage) {
        try {
            System.out.println("接收到了新消息————————————————————————————————————");
            JSONObject json = new JSONObject(serverMessage);
            String type = json.getString("type");

            switch (type) {

                case "PokeNames":
                    if (this.ifIamA) {
                        this.PokeAName = json.getString("pokeAName");
                        this.PokeBName = json.getString("pokeBName");
                    } else {
                        this.PokeAName = json.getString("pokeAName");
                        this.PokeBName = json.getString("pokeBName");
                    }
                    System.out.println("接收到宝可梦A名字: " + PokeAName);
                    System.out.println("接收到宝可梦B名字: " + PokeBName);
                    break;

                case "MatchComplete":
                    ifCom = true;
                    System.out.println("已完成匹配");
                    System.out.println("宝可梦A名字："+PokeAName+", 宝可梦B名字："+PokeBName);

                    break;

                case "Role":
                    String role = json.getString("role");
                    if ("A".equals(role)) {
                        this.ifIamA = true;
                        System.out.println("你是客户端A");
                    } else if ("B".equals(role)) {
                        this.ifIamA = false;
                        System.out.println("你是客户端B");
                    }
                    break;



                case "YourTurn":
                    this.isMyTurn = true;
                    System.out.println("轮到你操作了。 " + "isMyturn=" + isMyTurn);
                    if (callback != null) {
                        callback.onYourTurn();
                        System.out.println("UI显示轮到你了" + isMyTurn);
                    }
                    break;

                case "Action":
                    int result = json.getInt("result");
                    System.out.println("接受到的result值: " + result); // 调试输出
                    if (result == -3) {
                        this.ifMiss = true;
                        System.out.println("操作结果为-3,闪避成功，ifMiss已设置为true");
                    }
                    // 处理其他可能的result值
                    break;

                case "HealthUpdate":
                    String healthInfo = json.getString("healthInfo");
                    System.out.println("服务器消息 - " + healthInfo);

                    // 解析血量信息并更新属性
                    String[] healthParts = healthInfo.split(", ");
                    if (this.ifIamA) {
                        this.healthEn = Integer.parseInt(healthParts[1].split(": ")[1]);
                        this.healthMe = Integer.parseInt(healthParts[0].split(": ")[1]);
                    } else {
                        this.healthMe = Integer.parseInt(healthParts[1].split(": ")[1]);
                        this.healthEn = Integer.parseInt(healthParts[0].split(": ")[1]);
                    }

                    // 打印更新后的血量
                    System.out.println("更新后的血量 - 我方宝可梦: " + healthMe + ", 敌方宝可梦: " + healthEn);

                    if (callback != null) {
                        callback.onHealthUpdate(healthInfo);
                    }
                    break;

                case "GameOver":
                    System.out.println("游戏结束！");
                    if (callback != null) {
                        callback.onGameOver();
                    }
                    break;

                default:
                    System.out.println("服务器消息： " + serverMessage);
                    break;
            }
        } catch (JSONException e) {
            System.err.println("处理服务器消息时出错：" + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("解析血量信息时出错：" + e.getMessage());
        } catch (Exception e) {
            System.err.println("处理服务器消息时出错：" + e.getMessage());
        }
    }

    // 向服务器发送动作指令
    public void sendAction(String action) {
        System.out.println("正在发送"); // 打印要发送的动作指令
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
