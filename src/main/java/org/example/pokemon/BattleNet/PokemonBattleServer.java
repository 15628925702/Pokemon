package org.example.pokemon.BattleNet;

import java.io.*;
import java.net.*;

public class PokemonBattleServer {
    private static final int PORT = 12345;  // 服务器端口号
    private static Socket socketA;          // 客户端A的Socket
    private static Socket socketB;          // 客户端B的Socket
    private static PrintWriter outA;        // 向客户端A发送数据的PrintWriter
    private static PrintWriter outB;        // 向客户端B发送数据的PrintWriter
    private static BufferedReader inA;      // 从客户端A接收数据的BufferedReader
    private static BufferedReader inB;      // 从客户端B接收数据的BufferedReader

    private static Pet petA;
    private static Pet petB;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("服务器启动，监听端口 " + PORT);

        // 接受来自客户端A的连接
        System.out.println("等待客户端A连接...");
        socketA = serverSocket.accept();
        System.out.println("客户端A已连接。");

        // 接受来自客户端B的连接
        System.out.println("等待客户端B连接...");
        socketB = serverSocket.accept();
        System.out.println("客户端B已连接。");

        // 创建输入输出流
        inA = new BufferedReader(new InputStreamReader(socketA.getInputStream()));
        outA = new PrintWriter(socketA.getOutputStream(), true);

        inB = new BufferedReader(new InputStreamReader(socketB.getInputStream()));
        outB = new PrintWriter(socketB.getOutputStream(), true);

        // 启动战斗过程
        startBattle();

        // 关闭ServerSocket
        serverSocket.close();
    }

    private static void startBattle() throws IOException {
        // 向A请求开始战斗
        outA.println("请求开始战斗...");
        String responseA = inA.readLine();

        // 将A的请求转发给B
        outB.println(responseA);
        String responseB = inB.readLine();

        // 根据B的反馈通知A和B
        if ("accept".equalsIgnoreCase(responseB)) {
            outA.println("战斗接受，开始准备战斗。");
            outB.println("战斗接受，开始准备战斗。");

            // 设置战斗
            setupBattle();
        } else {
            outA.println("拒绝战斗。");
            outB.println("拒绝战斗。");
        }
    }

    private static void setupBattle() throws IOException {
        // 请求A和B提供宠物名字
        outA.println("请求宠物名字。");
        String petNameA = getPetName("A");
        outB.println("请求宠物名字。");
        String petNameB = getPetName("B");

        // 根据宠物名字创建宠物（这里是简化版）
        createPets(petNameA, petNameB);

        // 开始战斗回合
        while (true) {
            // 根据宠物速度确定轮到谁
            String currentPlayer = determineTurn();
            if ("A".equals(currentPlayer)) {
                outA.println("轮到你行动。");
                String actionA = getAction("A");
                // 处理A的操作
                processAction("A", actionA);
                // 将当前状态反馈给A和B
                updatePlayers();
                outB.println("轮到你行动。");
                String actionB = getAction("B");
                // 处理B的操作
                processAction("B", actionB);
                // 将当前状态反馈给A和B
                updatePlayers();
            } else {
                outB.println("轮到你行动。");
                String actionB = getAction("B");
                // 处理B的操作
                processAction("B", actionB);
                // 将当前状态反馈给A和B
                updatePlayers();
                outA.println("轮到你行动。");
                String actionA = getAction("A");
                // 处理A的操作
                processAction("A", actionA);
                // 将当前状态反馈给A和B
                updatePlayers();
            }

            // 检查是否结束战斗
            if (checkEndBattle()) {
                outA.println("战斗结束。");
                outB.println("战斗结束。");
                break;
            }
        }
    }

    // 根据宠物名字创建宠物
    private static void createPets(String petNameA, String petNameB) {
        // 从本地文件读取宠物数据
        petA = new Pet(petNameA);
        petB = new Pet(petNameB);
    }

    // 确定当前轮到哪个玩家
    private static String determineTurn() {
        // 比较宠物速度，实际实现中可以更复杂
        return petA.getSpeed() > petB.getSpeed() ? "A" : "B";
    }

    // 处理玩家的行动
    private static void processAction(String player, String action) {
        // 实际实现中这里需要处理玩家的行动并更新状态
        Pet currentPet = "A".equals(player) ? petA : petB;
        // 处理行动...
        currentPet.performAction(action);
    }

    // 更新A和B的状态
    private static void updatePlayers() {
        // 实际实现中这里需要将当前战斗状态反馈给A和B
        outA.println(petA.getStatus());
        outB.println(petB.getStatus());
    }

    // 检查战斗是否结束
    private static boolean checkEndBattle() {
        // 实际实现中这里需要检查是否有宠物死亡等结束条件
        return petA.isDead() || petB.isDead();
    }

    // 获取指定玩家的宠物名字
    private static String getPetName(String player) throws IOException {
        // 实际实现中这里需要从指定玩家处获取宠物名字
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine(); // 这里可以用合适的方式获取宠物名字
    }

    // 获取指定玩家的行动
    private static String getAction(String player) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine(); // 这里可以用合适的方式获取行动
    }
}

// 宠物类
class Pet {
    private String name;
    private int speed;
    private int health;

    public Pet(String name) {
        // 从本地文件或数据库加载宠物数据
        this.name = name;
        this.speed = loadSpeed(name);
        this.health = loadHealth(name);
    }

    public void performAction(String action) {
        // 执行行动的逻辑
    }

    public String getStatus() {
        // 返回宠物的状态
        return name + " - 生命值: " + health;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getSpeed() {
        return speed;
    }

    private int loadSpeed(String name) {
        // 从文件或数据库加载宠物速度
        return 10; // 示例值
    }

    private int loadHealth(String name) {
        // 从文件或数据库加载宠物生命值
        return 100; // 示例值
    }
}
