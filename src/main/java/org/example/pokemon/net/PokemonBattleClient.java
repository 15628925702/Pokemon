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
                // 发送开始战斗的请求
                out.println("start");
            } else if (response.contains("请求宠物名字")) {
                // 获取并发送宠物名字
                String petName = getPetName();
                out.println(petName);
            } else if (response.contains("轮到你行动")) {
                // 获取并发送行动指令
                String action = getAction();
                out.println(action);
            } else if (response.contains("战斗结束")) {
                break; // 退出循环，结束客户端程序
            }
        }

        // 关闭与服务器的连接
        socket.close();
    }

    // 占位函数，获取宠物名字
    private static String getPetName() throws IOException {
        // 实际实现中这里需要获取用户的宠物名字
        return "示例宠物"; // 示例返回值
    }

    // 占位函数，获取行动指令
    private static String getAction() throws IOException {
        // 实际实现中这里需要获取用户的行动指令
        return "示例行动"; // 示例返回值
    }
}
