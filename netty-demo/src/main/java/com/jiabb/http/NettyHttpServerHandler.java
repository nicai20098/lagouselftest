package com.jiabb.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/28 14:21
 * @since: 1.0
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        // 判断是否是http请求
        if (httpObject instanceof HttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) httpObject;
            System.out.println("请求路径:" + request.uri());
            if (Objects.equals("/favicon.ico", request.uri())) {
                //图标不响应
                return;
            }
            // 给浏览器响应
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello, 我是Netty服务器", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            // 设置响应头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            channelHandlerContext.writeAndFlush(response);
        }
    }
}