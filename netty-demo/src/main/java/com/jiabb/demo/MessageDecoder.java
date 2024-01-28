package com.jiabb.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/28 13:35
 * @since: 1.0
 */
public class MessageDecoder extends MessageToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object msg, List list) throws Exception {
        System.out.println("doing decoder");
        ByteBuf byteBuf = (ByteBuf) msg;
        list.add(byteBuf.toString(CharsetUtil.UTF_8));


    }
}