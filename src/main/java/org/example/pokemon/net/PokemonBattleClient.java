package org.example.pokemon.net;

import java.io.*;
import java.net.*;

public class PokemonBattleClient {
    private static final String SERVER_ADDRESS = "localhost";  // 服务器地址
    private static final int SERVER_PORT = 12345;              // 服务器端口号

    public static void main(String[] args) throws IOException {
        // 连接到服务器
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String response;
        while ((response = in.readLine()) != null) {
            System.out.println("服务器: " + response);

            // 处理不同的服务器消息
            if (response.contains("请求开始战斗")) {
                // 客户端A的战斗请求被服务器转发给客户端B
                System.out.println("收到战斗请求，请输入 'accept' 接受战斗或 'decline' 拒绝战斗：");
                String userResponse = getUserResponse();
                out.println(userResponse);
            } else if (response.contains("战斗接受，开始准备战斗")) {
                // 客户端B接受了战斗
                System.out.println("战斗开始，请输入宠物名字：");
                String petName = getPetName();
                out.println(petName);
            } else if (response.contains("拒绝战斗")) {
                // 客户端B拒绝了战斗
                System.out.println("战斗被拒绝了。");
                break; // 退出循环，结束客户端程序
            } else if (response.contains("轮到你行动")) {
                // 获取并发送行动指令
                String action = getAction();
                out.println(action);
            } else if (response.contains("战斗结束")) {
                System.out.println("战斗结束。");
                break; // 退出循环，结束客户端程序
            }
        }

        // 关闭与服务器的连接
        socket.close();
    }

    // 获取宠物名字
    private static String getPetName() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine(); // 从控制台读取用户输入的宠物名字
    }

    // 获取行动指令
    private static String getAction() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine(); // 从控制台读取用户输入的行动指令
    }

    // 获取用户的回应
    private static String getUserResponse() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine(); // 从控制台读取用户输入的回应
    }
}
