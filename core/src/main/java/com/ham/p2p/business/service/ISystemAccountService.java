package com.ham.p2p.business.service;

import com.ham.p2p.business.domain.SystemAccount;

public interface ISystemAccountService {
    int save(SystemAccount record);



    int update(SystemAccount record);


    SystemAccount getCurrent();

    void initSystemAccount();
}
