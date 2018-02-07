package com.ham.p2p.business.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.business.domain.BidRequest;
import com.ham.p2p.business.query.BidRequestQueryObject;

import java.math.BigDecimal;
import java.util.List;

public interface IBidRequestService {
    int insert(BidRequest record);

    BidRequest selectByPrimaryKey(Long id);


    int updateByPrimaryKey(BidRequest record);

    boolean canApplyBorrow(Userinfo userinfo);

    void apply(BidRequest bidRequest);

    PageInfo queryPage(BidRequestQueryObject qo);

    void publishAudit(Long id, int state, String remark);

    List<BidRequest> queryIndexList(BidRequestQueryObject qo);

    void bid(Long bidRequestId, BigDecimal amount);

    void audit(Long id, int state, String remark);

    void audit2(Long id, int state, String remark);
}
