package org.example.pokemon.pokemonNet;

import java.io.*;
import java.net.*;
import java.util.*;

public class PokemonBattleServer {
    private ServerSocket serverSocket;
    private Map<String, Socket> clients = new HashMap<>();  // 用于存储连接的客户端
    private Map<String, String> clientPositions = new HashMap<>();  // 用于存储每个客户端的坐标

    public PokemonBattleServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动，等待玩家连接...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket, this).start();
        }
    }

    public synchronized void registerClient(String playerName, Socket socket) {
        clients.put(playerName, socket);
    }

    public synchronized void updateClientPosition(String playerName, String position) {
        clientPositions.put(playerName, position);
        broadcastPositions();
    }

    public synchronized void broadcastPositions() {
        for (String player : clients.keySet()) {
            Socket socket = clients.get(player);
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                for (Map.Entry<String, String> entry : clientPositions.entrySet()) {
                    out.writeUTF("位置更新: " + entry.getKey() + " -> " + entry.getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized Socket getClientSocket(String playerName) {
        return clients.get(playerName);
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PokemonBattleServer server;
        private DataInputStream in;
        private DataOutputStream out;
        private String playerName;

        public ClientHandler(Socket socket, PokemonBattleServer server) {
            this.socket = socket;
            this.server = server;
        }

        public void run() {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                // 处理玩家注册
                out.writeUTF("请输入您的玩家名：");
                playerName = in.readUTF();
                server.registerClient(playerName, socket);
                System.out.println(playerName + " 已连接");

                while (true) {
                    String message = in.readUTF();

                    if (message.startsWith("position")) {
                        String position = message.substring(9);  // "position " 后的字符串为坐标信息
                        server.updateClientPosition(playerName, position);
                    } else if (message.startsWith("challenge")) {
                        handleChallenge(message);
                    } else if (message.startsWith("action")) {
                        handleAction(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleChallenge(String message) throws IOException {
            String[] parts = message.split(" ");
            String opponentName = parts[1];
            Socket opponentSocket = server.getClientSocket(opponentName);

            if (opponentSocket != null) {
                DataOutputStream opponentOut = new DataOutputStream(opponentSocket.getOutputStream());
                opponentOut.writeUTF("挑战请求来自 " + playerName + "。输入 'accept' 接受或 'reject' 拒绝：");

                String response = new DataInputStream(opponentSocket.getInputStream()).readUTF();
                if (response.equals("accept")) {
                    out.writeUTF(opponentName + " 接受了你的挑战！");
                    opponentOut.writeUTF("战斗开始！");
                    startBattle(playerName, opponentName);
                } else {
                    out.writeUTF(opponentName +" 拒绝了你的挑战。");
                }
            } else {
                out.writeUTF("找不到玩家 " + opponentName);
            }
        }

        private void startBattle(String playerA, String playerB) throws IOException {
            Socket socketA = server.getClientSocket(playerA);
            Socket socketB = server.getClientSocket(playerB);
            DataOutputStream outA = new DataOutputStream(socketA.getOutputStream());
            DataOutputStream outB = new DataOutputStream(socketB.getOutputStream());
            DataInputStream inA = new DataInputStream(socketA.getInputStream());
            DataInputStream inB = new DataInputStream(socketB.getInputStream());

            outA.writeUTF("战斗开始！");
            outB.writeUTF("战斗开始！");

            while (true) {
                String actionA = inA.readUTF();
                String actionB = inB.readUTF();

                // 处理两个玩家的指令
                String result = processBattleActions(actionA, actionB);

                // 将结果返回给玩家
                outA.writeUTF(result);
                outB.writeUTF(result);

                if (result.contains("结束")) {
                    break;
                }
            }
        }

        private String processBattleActions(String actionA, String actionB) {
            // 在这里处理玩家的行动逻辑，比如计算伤害，判定胜负
            if (actionA.equals("end") || actionB.equals("end")) {
                return "战斗结束";
            }

            return "A: " + actionA + " | B: " + actionB;
        }

        private void handleAction(String message) throws IOException {
            // 处理其他的动作或命令
        }
    }

    public static void main(String[] args) {
        try {
            PokemonBattleServer server = new PokemonBattleServer(12345);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
