package com.jiabb.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @description: 消息编解码
 * @author: jia_b
 * @date: 2024/1/28 13:52
 * @since: 1.0
 */
public class MessageCodec extends MessageToMessageCodec {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        System.out.println("encoder doing");
        String str = (String) o;
        list.add(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        System.out.println("doing decoder");
        ByteBuf byteBuf = (ByteBuf) o;
        list.add(byteBuf.toString(CharsetUtil.UTF_8));

    }
}