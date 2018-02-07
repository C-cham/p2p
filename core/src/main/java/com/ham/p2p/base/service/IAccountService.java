package com.ham.p2p.base.service;

import com.ham.p2p.base.domain.Account;

public interface IAccountService {
    int save(Account record);

    Account selectByPrimaryKey(Long id);
    Account getCurrent();

    int update(Account record);
}
