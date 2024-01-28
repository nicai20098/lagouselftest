package com.jiabb.socket.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/27 15:23
 * @since: 1.0
 */
public class ServerDemo {

    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务器已启动");
        while (true) {
            // 监听客户端
            Socket socket = serverSocket.accept();
            System.out.println("有客户端连接");
            // 开启新线程处理
            executorService.execute(() -> {
                handle(socket);
            });

        }

    }

    private static void handle(Socket socket) {
        try {
            System.out.println("线程id: " + Thread.currentThread().getId() + "线程名称: " + Thread.currentThread().getName());
            InputStream is = socket.getInputStream();
            byte[] b = new byte[1024];
            int read = is.read(b);
            System.out.println("客户端:" + new String(b, 0, read));
            OutputStream os = socket.getOutputStream();
            os.write("没钱".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}