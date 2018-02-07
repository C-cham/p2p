package com.ham.p2p.business.service;

import com.ham.p2p.business.domain.SystemAccount;
import com.ham.p2p.business.domain.SystemAccountFlow;

import java.math.BigDecimal;

public interface ISystemAccountFlowService {
    int save(SystemAccountFlow record);

    /**
     * 系统账户收取借款人手续费
     * @param systemAccount
     * @param accountManagementCharge
     */
    void createGainAccountManagementChargeFlow(SystemAccount systemAccount, BigDecimal accountManagementCharge);

    void createGainInterestManagerChargeFlow(SystemAccount systemAccount, BigDecimal interestManagerCharge);
}
