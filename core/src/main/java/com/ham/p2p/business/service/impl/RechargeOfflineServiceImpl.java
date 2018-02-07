package com.ham.p2p.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Account;
import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.util.UserContext;
import com.ham.p2p.business.domain.RechargeOffline;
import com.ham.p2p.business.mapper.RechargeOfflineMapper;
import com.ham.p2p.business.query.RechargeOfflineQueryObject;
import com.ham.p2p.business.service.IAccountFlowService;
import com.ham.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {
    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;

    @Override
    public int save(RechargeOffline record) {
        return rechargeOfflineMapper.insert(record);
    }

    @Override
    public RechargeOffline get(Long id) {
        return rechargeOfflineMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(RechargeOffline record) {
        return rechargeOfflineMapper.updateByPrimaryKey(record);
    }

    @Override
    public void apply(RechargeOffline rechargeOffline) {
        RechargeOffline offline = new RechargeOffline();
        offline.setApplier(UserContext.getCurrentUser());
        offline.setApplyTime(new Date());
        offline.setAmount(rechargeOffline.getAmount());
        offline.setTradeCode(rechargeOffline.getTradeCode());
        offline.setTradeTime(rechargeOffline.getTradeTime());
        offline.setBankInfo(rechargeOffline.getBankInfo());
        offline.setState(RechargeOffline.STATE_NORMAL);
        offline.setNote(rechargeOffline.getNote());
        this.save(offline);
    }

    @Override
    public PageInfo queryPage(RechargeOfflineQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = rechargeOfflineMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        RechargeOffline ro = this.get(id);
        if (ro != null && ro.getState() == RechargeOffline.STATE_NORMAL) {
            ro.setAuditor(UserContext.getCurrentUser());
            ro.setAuditTime(new Date());
            ro.setRemark(remark);
            if (state == RechargeOffline.STATE_PASS) {
                ro.setState(RechargeOffline.STATE_PASS);
                Account applierAccount = accountService.selectByPrimaryKey(ro.getApplier().getId());
                applierAccount.setUsableAmount(applierAccount.getUsableAmount().add(ro.getAmount()));
                accountService.update(applierAccount);
                //生成充值成功流水
                accountFlowService.createRechargeOfflineFlow(applierAccount,ro.getAmount());
            } else {
                ro.setState(RechargeOffline.STATE_REJECT);
            }
        }
        this.update(ro);
    }
}
