package com.ham.p2p.base.mapper;

import com.ham.p2p.base.domain.VedioAuth;
import com.ham.p2p.base.query.VedioAuthQueryObject;
import java.util.List;

public interface VedioAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VedioAuth record);

    VedioAuth selectByPrimaryKey(Long id);


    int updateByPrimaryKey(VedioAuth record);

    List queryPage(VedioAuthQueryObject qo);
}