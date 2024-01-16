package com.example.webfluxlistener.demos.web.entity;

/**
 * @description: 温度
 * @author: jia_b
 * @date: 2024/1/16 20:48
 * @since: 1.0
 */
public class Temperature {

    private final double value;

    public Temperature(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}