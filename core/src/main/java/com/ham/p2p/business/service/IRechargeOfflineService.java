package com.ham.p2p.business.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.business.domain.RechargeOffline;
import com.ham.p2p.business.query.RechargeOfflineQueryObject;

public interface IRechargeOfflineService {
    int save(RechargeOffline record);

    RechargeOffline get(Long id);


    int update(RechargeOffline record);

    void apply(RechargeOffline rechargeOffline);

    PageInfo queryPage(RechargeOfflineQueryObject qo);

    /**
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, int state, String remark);
}
