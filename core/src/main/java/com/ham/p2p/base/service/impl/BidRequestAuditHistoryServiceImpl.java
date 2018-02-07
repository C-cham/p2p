package com.ham.p2p.base.service.impl;

import com.ham.p2p.business.domain.BidRequestAuditHistory;
import com.ham.p2p.business.mapper.BidRequestAuditHistoryMapper;
import com.ham.p2p.business.service.IBidRequestAuditHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service@Transactional
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {

    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;

    @Override
    public int save(BidRequestAuditHistory bidRequestAuditHistory) {
        return bidRequestAuditHistoryMapper.insert(bidRequestAuditHistory);
    }

    @Override
    public List<BidRequestAuditHistory> queryByBidRequestId(Long bidRequestId) {
        return bidRequestAuditHistoryMapper.queryByBidRequestId(bidRequestId);
    }
}
