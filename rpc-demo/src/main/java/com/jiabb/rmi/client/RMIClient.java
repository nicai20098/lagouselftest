package com.jiabb.rmi.client;

import com.jiabb.rmi.pojo.User;
import com.jiabb.rmi.service.IUserService;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @description: 客户端
 * @author: jia_b
 * @date: 2024/1/30 21:55
 * @since: 1.0
 */
public class RMIClient {


    public static void main(String[] args) {
        try {
            // 获取Registry实例
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9998);
            // 通过registry实例查找远程对象
            IUserService userService = (IUserService) registry.lookup("userService");
            User byUserId = userService.getByUserId(1);
            System.out.println(byUserId);

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }


    }

}