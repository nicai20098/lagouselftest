package com.jiabb.socket.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/28 9:50
 * @since: 1.0
 */
public class NIOServer {


    public static void main(String[] args) throws Exception {

        // 1. 打开一个服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2. 绑定对应的端口号
        serverSocketChannel.bind(new InetSocketAddress(9999));
        // 3. 通道默认是阻塞的，需要设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        System.out.println("服务端启动成功...");
        while (true) {
            // 4. 检查是否有客户端连接 有客户端连接会返回对应的通道
            SocketChannel accept = serverSocketChannel.accept();
            if (Objects.isNull(accept)) {
                System.out.println("没有客户端连接... 先做别的事情");
                Thread.sleep(1000);
                continue;
            }
            System.out.println("客户端连接... 等待客户端响应");
            // 5. 获取客户端传递过来的数据,并把数据放在byteBuffer这个缓冲区中
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            // 返回值
            // 正数 有效字节数
            // 0 本此没有读到
            // -1 表示读到末尾
            int read = accept.read(allocate);
            System.out.println("客户端消息:" + new String(allocate.array(), 0, read, StandardCharsets.UTF_8));
            // 6. 给客户端回写数据
            accept.write(ByteBuffer.wrap("没钱".getBytes(StandardCharsets.UTF_8)));
            // 7. 释放资源
            accept.close();
        }




    }

}