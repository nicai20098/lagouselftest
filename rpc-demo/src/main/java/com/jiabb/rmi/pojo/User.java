package com.jiabb.rmi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/30 21:44
 * @since: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private int id;
    private String name;

}