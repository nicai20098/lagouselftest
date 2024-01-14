package com.jiabb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiabb.domain.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    // 根据用户ID查询对应的权限
    List<Permission> findByUserId(Integer id);
}
