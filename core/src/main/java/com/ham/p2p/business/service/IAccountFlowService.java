package com.ham.p2p.business.service;

import com.ham.p2p.base.domain.Account;
import com.ham.p2p.business.domain.AccountFlow;

import java.math.BigDecimal;

public interface IAccountFlowService {
    int save(AccountFlow accountFlow);

    void createRechargeOfflineFlow(Account applierAccount, BigDecimal amount);

    void createBidFlow(Account account, BigDecimal amount);

    void createBidFailFlow(Account account, BigDecimal amount);

    void createBorrowSuccessFlow(Account createAccount, BigDecimal bidRequestAmount);

    void createPayAccountManagementChargeFailFlow(Account createAccount, BigDecimal accountManagementCharge);

    void creteBidSuccessFlow(Account bidUserAccount, BigDecimal availableAmount);

    void createReturnMoneyFlow(Account account, BigDecimal totalAmount);

    void createGainPrincipalAndInterestFlow(Account bidUserAccount, BigDecimal totalAmount);

    void createPaymentInterestManagerChargeFlow(Account bidUserAccount, BigDecimal interestManagerCharge);
}
