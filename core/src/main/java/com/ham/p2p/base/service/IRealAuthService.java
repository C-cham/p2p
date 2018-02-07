package com.ham.p2p.base.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.RealAuth;
import com.ham.p2p.base.query.RealAuthQueryObject;

public interface IRealAuthService {
    int save(RealAuth realAuth);

    int update(RealAuth realAuth);

    RealAuth get(Long id);

    void realAuthSave(RealAuth realAuth);

    PageInfo queryPage(RealAuthQueryObject qo);

    void audit(Long id, int state, String remark);
}
