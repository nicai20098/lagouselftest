package com.jiabb.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义处理类
 * TextWebSocketFrame : websocket数据是帧的形式处理
 */
@Component
@ChannelHandler.Sharable // 设置通道共享
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    public static List<Channel> channelList = new ArrayList<>();

    /**
     * 通道就绪事件
     *
     * @param ctx ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        // 当有新的客户端连接的时候，将通道放入集合
        channelList.add(channel);
        System.out.println("有新的连接");
    }

    /**
     * 读就绪事件
     *
     * @param ctx ctx
     * @param textWebSocketFrame twf
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) {
        String msg = textWebSocketFrame.text();
        System.out.println("msg:" + msg);
        // 当前发送消息的通道，当前发送客户端的连接
        Channel channel = ctx.channel();
        for (Channel channel1 : channelList) {
            // 排除自身通道
            if (channel != channel1) {
                channel1.writeAndFlush(new TextWebSocketFrame(msg));
            }
        }
    }

    /**
     * 通道未就绪--channel 下线
     *
     * @param ctx ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        // 当已有客户端断开连接的时候，就一处对应的通道
        channelList.remove(channel);
    }

    /**
     * 异常处理事件
     *
     * @param ctx ctx
     * @param cause ca
     */

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        // 移除集合
        channelList.remove(channel);
    }
}
