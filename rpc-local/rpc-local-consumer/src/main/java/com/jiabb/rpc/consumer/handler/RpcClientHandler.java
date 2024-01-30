package com.jiabb.rpc.consumer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

/**
 * @description: 客户端处理类
 * @author: jia_b
 * @date: 2024/1/30 23:15
 * @since: 1.0
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable<Object> {

    ChannelHandlerContext ctx;

    String requestMsg;
    String responseMsg;

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        responseMsg = s;
        // 唤醒等待的线程
        notify();
    }

    @Override
    public synchronized Object call() throws Exception {
        // 消息发送
        ctx.writeAndFlush(requestMsg);
        // 线程等待
        wait();
        return responseMsg;
    }
}