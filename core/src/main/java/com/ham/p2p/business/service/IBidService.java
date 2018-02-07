package com.ham.p2p.business.service;

import com.ham.p2p.business.domain.Bid;

public interface IBidService {
    int save(Bid record);

    Bid get(Long id);


    int update(Bid record);

    /**
     * 批量修改投标状态
     *
     * @param bidRequestId
     * @param bidrequestState
     */
    void updateState(Long bidRequestId, int bidrequestState);
}
