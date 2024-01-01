package com.jiabb.dao;

import com.jiabb.entity.Order;

import java.util.List;

/**
 * @description: 订单dao
 * @author: jia_b
 * @date: 2024/1/1 12:53
 * @since: 1.0
 */
public interface OrderMapper {

    //查询订单所属用户信息
    List<Order> findOrderAndUser();


}