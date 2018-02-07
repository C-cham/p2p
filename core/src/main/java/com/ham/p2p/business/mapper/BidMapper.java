package com.ham.p2p.business.mapper;

import com.ham.p2p.business.domain.Bid;
import com.ham.p2p.business.domain.BidRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BidMapper {

    int insert(Bid record);

    Bid selectByPrimaryKey(Long id);


    int updateByPrimaryKey(Bid record);

    List<BidRequest> queryListByRequestId(Long bidRequestId);

    void updateState(@Param("bidRequestId") Long bidRequestId, @Param("bidrequestState") int bidrequestState);
}