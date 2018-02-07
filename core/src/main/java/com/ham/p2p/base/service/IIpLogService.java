package com.ham.p2p.base.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Iplog;
import com.ham.p2p.base.query.IpLogQueryObject;

public interface IIpLogService {
    int save(Iplog record);

    PageInfo queryPage(IpLogQueryObject qo);
}
