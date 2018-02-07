package com.ham.p2p.base.mapper;

import com.ham.p2p.base.domain.RealAuth;
import com.ham.p2p.base.query.RealAuthQueryObject;
import java.util.List;

public interface RealAuthMapper {


    int insert(RealAuth record);

    RealAuth selectByPrimaryKey(Long id);

    List<RealAuth> selectAll();

    int updateByPrimaryKey(RealAuth record);

    List queryPage(RealAuthQueryObject qo);
}