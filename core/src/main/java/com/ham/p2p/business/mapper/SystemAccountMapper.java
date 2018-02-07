package com.ham.p2p.business.mapper;

import com.ham.p2p.business.domain.SystemAccount;

public interface SystemAccountMapper {

    int insert(SystemAccount record);



    int updateByPrimaryKey(SystemAccount record);


    SystemAccount selectByCurrent();
}