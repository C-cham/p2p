package com.ham.p2p.base.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.VedioAuth;
import com.ham.p2p.base.query.VedioAuthQueryObject;

public interface IVedioAuthSerivce {
    int save(VedioAuth vedioAuth);

    int update(VedioAuth vedioAuth);

    VedioAuth get(Long id);


    PageInfo queryPage(VedioAuthQueryObject qo);

    void audit(Long loginInfoValue, int state, String remark);
}
