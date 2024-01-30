package com.jiabb.rpc.provider;

import com.jiabb.rpc.provider.server.RpcServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/30 22:39
 * @since: 1.0
 */
@SpringBootApplication
public class ServerBootstrapApplication implements CommandLineRunner {

    @Resource
    private RpcServer rpcServer;

    public static void main(String[] args) {
        SpringApplication.run(ServerBootstrapApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            rpcServer.startServer("127.0.0.1", 8899);
        }).start();
    }
}