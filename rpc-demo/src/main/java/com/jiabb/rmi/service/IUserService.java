package com.jiabb.rmi.service;

import com.jiabb.rmi.pojo.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/30 21:45
 * @since: 1.0
 */
public interface IUserService extends Remote {


    User getByUserId(int id) throws RemoteException;

}