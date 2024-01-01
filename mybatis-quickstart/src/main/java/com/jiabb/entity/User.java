package com.jiabb.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 用户
 * @author: jia_b
 * @date: 2024/1/1 11:33
 * @since: 1.0
 */
@Data
public class User implements Serializable {

    private Integer id;

    private String username;

    //该用户所涉及的订单
    private List<Order> orderList;
    //该用户所涉及的角色
    private List<Role> roles;


}