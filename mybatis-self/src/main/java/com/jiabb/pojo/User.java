package com.jiabb.pojo;

/**
 * @description: 实体用户类
 * @author: jiabb-b
 * @date: 2023/5/6 22:41
 * @since: 1.0
 */
public class User {

    //主键标识
    private Integer id;
    //用户名
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
