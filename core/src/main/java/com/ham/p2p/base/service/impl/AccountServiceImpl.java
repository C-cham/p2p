package com.ham.p2p.base.service.impl;

import com.ham.p2p.base.domain.Account;
import com.ham.p2p.base.mapper.AccountMapper;
import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service@Transactional
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public int save(Account record) {
        return accountMapper.insert(record);
    }

    @Override
    public Account selectByPrimaryKey(Long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    @Override
    public Account getCurrent() {
        return this.selectByPrimaryKey(UserContext.getCurrentUser().getId());
    }

    @Override
    public int update(Account record) {
        int count = accountMapper.updateByPrimaryKey(record);
        if (count <= 0) {
            throw new RuntimeException("乐观锁回滚");
        }

        return count;
    }
}
