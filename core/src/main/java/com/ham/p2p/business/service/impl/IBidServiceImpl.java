package com.ham.p2p.business.service.impl;

import com.ham.p2p.business.domain.Bid;
import com.ham.p2p.business.mapper.BidMapper;
import com.ham.p2p.business.service.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IBidServiceImpl implements IBidService {
    @Autowired
    private BidMapper bidMapper;

    @Override
    public int save(Bid record) {
        return bidMapper.insert(record);
    }

    @Override
    public Bid get(Long id) {
        return bidMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Bid record) {
        return bidMapper.updateByPrimaryKey(record);
    }

    @Override
    public void updateState(Long bidRequestId, int bidrequestState) {
bidMapper.updateState(bidRequestId,bidrequestState);
    }
}
