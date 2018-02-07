package com.ham.p2p.business.mapper;

import com.ham.p2p.business.domain.RechargeOffline;
import com.ham.p2p.business.query.RechargeOfflineQueryObject;
import java.util.List;

public interface RechargeOfflineMapper {

    int insert(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);


    int updateByPrimaryKey(RechargeOffline record);

    List queryPage(RechargeOfflineQueryObject qo);
}