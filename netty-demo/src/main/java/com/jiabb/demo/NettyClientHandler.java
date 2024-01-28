package com.jiabb.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @description: 客户端处理类
 * @author: jia_b
 * @date: 2024/1/28 12:56
 * @since: 1.0
 */
public class NettyClientHandler implements ChannelInboundHandler {
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    /**
     * 通道就绪事件
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
//        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("你好呀 我是Netty客户端", CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush("你好呀 我是Netty客户端");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    /**
     * 通道读就绪事件
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) o;
//        System.out.println("服务端发送过来的消息:" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端发送过来的消息:" + o);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }
}