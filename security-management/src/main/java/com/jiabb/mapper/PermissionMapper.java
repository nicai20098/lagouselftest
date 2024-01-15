package com.jiabb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiabb.domain.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根据用户ID查询权限
     *
     * @param id
     * @return
     */
    @Select("SELECT p.*  FROM t_permission p,t_role_permission rp,t_role r,t_user_role ur,t_user u " +
            "WHERE p.id = rp.permission_id AND rp.role_id = r.id AND r.id = ur.role_id AND ur.user_id = u.id AND u.id =#{id}")
    List<Permission> findByUserId(Integer id);
}
