package com.ham.p2p.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Account;
import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.base.util.UserContext;
import com.ham.p2p.business.domain.BidRequest;
import com.ham.p2p.business.domain.PaymentSchedule;
import com.ham.p2p.business.domain.PaymentScheduleDetail;
import com.ham.p2p.business.domain.SystemAccount;
import com.ham.p2p.business.mapper.PaymentScheduleMapper;
import com.ham.p2p.business.query.PaymentScheduleQueryObject;
import com.ham.p2p.business.service.*;
import com.ham.p2p.business.util.CalculatetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.ServiceMode;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentScheduleServiceImpl implements IPaymentScheduleService {
    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private IAccountService accountService;

    @Autowired
    private IAccountFlowService accountFlowService;

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IBidService bidService;

    @Autowired
    private ISystemAccountService systemAccountService;

    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;

    @Override
    public int save(PaymentSchedule record) {
        return paymentScheduleMapper.insert(record);
    }

    @Override
    public PaymentSchedule get(Long id) {
        return paymentScheduleMapper.selectByPrimaryKey(id);

    }

    @Override
    public int update(PaymentSchedule record) {
        return paymentScheduleMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo queryPage(PaymentScheduleQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = paymentScheduleMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void returnMoney(Long id) {
        //1.获取还款对象
        PaymentSchedule ps = this.get(id);
        //2.判断条件是否满足:
        //2.1还款对象状态是待还
        if (ps != null && ps.getState() == BidConst.PAYMENT_STATE_NORMAL) {
            //2.2判断当前用户是还款人
            if (UserContext.getCurrentUser().getId().equals(ps.getBorrowUser().getId())) {
                //当前登录用户的可用金额>=该期还款金额
                Account account = accountService.getCurrent();
                if (account.getUsableAmount().compareTo(ps.getTotalAmount()) >= 0) {
                    //还款对象设置状态为:已还
                    ps.setState(BidConst.PAYMENT_STATE_DONE);
                    //设置对象还款日期
                    ps.setPayDate(new Date());
                    this.update(ps);
                    //对象还款明细设置还款日志
                    paymentScheduleDetailService.updatePayDate(ps.getId(), ps.getPayDate());
                    //可以用金额减少,待还金额减少,授信额度增加(还款的本金)
                    account.setUsableAmount(account.getUsableAmount().subtract(ps.getTotalAmount()));
                    account.setUnReturnAmount(account.getUnReturnAmount().subtract(ps.getTotalAmount()));
                    account.setRemainBorrowLimit(account.getRemainBorrowLimit().add(ps.getPrincipal()));
                    accountService.update(account);
                    accountFlowService.createReturnMoneyFlow(account, ps.getTotalAmount());
                    //4.对于还款人
                    Long bidUserId;
                    Account bidUserAccount;
                    Map<Long, Account> accountMap = new HashMap<Long, Account>();
                    BigDecimal interestManagerCharge;
                    SystemAccount systemAccount = systemAccountService.getCurrent();
                    for (PaymentScheduleDetail psd : ps.getDetails()) {
                        bidUserId = psd.getInvestorId();
                        bidUserAccount = accountMap.get(bidUserId);
                        if (bidUserAccount == null) {
                            bidUserAccount = accountService.selectByPrimaryKey(bidUserId);
                            accountMap.put(bidUserId, bidUserAccount);
                        }
                        //可用金额增加,代收本金和代收利息减少
                        bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(psd.getTotalAmount()));
                        bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
                        bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().subtract(psd.getInterest()));
                        //生成回款成功流水
                        accountFlowService.createGainPrincipalAndInterestFlow(bidUserAccount, psd.getTotalAmount());
                        //投资人支付利息管理费(利息的10%),投资人可以用金额减少
                        interestManagerCharge = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
                        //生成支付利息管理费的流水
                        bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().subtract(interestManagerCharge));
                        accountFlowService.createPaymentInterestManagerChargeFlow(bidUserAccount, interestManagerCharge);
                        //平台账户收取利息管理费
                        systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(interestManagerCharge));
                        //生成平台账户收取利息管理费的流水
                        systemAccountFlowService.createGainInterestManagerChargeFlow(systemAccount, interestManagerCharge);
                    }
                    //统一对信息用户修改
                    for (Account accountTemp : accountMap.values()) {
                        accountService.update(accountTemp);
                    }
                    systemAccountService.update(systemAccount);
                    List<PaymentSchedule> paymentSchedules = paymentScheduleMapper.queryByBidRequestId(ps.getBidRequestId());
                    //通过借款的id获取该借款人的所有还款,判断所有还款对象状态是否变成已还
                    for (PaymentSchedule paymentSchedule : paymentSchedules) {
                        if (paymentSchedule.getState() != BidConst.PAYMENT_STATE_DONE) ;
                    }
                }
                //借款对象和投标对象状态修改成-->已还清
                BidRequest bidRequest = bidRequestService.selectByPrimaryKey(ps.getBidRequestId());
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                bidRequestService.updateByPrimaryKey(bidRequest);
                bidService.updateState(ps.getBidRequestId(), BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);

            }
        }

    }
}
