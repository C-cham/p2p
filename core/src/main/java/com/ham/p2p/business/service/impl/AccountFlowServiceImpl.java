package com.ham.p2p.business.service.impl;

import com.ham.p2p.base.domain.Account;
import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.business.domain.AccountFlow;
import com.ham.p2p.business.mapper.AccountFlowMapper;
import com.ham.p2p.business.service.IAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class AccountFlowServiceImpl implements IAccountFlowService {
    @Autowired
    private AccountFlowMapper accountFlowMapper;

    @Override
    public int save(AccountFlow accountFlow) {
        return accountFlowMapper.insert(accountFlow);
    }

    @Override
    public void createRechargeOfflineFlow(Account account, BigDecimal amount) {
        createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE, "线下充值成功:" + amount + "元");
    }

    @Override
    public void createBidFlow(Account account, BigDecimal amount) {
        createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED, "投标冻结:" + amount + "元");

    }

    @Override
    public void createBidFailFlow(Account account, BigDecimal amount) {
        createFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED, "投标失败,返回冻结:" + amount + "元");
    }

    @Override
    public void createBorrowSuccessFlow(Account createAccount, BigDecimal bidRequestAmount) {
        createFlow(createAccount, bidRequestAmount, BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL, "成功借款:" + bidRequestAmount + "元");

    }

    @Override
    public void createPayAccountManagementChargeFailFlow(Account createAccount, BigDecimal accountManagementCharge) {
        createFlow(createAccount, accountManagementCharge, BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_CHARGE, "支付平台手续费:" + accountManagementCharge + "元");

    }

    @Override
    public void creteBidSuccessFlow(Account bidUserAccount, BigDecimal availableAmount) {
        createFlow(bidUserAccount, availableAmount, BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL, "投标成功:" + availableAmount + "元");

    }

    @Override
    public void createReturnMoneyFlow(Account account, BigDecimal totalAmount) {
        createFlow(account, totalAmount, BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY, "还款成功:" + totalAmount + "元");

    }


    @Override
    public void createGainPrincipalAndInterestFlow(Account bidUserAccount, BigDecimal totalAmount) {
        createFlow(bidUserAccount, totalAmount, BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY, "回款成功:" + totalAmount + "元");

    }
    @Override
    public void createPaymentInterestManagerChargeFlow(Account bidUserAccount, BigDecimal interestManagerCharge) {
        createFlow(bidUserAccount, interestManagerCharge, BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE, "支付信息管理费成功:" + interestManagerCharge + "元");

    }

    private void createFlow(Account account, BigDecimal amount, int actionType, String remark) {
        AccountFlow flow = new AccountFlow();
        flow.setAccountId(account.getId());
        flow.setAmount(amount);
        flow.setTradeTime(new Date());
        flow.setUsableAmount(account.getUsableAmount());
        flow.setFreezedAmount(account.getFreezedAmount());
        flow.setActionType(actionType);
        flow.setRemark(remark);
        this.save(flow);
    }
}
