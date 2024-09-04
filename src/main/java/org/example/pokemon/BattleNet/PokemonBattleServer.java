package org.example.pokemon.BattleNet;

import org.example.pokemon.battle.Battle;
import org.example.pokemon.battle.BattleApplication;
import org.example.pokemon.battle.PokemonData;

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
        System.out.println("Server started...");  // 打印服务器启动信息
    }

    // 启动服务器并开始战斗
    public void start() throws IOException {
        // 接受两个客户端的连接
        clientA = serverSocket.accept();
        clientB = serverSocket.accept();
        System.out.println("Clients connected.");  // 打印客户端连接信息

        // 创建输入输出流
        inA = new BufferedReader(new InputStreamReader(clientA.getInputStream()));
        outA = new PrintWriter(clientA.getOutputStream(), true);

        inB = new BufferedReader(new InputStreamReader(clientB.getInputStream()));
        outB = new PrintWriter(clientB.getOutputStream(), true);

        // 从文件中加载宝可梦数据
        poke1 = loadPokemonData("pokemonA.txt");
        poke2 = loadPokemonData("pokemonB.txt");

        // 初始化战斗逻辑
        battle = new ServerBattle();
        // 启动战斗线程
        new Thread(this::runBattle).start();
    }

    // 从文件中加载宝可梦数据
    private PokemonData loadPokemonData(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String name = reader.readLine();  // 读取宝可梦名字
            String image = reader.readLine();  // 读取宝可梦图片
            String skill1 = reader.readLine();  // 读取宝可梦技能1
            String skill2 = reader.readLine();  // 读取宝可梦技能2
            String skill3 = reader.readLine();  // 读取宝可梦技能3
            String skill4 = reader.readLine();  // 读取宝可梦技能4
            return new PokemonData();  // 创建并返回宝可梦对象
        } catch (IOException e) {
            e.printStackTrace();  // 打印异常信息
            return null;  // 处理错误的情况
        }
    }

    // 运行战斗逻辑
    private void runBattle() {
        try {
            while (true) {
                // 处理客户端A和客户端B的轮流操作
                if (!processTurn(poke1, poke2, outA, inA, outB, "Client A") ||
                        !processTurn(poke2, poke1, outB, inB, outA, "Client B")) {
                    break;  // 退出循环
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // 打印异常信息
        } finally {
            // 关闭客户端和服务器的套接字
            try {
                clientA.close();
                clientB.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();  // 打印异常信息
            }
        }
    }

    // 处理每一轮的攻击操作
    private boolean processTurn(PokemonData attacker, PokemonData defender, PrintWriter attackerOut, BufferedReader attackerIn, PrintWriter defenderOut, String attackerName) throws IOException {
        try {
            attackerOut.println("Your turn");  // 通知攻击方轮到他们操作
            String actioning = attackerIn.readLine();  // 从攻击方读取操作指令
            int action = Integer.parseInt(actioning);
            battle.act(attacker, action, defender);  // 执行攻击动作
            defenderOut.println(attackerName + " took action: " + action);  // 通知防守方攻击方的操作
        } catch (InterruptedException e) {
            // 处理 InterruptedException 异常
            System.err.println("Operation was interrupted: " + e.getMessage());
            // 根据需要，你还可以选择中断当前线程，或采取其他措施
            Thread.currentThread().interrupt();  // 可选: 恢复线程的中断状态
        }


        // 检查宝可梦是否逃跑
        if (battle.checkIsOver(poke1, poke2) == -2) {
            attackerOut.println(attacker.getPokemonName() + "逃跑了");  // 通知攻击方宝可梦逃跑
            defenderOut.println(attacker.getPokemonName() + "逃跑了");  // 通知防守方宝可梦逃跑
            return false;  // 退出游戏
        }

        updateHealth(outA, outB);  // 更新双方宝可梦的血量

        // 检查战斗是否结束
        if (battle.checkIsOver(poke1, poke2) != 0) {
            attackerOut.println("Game over");  // 通知攻击方游戏结束
            defenderOut.println("Game over");  // 通知防守方游戏结束
            return false;  // 退出游戏
        }

        return true;  // 继续游戏
    }

    // 更新双方宝可梦的血量
    private void updateHealth(PrintWriter outA, PrintWriter outB) {
        String healthInfo = "Remaining HP - Pokemon 1: " + poke1.getHp() + ", Pokemon 2: " + poke2.getHp();
        outA.println(healthInfo);  // 更新客户端A的宝可梦血量
        outB.println(healthInfo);  // 更新客户端B的宝可梦血量
    }

    // 主函数：启动服务器
    public static void main(String[] args) {
        try {
            new PokemonBattleServer().start();  // 启动服务器
        } catch (IOException e) {
            e.printStackTrace();  // 打印异常信息
        }
    }
}
