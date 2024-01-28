package com.jiabb.http;

import com.jiabb.demo.MessageDecoder;
import com.jiabb.demo.MessageEncoder;
import com.jiabb.demo.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import javax.sound.sampled.Port;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/28 14:18
 * @since: 1.0
 */
public class NettyHttpServer {

    private int port;

    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = null;
        NioEventLoopGroup workerGroup = null;
        try {
            //1. 创建bossGroup线程组: 处理网络事件--连接事件
            bossGroup = new NioEventLoopGroup(1);
            //2. 创建workerGroup线程组: 处理网络事件--读写事件
            workerGroup = new NioEventLoopGroup();
            //3. 创建服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //4. 设置bossGroup线程组和workerGroup线程组
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) //5. 设置服务端通道实现为NIO
                    .option(ChannelOption.SO_BACKLOG, 128)  //6. 参数设置
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .childHandler(new ChannelInitializer<SocketChannel>() { //7. 创建一个通道初始化对象
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //8.向pipeline中添加自定义业务处理handler
                            channel.pipeline().addLast(new HttpServerCodec());
                            channel.pipeline().addLast(new NettyHttpServerHandler());

                        }

                    });
            //9. 启动服务端并绑定端口,同时将异步改为同步
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            System.out.println("Http服务端启动成功");
            //10. 关闭通道和关闭连接池
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public NettyHttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyHttpServer(8080).run();

    }


}