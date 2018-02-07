package com.ham.p2p.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Account;
import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.base.util.BitStatesUtils;
import com.ham.p2p.base.util.DateUtil;
import com.ham.p2p.base.util.UserContext;
import com.ham.p2p.business.domain.*;
import com.ham.p2p.business.mapper.BidRequestMapper;
import com.ham.p2p.business.query.BidRequestQueryObject;
import com.ham.p2p.business.service.*;
import com.ham.p2p.business.util.CalculatetUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class BidRequestServiceImpl implements IBidRequestService {

    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private IBidService bidService;

    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;

    @Autowired
    private IAccountFlowService accountFlowService;

    @Autowired
    private ISystemAccountService systemAccountService;

    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;

    @Override
    public int insert(BidRequest record) {
        return bidRequestMapper.insert(record);
    }

    @Override
    public BidRequest selectByPrimaryKey(Long id) {
        return bidRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(BidRequest record) {
        return bidRequestMapper.updateByPrimaryKey(record);
    }

    @Override
    public boolean canApplyBorrow(Userinfo userinfo) {
        if (userinfo.getIsBasicInfo() &&//用户是否填写基本资料
                userinfo.getIsRealAuth() &&//实名认证
                userinfo.getIsVedioAuth() &&//视频认证
                userinfo.getScore() >= BidConst.CREDIT_BORROW_SCORE//风控材料分数是否大于30分
                ) {
            return true;
        }

        return false;
    }

    @Override
    public void apply(BidRequest bidRequest) {
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();
        if (this.canApplyBorrow(userinfo) &&
                !userinfo.getHasBidRequestProcess() &&
                bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT) >= 0 &&
                bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit()) <= 0 &&
                bidRequest.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE) >= 0 &&
                bidRequest.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE) <= 0 &&
                bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0
                ) {
            //创建BidRequest对象,设置相关属性
            BidRequest br = new BidRequest();
            br.setApplyTime(new Date());
            br.setBidRequestAmount(bidRequest.getBidRequestAmount());
            br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
            br.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);
            br.setCreateUser(UserContext.getCurrentUser());
            br.setCurrentRate(bidRequest.getCurrentRate());
            br.setDescription(bidRequest.getDescription());
            br.setDisableDays(bidRequest.getDisableDays());
            br.setMinBidAmount(bidRequest.getMinBidAmount());
            br.setMonthes2Return(bidRequest.getMonthes2Return());
            br.setReturnType(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);
            br.setTitle(bidRequest.getTitle());
            br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(), br.getBidRequestAmount(), br.getCurrentRate(), br.getMonthes2Return()));
            this.insert(br);
            userinfo.addState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
            userinfoService.update(userinfo);
        }
    }

    @Override
    public PageInfo queryPage(BidRequestQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getCurrentPage());
        List list = bidRequestMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void publishAudit(Long id, int state, String remark) {
        BidRequest bidRequest = this.selectByPrimaryKey(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING) {
            //创建审核历史对象
            BidRequestAuditHistory brah = new BidRequestAuditHistory();
            brah.setApplier(bidRequest.getCreateUser());
            brah.setApplyTime(bidRequest.getApplyTime());
            brah.setAuditor(UserContext.getCurrentUser());
            brah.setAuditTime(new Date());
            brah.setRemark(remark);
            brah.setBidRequestId(bidRequest.getId());
            brah.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);

            if (state == BidRequestAuditHistory.STATE_PASS) {
                brah.setState(BidRequestAuditHistory.STATE_PASS);
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
                bidRequest.setPublishTime(new Date());
                bidRequest.setNote(remark);
                bidRequest.setDisableDate(DateUtils.addDays(bidRequest.getPublishTime(), bidRequest.getDisableDays()));
            } else {
                brah.setState(BidRequestAuditHistory.STATE_REJECT);
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);

                //找到申请人,移除借款状态码
                Userinfo createUserinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
                createUserinfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
            }
            bidRequestAuditHistoryService.save(brah);
            this.updateByPrimaryKey(bidRequest);

        }

    }

    @Override
    public List<BidRequest> queryIndexList(BidRequestQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = bidRequestMapper.queryPage(qo);
        return list;
    }


    @Override
    public void bid(Long bidRequestId, BigDecimal amount) {
        BidRequest bidRequest = this.selectByPrimaryKey(bidRequestId);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_BIDDING) {
            if (!UserContext.getCurrentUser().getId().equals(bidRequest.getCreateUser().getId())) {
                Account account = accountService.getCurrent();
                if (amount.compareTo(bidRequest.getMinBidAmount()) >= 0 &&
                        amount.compareTo(account.getUsableAmount().min(bidRequest.getRemainAmount())) <= 0
                        ) {
                    //投标次数,投标金额增加
                    bidRequest.setBidCount(bidRequest.getBidCount() + 1);
                    bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));

                    //标属性
                    Bid bid = new Bid();
                    bid.setActualRate(bidRequest.getCurrentRate());
                    bid.setAvailableAmount(amount);
                    bid.setBidRequestId(bidRequest.getId());
                    bid.setBidRequestTitle(bidRequest.getTitle());
                    bid.setBidTime(new Date());
                    bid.setBidUser(UserContext.getCurrentUser());
                    bid.setBidRequestState(bidRequest.getBidRequestState());
                    bidService.save(bid);

                    //投标的对象
                    account.setUsableAmount(account.getUsableAmount().subtract(amount));
                    account.setFreezedAmount(account.getFreezedAmount().add(amount));
                    accountService.update(account);
                    //设置标流水
                    accountFlowService.createBidFlow(account, amount);

                    if (bidRequest.getCurrentSum().compareTo(bidRequest.getBidRequestAmount()) == 0) {
                        bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                        bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                    }
                    this.updateByPrimaryKey(bidRequest);
                }
            }
        }
    }

    //审核对象的抽取
    private void createBidRequestAuditHistory(int auditType, int state, String remark, BidRequest bidRequest) {
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        brah.setApplier(bidRequest.getCreateUser());
        brah.setApplyTime(new Date());
        brah.setAuditor(UserContext.getCurrentUser());
        brah.setAuditTime(new Date());
        brah.setBidRequestId(bidRequest.getId());
        brah.setRemark(remark);
        brah.setAuditType(auditType);
        if (state == BidRequestAuditHistory.STATE_PASS) {
            brah.setState(BidRequestAuditHistory.STATE_PASS);
        } else {
            brah.setState(BidRequestAuditHistory.STATE_REJECT);
        }
        bidRequestAuditHistoryService.save(brah);
    }

    //满标一审
    @Override
    public void audit(Long id, int state, String remark) {
        BidRequest bidRequest = bidRequestMapper.selectByPrimaryKey(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1) {
            createBidRequestAuditHistory(BidRequestAuditHistory.AUDIT1, state, remark, bidRequest);

            //审核通过
            if (state == BidRequestAuditHistory.STATE_PASS) {
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
                bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
            } else {
                auditReject(bidRequest);

            }
            this.updateByPrimaryKey(bidRequest);
        }
    }

    //抽取审核拒绝
    private void auditReject(BidRequest bidRequest) {
        //审核拒绝
        bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
        bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_REJECTED);

        //遍历投标集合,获取投资人的账户
        Long bidUserId;
        Account bidUserAccount;
        Map<Long, Account> accountMap = new HashMap<Long, Account>();
        for (Bid bid : bidRequest.getBids()) {
            bidUserId = bid.getBidUser().getId();
            bidUserAccount = accountMap.get(bidUserId);
            //如果账户为空,
            if (bidUserAccount == null) {
                bidUserAccount = accountService.selectByPrimaryKey(bidUserId);
                accountMap.put(bidUserId, bidUserAccount);
            }
            //冻结金额介绍,可用金额增加
            bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
            bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(bid.getAvailableAmount()));
            //生成投标失败流水
            accountFlowService.createBidFailFlow(bidUserAccount, bid.getAvailableAmount());
        }
        //对账户进行统一修改
        for (Account account : accountMap.values()) {
            accountService.update(account);
        }
        //找到借款人的userinfo,移除借款的状态码
        Userinfo createUserinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
        createUserinfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
        userinfoService.update(createUserinfo);
    }


    //满标二审
    @Override
    public void audit2(Long id, int state, String remark) {
        //根据id获取bidRequest对象,初愈满标二审状态
        BidRequest bidRequest = this.selectByPrimaryKey(id);//拿到传回来的id,查询当前id的对象
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2) {
            //创建审核对像
            createBidRequestAuditHistory(BidRequestAuditHistory.AUDIT2, state, remark, bidRequest);
            if (state == BidRequestAuditHistory.STATE_PASS) {
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);//设置还款中属性
                bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_PAYING_BACK);//更新状态码,显示还款中
                //审核通过,借款人增加金额
                Account createAccount = accountService.selectByPrimaryKey(bidRequest.getCreateUser().getId());//查询当前借款人的账户
                //可用金额增加,代还本息增加,剩余授信额度减少
                createAccount.setUsableAmount(createAccount.getUsableAmount().add(bidRequest.getBidRequestAmount())); //借款人的可用金额=借款人的可用金额借款金额
                createAccount.setUnReturnAmount(createAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount().add(bidRequest.getTotalRewardAmount())));
                createAccount.setRemainBorrowLimit(createAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount())); //借款人的剩余授信额度=借款人的原剩余授信额度-借款金额

                accountFlowService.createBorrowSuccessFlow(createAccount, bidRequest.getBidRequestAmount()); //生成借款成功流水

                //移除申请人的借款状态码
                Userinfo createUserinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
                createUserinfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
                userinfoService.update(createUserinfo);

                //支付平台借款手续费
                BigDecimal accountManagementCharge = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
                //借款人的可用金额减少没生成支付平台的手续费流水
                createAccount.setUsableAmount(createAccount.getUsableAmount().subtract(accountManagementCharge));
                accountFlowService.createPayAccountManagementChargeFailFlow(createAccount, accountManagementCharge);
                accountService.update(createAccount);

                //系统账户手续借款手续费
                SystemAccount systemAccount = systemAccountService.getCurrent();
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(accountManagementCharge));
                systemAccountService.update(systemAccount);
                //生成系统账户收取借款手续费流水
                systemAccountFlowService.createGainAccountManagementChargeFlow(systemAccount, accountManagementCharge);

                //遍历投标集合,获取到投资人的账户
                Long bidUserId;
                Account bidUserAccount;
                Map<Long, Account> accountMap = new HashMap<Long, Account>();
                for (Bid bid : bidRequest.getBids()) {
                    bidUserId = bid.getBidUser().getId();//设置key为投资人的id
                    bidUserAccount = accountMap.get(bidUserId);//拿到当前投资人的value
                    if (bidUserAccount == null) {
                        bidUserAccount = accountService.selectByPrimaryKey(bidUserId);//查询当前id的投资人
                        accountMap.put(bidUserId, bidUserAccount);//投资人对象存入map
                    }
                    //冻结资产减少,代收本金和代收利息增加
                    bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
                    //生成投标成功流水账单
                    accountFlowService.creteBidSuccessFlow(bidUserAccount, bid.getAvailableAmount());
                }
                List<PaymentSchedule> paymentSchedules = createPaymentScheduleList(bidRequest);
                //计算投资人的代收利息本金
                for (PaymentSchedule ps : paymentSchedules) {
                    for (PaymentScheduleDetail psd : ps.getDetails()) {
                        bidUserAccount = accountMap.get(psd.getInvestorId());
                        bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
                        bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().add(psd.getInterest()));
                    }
                }
                //对账户进行统一的修改
                for (Account account : accountMap.values()) {
                    accountService.update(account);
                }
            } else {
                auditReject(bidRequest);
            }
            this.updateByPrimaryKey(bidRequest);

        }
    }

    //创建还款表
    private List<PaymentSchedule> createPaymentScheduleList(BidRequest bidRequest) {
        List<PaymentSchedule> paymentSchedules = new ArrayList<PaymentSchedule>();
        PaymentSchedule ps;
        BigDecimal interestTemp = BigDecimal.ZERO;
        BigDecimal principalTemp = BigDecimal.ZERO;
        for (int i = 0; i < bidRequest.getMonthes2Return(); i++) {
            ps = new PaymentSchedule();
            ps.setBidRequestId(bidRequest.getId());//关联借款的id
            ps.setBidRequestTitle(bidRequest.getTitle());//关联借款的标题
            ps.setBidRequestType(bidRequest.getBidRequestType());//关联借款类型
            ps.setBorrowUser(bidRequest.getCreateUser());//关联借款人
            ps.setMonthIndex(i + 1);//是第几个月的还款对象
            ps.setDeadLine(DateUtils.addMonths(bidRequest.getPublishTime(), i + 1));//还款的截止时间(标的发布时间+期数*月)
            ps.setReturnType(bidRequest.getReturnType());
            /*ps.setInterest();
            ps.setPrincipal();
            ps.setTotalAmount();*/
            //判断是否最后一期还款
            if (i < bidRequest.getMonthes2Return() - 1) {
                //不是最后一期
                ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), //还款方式(按月分期)
                        bidRequest.getBidRequestAmount(), //还款金额
                        bidRequest.getCurrentRate(), //还款的年化利率
                        i + 1, //第几期还款
                        bidRequest.getMonthes2Return()));//还款期数

                ps.setInterest(CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(),
                        bidRequest.getBidRequestAmount(),
                        bidRequest.getCurrentRate(),
                        i + 1,
                        bidRequest.getMonthes2Return()));

                //该期本金=该期还款金额-该期利息
                ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
                interestTemp = interestTemp.add(ps.getInterest());
                principalTemp = principalTemp.add(ps.getPrincipal());
            } else {
                //是最后一期
                //该期本金=借款的本金-前N-1期的本金之和
                ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(principalTemp));
                //该期利息=借款利息-前N-1期的利息之和
                ps.setInterest(bidRequest.getTotalRewardAmount().subtract(interestTemp));
                //该期还款金额=该期的本金+利息
                ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal()));
            }
            paymentScheduleService.save(ps);
            createPaymentScheduleDetail(ps, bidRequest);
            paymentSchedules.add(ps);
        }
        return paymentSchedules;
    }

    //创建还款表明细
    private void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest bidRequest) {
        PaymentScheduleDetail psd;
        Bid bid;
        BigDecimal interestTemp = BigDecimal.ZERO;
        BigDecimal principalTemp = BigDecimal.ZERO;
        for (int i = 0; i < bidRequest.getBids().size(); i++) {
            bid = bidRequest.getBids().get(i);
            psd = new PaymentScheduleDetail();
            psd.setBidAmount(bid.getAvailableAmount());
            psd.setBidId(bid.getId());
            psd.setBidRequestId(bidRequest.getId());
            psd.setBorrowUser(bidRequest.getCreateUser());
            psd.setDeadLine(ps.getDeadLine());
            psd.setInvestorId(bid.getBidUser().getId());
            psd.setMonthIndex(ps.getMonthIndex());
            psd.setPaymentScheduleId(ps.getId());
            psd.setReturnType(ps.getReturnType());
           /*psd.setTotalAmount(totalAmount);
           psd.setInterest(interest);
           psd.setPrincipal(principal);*/

            //判断是否是最后一个投标人
            if (i < bidRequest.getBids().size() - 1) {
                //不是最后一个投标人
                BigDecimal bidRate = bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), BidConst.CAL_SCALE, RoundingMode.HALF_UP);
                //该还款明细的本金=(该次投标/借款金额)*改期还款的本金
                psd.setPrincipal(bidRate.multiply(ps.getPrincipal()).setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP));
                //该还款明细的利息=(该次投标/借款金额)*改期还款的利息
                psd.setInterest(bidRate.multiply(ps.getInterest()).setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP));
                //该还款明细的总金额(该还款明细的本金+该还款明细的利息
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
                principalTemp = principalTemp.add(psd.getPrincipal());
                interestTemp = interestTemp.add(psd.getInterest());
            } else {
                //是最后一个投标人
                //该还款明细的本金=该期还款本金-(前N-1投资人的本金之和)
                psd.setPrincipal(ps.getPrincipal().subtract(principalTemp));
                //该还款明细的利息=该期还款利息-(前N-1投资人的利息之和)
                psd.setInterest(ps.getInterest().subtract(interestTemp));
                //该还款的明细总金额=该还款的明细本金+该还款的明细利息
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
            }

            paymentScheduleDetailService.save(psd);
            ps.getDetails().add(psd);
        }
    }
}
