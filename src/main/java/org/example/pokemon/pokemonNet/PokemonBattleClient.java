package org.example.pokemon.pokemonNet;

import java.io.*;
import java.net.*;

public class PokemonBattleClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String playerName;

    public PokemonBattleClient(String address, int port) throws IOException {
        socket = new Socket(address, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void start() throws IOException {
        System.out.println(in.readUTF());  // 读取服务器的欢迎消息
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        playerName = reader.readLine();
        out.writeUTF(playerName);

        // 启动发送坐标的线程
        new Thread(new PositionSender()).start();

        while (true) {
            String serverMessage = in.readUTF();
            System.out.println("服务器: " + serverMessage);

            if (serverMessage.startsWith("位置更新")) {
                // 处理其他玩家的坐标
                System.out.println("收到其他玩家的位置: " + serverMessage);
            } else if (serverMessage.contains("输入 'accept' 接受或 'reject' 拒绝")) {
                String response = reader.readLine();
                out.writeUTF(response);
            } else if (serverMessage.contains("战斗开始！")) {
                startBattle();
            } else {
                String userInput = reader.readLine();
                out.writeUTF(userInput);
            }
        }
    }

    private void startBattle() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("输入你的行动: ");
            String action = reader.readLine();
            out.writeUTF(action);

            String battleResult = in.readUTF();
            System.out.println("战斗结果: " + battleResult);

            if (battleResult.contains("结束")) {
                break;
            }
        }
    }

    // 定期发送玩家坐标的线程
    private class PositionSender implements Runnable {
        public void run() {
            try {
                while (true) {
                    // 假设随机生成坐标，实际使用中应替换为真实的玩家位置
                    String position = "x=" + (int)(Math.random() * 100) + ",y=" + (int)(Math.random() * 100);
                    out.writeUTF("position " + position);
                    Thread.sleep(5000);  // 每5秒发送一次位置
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            PokemonBattleClient client = new PokemonBattleClient("localhost", 12345);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
