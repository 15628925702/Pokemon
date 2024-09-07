package org.example.pokemon.MapNet;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MapClient {
    // 服务器地址和端口
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("客户端已启动.");

            // 启动一个线程来接收服务器的广播消息
            new Thread(() -> {
                String message;
                try {
                    // 持续接收服务器发来的消息并打印
                    while ((message = in.readLine()) != null) {
                        System.out.println("从服务器收到: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // 主线程用来从用户输入中读取坐标并发送到服务器
            while (true) {
                System.out.print("请输入坐标 (x,y): ");
                String coordinates = scanner.nextLine();
                // 发送坐标到服务器
                out.println(coordinates);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
