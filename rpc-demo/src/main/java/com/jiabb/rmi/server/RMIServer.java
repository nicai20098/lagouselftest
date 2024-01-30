package com.jiabb.rmi.server;

import com.jiabb.rmi.service.impl.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @description: 服务端
 * @author: jia_b
 * @date: 2024/1/30 21:49
 * @since: 1.0
 */
public class RMIServer {

    public static void main(String[] args) {
        try {
            // 注册Registry 实例 绑定端口
            Registry registry = LocateRegistry.createRegistry(9998);
            // 创建远程对象
            UserServiceImpl userService = new UserServiceImpl();
            // 将远程对象注册到RMI
            registry.rebind("userService", userService);
            System.out.println("rmi服务端启动成功");
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

}