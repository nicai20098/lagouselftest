package com.jiabb.service.impl;

import com.jiabb.mapper.AccountMapper;
import com.jiabb.pojo.Account;
import com.jiabb.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/9 23:25
 * @since: 1.0
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {


    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<Account> queryAccountList() {
        return accountMapper.queryAccountList();
    }
}