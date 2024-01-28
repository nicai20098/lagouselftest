package com.jiabb.unpacking;

import com.jiabb.demo.MessageDecoder;
import com.jiabb.demo.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/28 12:53
 * @since: 1.0
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        //1. 创建线程组
        EventLoopGroup group = new NioEventLoopGroup();
        //2. 创建客户端启动助手
        Bootstrap bootstrap = new Bootstrap();
        //3. 设置线程组
        bootstrap.group(group)
                .channel(NioSocketChannel.class) //4. 设置客户端通道实现为NIO
                .handler(new ChannelInitializer<SocketChannel>() {  //5. 创建一个通道初始化对象
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("messageEncoder",new MessageEncoder());
                        socketChannel.pipeline().addLast("messageDecoder",new MessageDecoder());
                        //6. 向pipeline中添加自定义业务处理handler
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        //7. 启动客户端,等待连接服务器,同时将异步改为同步
        ChannelFuture connect = bootstrap.connect("127.0.0.1", 9999);
        //8. 关闭通道和关闭连接池
        connect.channel().closeFuture().sync();
        group.shutdownGracefully();


    }

}