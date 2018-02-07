package com.ham.p2p.base.mapper;

import com.ham.p2p.base.domain.Iplog;
import com.ham.p2p.base.query.IpLogQueryObject;
import java.util.List;

public interface IplogMapper {


    int insert(Iplog record);

    List<Iplog> queryPage(IpLogQueryObject qo);
}