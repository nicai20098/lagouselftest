package com.jiabb.rpc.consumer.client;

import com.jiabb.rpc.consumer.handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description: 客户端
 * 1. 连接Netty
 * 2. 提供给调用者主动关闭资源的方法
 * 3. 提供消息发送的方法
 * @author: jia_b
 * @date: 2024/1/30 23:07
 * @since: 1.0
 */
public class RpcClient {

    private String ip;

    private int port;

    NioEventLoopGroup group;
    Channel channel;

    private final RpcClientHandler rpcClientHandler = new RpcClientHandler();

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initClient();
    }

    public void initClient() {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(rpcClientHandler);
                    }
                });

        try {
            channel = bootstrap.connect(ip, port).sync().channel();
        } catch (InterruptedException e) {
            if (Objects.nonNull(channel)) {
               channel.close();
            }
            if (Objects.nonNull(group)) {
                group.shutdownGracefully();
            }
        }
    }

    public void close() {
        if (Objects.nonNull(channel)) {
            channel.close();
        }
        if (Objects.nonNull(group)) {
            group.shutdownGracefully();
        }
    }

    public Object send(String msg) throws ExecutionException, InterruptedException {
        rpcClientHandler.setRequestMsg(msg);
        Future<Object> submit = executorService.submit(rpcClientHandler);
        return submit.get();
    }

}