package com.jiabb.rmi.service.impl;

import com.jiabb.rmi.pojo.User;
import com.jiabb.rmi.service.IUserService;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/30 21:46
 * @since: 1.0
 */
public class UserServiceImpl extends UnicastRemoteObject implements IUserService {

    private final Map<Integer, User> userMap = new HashMap<Integer, User>();

    public UserServiceImpl() throws RemoteException {
        super();
        userMap.put(1, new User(1, "张三"));
        userMap.put(2, new User(2, "李四"));
    }


    @Override
    public User getByUserId(int id) throws RemoteException {
        return userMap.get(id);
    }
}