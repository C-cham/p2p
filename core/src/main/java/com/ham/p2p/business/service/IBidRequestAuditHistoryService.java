package com.ham.p2p.business.service;

import com.ham.p2p.business.domain.BidRequestAuditHistory;

import java.util.List;

public interface IBidRequestAuditHistoryService {
    int save(BidRequestAuditHistory bidRequestAuditHistory);

    List<BidRequestAuditHistory> queryByBidRequestId(Long bidRequestId);
}
