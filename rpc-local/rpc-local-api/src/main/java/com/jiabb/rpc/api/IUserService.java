package com.jiabb.rpc.api;

import com.jiabb.rpc.pojo.User;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/30 21:45
 * @since: 1.0
 */
public interface IUserService {

    User getByUserId(int id);

}