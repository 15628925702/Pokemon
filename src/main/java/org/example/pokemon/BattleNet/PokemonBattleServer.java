package org.example.pokemon.BattleNet;

import org.example.pokemon.battle.PokemonData;
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
    public String PokeAName; // 宝可梦A名字
    public String PokeBName; // 宝可梦B名字

    // 构造函数：初始化服务器套接字
    public PokemonBattleServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("服务器已启动，端口号: " + PORT);  // 打印服务器启动信息
    }

    // 启动服务器并开始战斗
    public void start() {
        try {
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

            // 处理客户端发送的宝可梦名字
            handlePokeNames();

            // 发送角色信息
            sendRoleInfo();

            // 从文件中加载宝可梦数据
            loadPokemonData();

            updateHealth();

            // 初始化战斗逻辑
            battle = new ServerBattle();

            // 向客户端A和客户端B发送“已经完成匹配”的信息
            JSONObject matchMessage = new JSONObject();
            matchMessage.put("type", "MatchComplete");
            matchMessage.put("message", "已经完成匹配");
            outA.println(matchMessage.toString());
            outB.println(matchMessage.toString());
            System.out.println("已向两个客户端发送“已经完成匹配”的信息。");


            // 启动战斗线程
            new Thread(this::runBattle).start();
        } catch (IOException e) {
            System.err.println("启动服务器时发生错误: " + e.getMessage());  // 打印异常信息
        }
    }

    // 处理客户端发送的宝可梦名字
    private void handlePokeNames() {
        try {
            // 从客户端A接收宝可梦名字
            JSONObject pokeNameMessageA = new JSONObject(inA.readLine());
            PokeAName = pokeNameMessageA.getString("pokeName");
            System.out.println("接收到客户端A的宝可梦名字: " + PokeAName);

            // 从客户端B接收宝可梦名字
            JSONObject pokeNameMessageB = new JSONObject(inB.readLine());
            PokeBName = pokeNameMessageB.getString("pokeName");
            System.out.println("接收到客户端B的宝可梦名字: " + PokeBName);

            // 发送两个宝可梦名字到客户端A
            JSONObject pokeNameInfoA = new JSONObject();
            pokeNameInfoA.put("type", "PokeNames");
            pokeNameInfoA.put("pokeAName", PokeAName);
            pokeNameInfoA.put("pokeBName", PokeBName);
            outA.println(pokeNameInfoA.toString());

            // 发送两个宝可梦名字到客户端B
            JSONObject pokeNameInfoB = new JSONObject();
            pokeNameInfoB.put("type", "PokeNames");
            pokeNameInfoB.put("pokeAName", PokeAName);
            pokeNameInfoB.put("pokeBName", PokeBName);
            outB.println(pokeNameInfoB.toString());
        } catch (IOException e) {
            System.err.println("处理宝可梦名字时发生错误: " + e.getMessage());
        }
    }

    // 发送角色信息
    private void sendRoleInfo() {
        JSONObject roleMessageA = new JSONObject();
        roleMessageA.put("type", "Role");
        roleMessageA.put("role", "A");
        outA.println(roleMessageA.toString());

        JSONObject roleMessageB = new JSONObject();
        roleMessageB.put("type", "Role");
        roleMessageB.put("role", "B");
        outB.println(roleMessageB.toString());
    }

    // 从文件中加载宝可梦数据
    private void loadPokemonData() {
        try {
            poke1=new PokemonData();
            poke2=new PokemonData();

            PokemonData pokemon1 = new PokemonData();
            pokemon1.getPokeDataFromDb("皮卡丘");
            pokemon1.setPokeSkill("冲撞",0);
            pokemon1.setPokeSkill("十万伏特",1);
            pokemon1.setPokeSkill("电击",2);

            PokemonData pokemon2 = new PokemonData();
            pokemon2.getPokeDataFromDb("小火龙");
            pokemon2.setPokeSkill("冲撞",0);
            pokemon2.setPokeSkill("喷火",1);

            PokemonData pokemon3 = new PokemonData();
            pokemon3.getPokeDataFromDb("杰尼龟");
            pokemon3.setPokeSkill("冲撞",0);
            pokemon3.setPokeSkill("水枪",1);

            PokemonData pokemon4 = new PokemonData();
            pokemon4.getPokeDataFromDb("妙蛙种子");
            pokemon4.setPokeSkill("冲撞",0);
            pokemon4.setPokeSkill("种子炸弹",1);


            if(PokeAName.equals("皮卡丘")){
                poke1.clonePokeData(pokemon1);
                System.out.println("宝可梦A是皮卡丘——" );
            }
            if(PokeAName.equals("小火龙")){
                poke1.clonePokeData(pokemon2);
                System.out.println("宝可梦A是小火龙——" );
            }
            if(PokeAName.equals("杰尼龟")){
                poke1.clonePokeData(pokemon3);
                System.out.println("宝可梦A是杰尼龟——" );
            }
            if(PokeAName.equals("妙蛙种子")){
                poke1.clonePokeData(pokemon4);
                System.out.println("宝可梦A是妙蛙种子——" );
            }

            if(PokeBName.equals("皮卡丘")){
                poke2.clonePokeData(pokemon1);
                System.out.println("宝可梦B是皮卡丘——" );
            }
            if(PokeBName.equals("小火龙")){
                poke2.clonePokeData(pokemon2);
                System.out.println("宝可梦B是小火龙——" );
            }
            if(PokeBName.equals("杰尼龟")){
                poke2.clonePokeData(pokemon3);
                System.out.println("宝可梦B是杰尼龟——" );
            }
            if(PokeBName.equals("妙蛙种子")){
                poke2.clonePokeData(pokemon4);
                System.out.println("宝可梦B是妙蛙种子——" );
            }

            System.out.println("宝可梦数据加载完成。");
        } catch (Exception e) {
            System.err.println("加载宝可梦数据时发生错误: " + e.getMessage());
        }
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
                if (clientA != null) clientA.close();
                if (clientB != null) clientB.close();
                if (serverSocket != null) serverSocket.close();
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
            int result = battle.act(attacker, action, defender);
            System.out.println(attackerName + " 执行了动作: " + action);

            // 通知防守方攻击方的操作
            message = new JSONObject();
            message.put("result", result);
            message.put("type", "Action");
            message.put("attacker", attackerName);
            message.put("action", action);
            defenderOut.println(message.toString());

            // 通知攻击方自己执行的动作结果
            message = new JSONObject();
            message.put("result", result);
            message.put("type", "Action");
            message.put("attacker", attackerName);
            message.put("action", action);
            attackerOut.println(message.toString());

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
        System.out.println("====================================================================");
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
