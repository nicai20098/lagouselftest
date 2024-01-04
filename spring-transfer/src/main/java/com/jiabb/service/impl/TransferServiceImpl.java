package com.jiabb.service.impl;

import com.jiabb.annotation.Service;
import com.jiabb.annotation.Transactional;
import com.jiabb.context.em.ProxyTypeEnum;
import com.jiabb.dao.AccountDao;
import com.jiabb.pojo.Account;
import com.jiabb.annotation.Autowired;
import com.jiabb.service.TransferService;

/**
 * @author 应癫
 */
@Service("transferServiceImpl")
public class TransferServiceImpl implements TransferService {

    @Autowired("jdbcAccountDaoImpl")
    private AccountDao accountDao;

    @Override
    @Transactional(type = ProxyTypeEnum.JDK)
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney()-money);
            to.setMoney(to.getMoney()+money);

            accountDao.updateAccountByCardNo(to);
            int c = 1/0;
            accountDao.updateAccountByCardNo(from);

    }
}
