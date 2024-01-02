package com.jiabb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiabb.entity.User;

import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/2 22:42
 * @since: 1.0
 */
public interface UserMapper extends BaseMapper<User> {


    /**
     * 查询全部
     */
    List<User> findAll();

}