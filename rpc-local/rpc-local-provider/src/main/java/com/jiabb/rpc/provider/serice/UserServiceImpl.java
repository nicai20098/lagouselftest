package com.jiabb.rpc.provider.serice;

import com.jiabb.rpc.api.IUserService;
import com.jiabb.rpc.pojo.User;
import com.jiabb.rpc.provider.anno.RpcService;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/30 21:46
 * @since: 1.0
 */
@RpcService
@Service
public class UserServiceImpl implements IUserService {

    private final Map<Integer, User> userMap = new HashMap<Integer, User>();

    public UserServiceImpl() throws RemoteException {
        super();
        userMap.put(1, new User(1, "张三"));
        userMap.put(2, new User(2, "李四"));
    }


    @Override
    public User getByUserId(int id) {
        return userMap.get(id);
    }
}