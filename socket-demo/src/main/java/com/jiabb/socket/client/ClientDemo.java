package com.jiabb.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/27 15:18
 * @since: 1.0
 */
public class ClientDemo {

    public static void main(String[] args) throws IOException {
        while (true) {
            // 创建 socket 对象
            Socket socket = new Socket("127.0.0.1", 9999);
            // 从连接中取出输出流并发消息
            OutputStream os = socket.getOutputStream();
            System.out.println("请输入:");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            os.write(s.getBytes());
            // 从连接中取出输入流并接收回话
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            System.out.println("老板说:" + new String(bytes, 0, read).trim());
            // 关闭
            socket.close();

        }

    }


}