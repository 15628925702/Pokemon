package org.example.pokemon.MapNet;

import java.io.*;
import java.net.*;
import java.util.*;

public class MapServer {
    // 服务器端口
    private static final int PORT = 12345;
    // 存储所有客户端的输出流，以便进行广播
    private static Set<PrintWriter> clientWriters = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("服务器已启动.");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // 不断接收新的客户端连接
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // 为每个客户端创建一个处理线程
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // 获取输入流和输出流
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 将客户端的输出流加入到集合中
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                String coordinates;
                // 持续读取客户端发送的坐标
                while ((coordinates = in.readLine()) != null) {
                    System.out.println("收到坐标: " + coordinates);
                    // 广播坐标给所有客户端
                    broadcast(coordinates);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 关闭连接
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 从集合中移除客户端的输出流
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            }
        }

        private void broadcast(String message) {
            // 广播消息给所有客户端
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
}
