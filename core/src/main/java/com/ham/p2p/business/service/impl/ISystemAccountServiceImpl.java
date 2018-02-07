package com.ham.p2p.business.service.impl;

import com.ham.p2p.business.domain.SystemAccount;
import com.ham.p2p.business.mapper.SystemAccountMapper;
import com.ham.p2p.business.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ISystemAccountServiceImpl implements ISystemAccountService {

    @Autowired
    private SystemAccountMapper systemAccountMapper;

    @Override
    public int save(SystemAccount record) {
        return systemAccountMapper.insert(record);
    }

    @Override
    public int update(SystemAccount record) {
        int count = systemAccountMapper.updateByPrimaryKey(record);
        if (count <= 0) {
            throw new RuntimeException("乐观锁异常");
        }
        return count;
    }

    @Override
    public SystemAccount getCurrent() {
        return systemAccountMapper.selectByCurrent();
    }

    @Override
    public void initSystemAccount() {
        SystemAccount systemAccount = this.getCurrent();
        if (systemAccount == null) {
            systemAccount = new SystemAccount();
            this.save(systemAccount);
        }
    }
}
