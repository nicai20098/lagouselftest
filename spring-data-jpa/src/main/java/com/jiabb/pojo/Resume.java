package com.jiabb.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @description: 简历实体类
 * @author: jia_b
 * @date: 2024/1/10 22:26
 * @since: 1.0
 */
@Entity
@Data
@Table(name = "tb_resume")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
}