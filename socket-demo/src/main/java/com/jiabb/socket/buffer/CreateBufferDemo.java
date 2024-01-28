package com.jiabb.socket.buffer;

import java.nio.ByteBuffer;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/27 16:06
 * @since: 1.0
 */
public class CreateBufferDemo {

    public static void main(String[] args) {
        // 方式1
        ByteBuffer buffer = ByteBuffer.allocate(5);
        for (int i = 0; i < 5; i++) {
            System.out.println(buffer.get());
        }

        // 方式2
        ByteBuffer bufferWrap = ByteBuffer.wrap("ceshi".getBytes());
        for (int i = 0; i < 5; i++) {
            System.out.println(bufferWrap.get());
        }



    }

}