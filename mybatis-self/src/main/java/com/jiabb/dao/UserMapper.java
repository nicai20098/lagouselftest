package com.jiabb.dao;

import com.jiabb.entity.User;

import java.util.List;

/**
 * @description: 用户查询类
 * @author: jia_b
 * @date: 2024/1/1 10:58
 * @since: 1.0
 */
public interface UserMapper {

    //查询所有用户
    List<User> selectList() throws Exception;

    //根据条件进行用户查询
    User selectOne(User user) throws Exception;

}
