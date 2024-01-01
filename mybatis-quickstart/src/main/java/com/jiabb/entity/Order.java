package com.jiabb.entity;

import lombok.Data;

/**
 * @description: 订单类
 * @author: jia_b
 * @date: 2024/1/1 12:47
 * @since: 1.0
 */
@Data
public class Order {

    private Integer id;

    private String ordertime;

    private Double total;

    private User user;
}