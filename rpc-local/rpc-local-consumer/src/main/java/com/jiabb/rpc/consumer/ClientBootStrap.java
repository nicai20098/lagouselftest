package com.jiabb.rpc.consumer;

import com.jiabb.rpc.api.IUserService;
import com.jiabb.rpc.consumer.handler.RpcClientHandler;
import com.jiabb.rpc.consumer.proxy.RpcClientProxy;
import com.jiabb.rpc.pojo.User;

/**
 * @description: 测试类
 * @author: jia_b
 * @date: 2024/1/30 23:32
 * @since: 1.0
 */
public class ClientBootStrap {

    public static void main(String[] args) {

        IUserService iUserService = (IUserService) RpcClientProxy.createProxy(IUserService.class);
        User byUserId = iUserService.getByUserId(1);
        System.out.println(byUserId);


    }

}