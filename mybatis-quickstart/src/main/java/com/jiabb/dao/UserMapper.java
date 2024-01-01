package com.jiabb.dao;

import com.jiabb.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/1 11:47
 * @since: 1.0
 */
//@CacheNamespace
public interface UserMapper {

    List<User> findAll();

    User findByCondition(User user);

    List<User> findByIds(List<Integer> ids);

    List<User> findUserAndOrder();

    List<User> findAllUserAndRole();

    @Insert("insert into user values (#{id}, #{username})")
    Integer addUser(User user);

    @Update("update user set username = #{username} where id = #{id}")
    Integer updateUser(User user);

    @Select("select * from user")
    List<User> selectUserList();

    @Delete("delete from user where id = #{id}")
    Integer deleteUser(Integer id);

}