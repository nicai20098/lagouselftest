package com.jiabb.dao;

import com.jiabb.pojo.Account;

/**
 * @author 应癫
 */
public interface AccountDao {

    Account queryAccountByCardNo(String cardNo) throws Exception;

    int updateAccountByCardNo(Account account) throws Exception;
}
