package com.jiabb.socket.buffer;

import java.nio.ByteBuffer;

/**
 * @description: 向缓冲区中添加数据
 * @author: jia_b
 * @date: 2024/1/27 16:15
 * @since: 1.0
 */
public class PutBufferDemo {

    public static void main(String[] args) {
        // 创建一个缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(10);
        System.out.println(allocate.position());
        System.out.println(allocate.limit());
        System.out.println(allocate.capacity());
        System.out.println(allocate.remaining());


    }

}