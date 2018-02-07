package com.ham.p2p.business.mapper;

import com.ham.p2p.business.domain.PlatFormBankInfo;
import com.ham.p2p.business.query.PlatFormBankInfoQueryObject;
import java.util.List;

public interface PlatFormBankInfoMapper {

    int insert(PlatFormBankInfo record);

    PlatFormBankInfo selectByPrimaryKey(Long id);


    int updateByPrimaryKey(PlatFormBankInfo record);

    List queryPage(PlatFormBankInfoQueryObject qo);

    List<PlatFormBankInfo> selectAll();
}