package org.example.pokemon.BattleNet;

import java.io.*;
import java.net.*;

public class PokemonBattleClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // 构造函数：初始化客户端套接字
    public PokemonBattleClient() throws IOException {
        socket = new Socket(SERVER_ADDRESS, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    // 启动客户端
    public void start() {
        try {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.equals("Your turn")) {
                    System.out.print("Enter your action: ");
                    String action = userIn.readLine();
                    out.println(action);
                } else if (serverMessage.equals("Game over")) {
                    System.out.println("Game over! Exiting...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // 打印异常信息
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();  // 打印异常信息
            }
        }
    }

    // 主函数：启动客户端
    public static void main(String[] args) {
        try {
            new PokemonBattleClient().start();  // 启动客户端
        } catch (IOException e) {
            e.printStackTrace();  // 打印异常信息
        }
    }
}
