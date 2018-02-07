package com.ham.p2p.business.service.impl;

import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.business.domain.SystemAccount;
import com.ham.p2p.business.domain.SystemAccountFlow;
import com.ham.p2p.business.mapper.SystemAccountFlowMapper;
import com.ham.p2p.business.service.ISystemAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class SystemAccountFlowServiceImpl implements ISystemAccountFlowService {
    @Autowired
    private SystemAccountFlowMapper systemAccountFlowMapper;

    @Override
    public int save(SystemAccountFlow record) {
        return systemAccountFlowMapper.insert(record);
    }

    @Override
    public void createGainAccountManagementChargeFlow(SystemAccount systemAccount, BigDecimal accountManagementCharge) {
        createFlow(systemAccount, accountManagementCharge, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE, "收取用户借款手续费:" + accountManagementCharge + "元");
    }

    @Override
    public void createGainInterestManagerChargeFlow(SystemAccount systemAccount, BigDecimal interestManagerCharge) {
        createFlow(systemAccount, interestManagerCharge, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE, "收取用户利息管理费:" + interestManagerCharge + "元");

    }

    private void createFlow(SystemAccount systemAccount, BigDecimal amount, int actionType, String remark) {
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setActionTime(new Date());
        flow.setActionType(actionType);
        flow.setAmount(amount);
        flow.setFreezedAmount(systemAccount.getFreezedAmount());
        flow.setUsableAmount(systemAccount.getUsableAmount());
        flow.setRemark(remark);
        this.save(flow);
    }
}
