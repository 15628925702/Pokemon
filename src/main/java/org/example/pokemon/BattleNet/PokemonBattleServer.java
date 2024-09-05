package org.example.pokemon.BattleNet;

import org.example.pokemon.battle.Battle;
import org.example.pokemon.battle.PokemonData;
import org.example.pokemon.battle.PokemonSkill;
import org.json.JSONObject;

import java.io.*;
import java.net.*;

public class PokemonBattleServer {
    private static final int PORT = 12345;  // 服务器端口
    private ServerSocket serverSocket;  // 服务器端套接字
    private Socket clientA;  // 客户端A的套接字
    private Socket clientB;  // 客户端B的套接字
    private PrintWriter outA;  // 向客户端A发送数据的输出流
    private PrintWriter outB;  // 向客户端B发送数据的输出流
    private BufferedReader inA;  // 从客户端A接收数据的输入流
    private BufferedReader inB;  // 从客户端B接收数据的输入流
    private ServerBattle battle;  // 战斗逻辑对象
    private PokemonData poke1;  // 宝可梦1数据
    private PokemonData poke2;  // 宝可梦2数据

    // 构造函数：初始化服务器套接字
    public PokemonBattleServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("服务器已启动，端口号: " + PORT);  // 打印服务器启动信息
    }

    // 启动服务器并开始战斗
    public void start() throws IOException {
        System.out.println("等待客户端连接...");

        // 接受两个客户端的连接
        clientA = serverSocket.accept();
        clientB = serverSocket.accept();
        System.out.println("两个客户端已连接。");

        // 创建输入输出流
        inA = new BufferedReader(new InputStreamReader(clientA.getInputStream()));
        outA = new PrintWriter(clientA.getOutputStream(), true);

        inB = new BufferedReader(new InputStreamReader(clientB.getInputStream()));
        outB = new PrintWriter(clientB.getOutputStream(), true);

        // 从文件中加载宝可梦数
        poke1=new PokemonData();
        poke2=new PokemonData();
        poke1.getPokeDataFromDb("小火龙");
        poke2.getPokeDataFromDb("小火龙");
        System.out.println("宝可梦数据加载完成。");

        // 初始化战斗逻辑
        battle = new ServerBattle();
        // 启动战斗线程
        new Thread(this::runBattle).start();
    }

    private PokemonData loadPokemonData(String fileName) {
        System.out.println("尝试加载文件: " + fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            System.out.println("文件打开成功。开始读取数据...");

            // 读取宝可梦名字
            String name = reader.readLine();
            System.out.println("宝可梦名字: " + name);
            int pokemonType = Integer.parseInt(reader.readLine());
            int hp = Integer.parseInt(reader.readLine());
            int speed = Integer.parseInt(reader.readLine());
            int physical_attack = Integer.parseInt(reader.readLine());
            int physical_defense = Integer.parseInt(reader.readLine());
            int special_attack = Integer.parseInt(reader.readLine());
            int special_defense = Integer.parseInt(reader.readLine());
            PokemonSkill[] skillsOfPokes = new PokemonSkill[4];
            for (int i = 0; i < 4; i++) {
                String skill = reader.readLine();
                System.out.println("技能 " + (i + 1) + ": " + skill);
                skillsOfPokes[i] = convertToPokemonSkill(skill);
            }

            System.out.println("成功加载宝可梦数据: " + name);  // 打印加载成功的信息
            return new PokemonData(name, pokemonType, hp, speed, physical_attack, physical_defense, special_attack, special_defense, skillsOfPokes);
        } catch (IOException e) {
            System.err.println("加载宝可梦数据时发生错误: " + e.getMessage());  // 打印异常信息
            return null;  // 处理错误的情况
        } catch (NumberFormatException e) {
            System.err.println("数据格式错误: " + e.getMessage());  // 捕捉数据格式异常
            return null;  // 处理格式错误
        }
    }


    // 假设有这样的方法
    private PokemonSkill convertToPokemonSkill(String skillName) {
        // 实现将技能名转换为 PokemonSkill 对象的逻辑
        // 这里是一个示例，具体实现取决于 PokemonSkill 类
        return new PokemonSkill();
    }

    // 运行战斗逻辑
    private void runBattle() {
        try {
            while (true) {
                // 处理客户端A和客户端B的轮流操作
                if (!processTurn(poke1, poke2, outA, inA, outB, "客户端A") ||
                        !processTurn(poke2, poke1, outB, inB, outA, "客户端B")) {
                    break;  // 退出循环
                }
            }
        } catch (IOException e) {
            System.err.println("战斗过程中发生错误: " + e.getMessage());  // 打印异常信息
        } finally {
            // 关闭客户端和服务器的套接字
            try {
                clientA.close();
                clientB.close();
                serverSocket.close();
                System.out.println("服务器和客户端连接已关闭。");
            } catch (IOException e) {
                System.err.println("关闭连接时发生错误: " + e.getMessage());  // 打印异常信息
            }
        }
    }

    // 处理每一轮的攻击操作
    private boolean processTurn(PokemonData attacker, PokemonData defender, PrintWriter attackerOut, BufferedReader attackerIn, PrintWriter defenderOut, String attackerName) throws IOException {
        try {
            // 通知攻击方轮到他们操作
            JSONObject message = new JSONObject();
            message.put("type", "YourTurn");
            attackerOut.println(message.toString());
            System.out.println(attackerName + " 轮到攻击。");

            // 从攻击方读取操作指令
            JSONObject receivedMessage = new JSONObject(attackerIn.readLine());
            int action = receivedMessage.getInt("action");

            // 执行攻击动作
            battle.act(attacker, action, defender);
            System.out.println(attackerName + " 执行了动作: " + action);

            // 通知防守方攻击方的操作
            message = new JSONObject();
            message.put("type", "Action");
            message.put("attacker", attackerName);
            message.put("action", action);
            defenderOut.println(message.toString());

        } catch (InterruptedException e) {
            // 处理 InterruptedException 异常
            System.err.println("操作被中断: " + e.getMessage());
            Thread.currentThread().interrupt();  // 可选: 恢复线程的中断状态
        }

        // 检查宝可梦是否逃跑
        if (battle.checkIsOver(poke1, poke2) == -2) {
            // 通知攻击方和防守方宝可梦逃跑
            JSONObject escapeMessage = new JSONObject();
            escapeMessage.put("type", "Escape");
            escapeMessage.put("pokemon", attacker.getPokemonName());
            attackerOut.println(escapeMessage.toString());
            defenderOut.println(escapeMessage.toString());
            System.out.println(attacker.getPokemonName() + " 逃跑了。");
            return false;  // 退出游戏
        }

        updateHealth();  // 更新双方宝可梦的血量

        // 检查战斗是否结束
        if (battle.checkIsOver(poke1, poke2) != 0) {
            // 通知双方游戏结束
            JSONObject endMessage = new JSONObject();
            endMessage.put("type", "GameOver");
            attackerOut.println(endMessage.toString());
            defenderOut.println(endMessage.toString());
            System.out.println("游戏结束。");
            return false;  // 退出游戏
        }

        return true;  // 继续游戏
    }

    // 更新双方宝可梦的血量
    private void updateHealth() {
        String healthInfo = "剩余血量 - 宝可梦1: " + poke1.getHp() + ", 宝可梦2: " + poke2.getHp();
        JSONObject healthMessage = new JSONObject();
        healthMessage.put("type", "HealthUpdate");
        healthMessage.put("healthInfo", healthInfo);
        outA.println(healthMessage.toString());  // 更新客户端A的宝可梦血量
        outB.println(healthMessage.toString());  // 更新客户端B的宝可梦血量
        System.out.println("血量更新: " + healthInfo);
    }

    // 主函数：启动服务器
    public static void main(String[] args) {
        try {
            new PokemonBattleServer().start();  // 启动服务器
        } catch (IOException e) {
            System.err.println("启动服务器时发生错误: " + e.getMessage());  // 打印异常信息
        }
    }
}
