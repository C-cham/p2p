package com.ham.p2p.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Iplog;
import com.ham.p2p.base.mapper.IplogMapper;
import com.ham.p2p.base.query.IpLogQueryObject;
import com.ham.p2p.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IpLogServiceImpl implements IIpLogService {
    @Autowired
    private IplogMapper iplogMapper;

    @Override
    public int save(Iplog record) {
        return iplogMapper.insert(record);
    }

    @Override
    public PageInfo queryPage(IpLogQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());

        List<Iplog> list = iplogMapper.queryPage(qo);
        return new PageInfo(list);
    }
}
