package com.ham.p2p.business.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.business.domain.PlatFormBankInfo;
import com.ham.p2p.business.query.PlatFormBankInfoQueryObject;

import java.util.List;

public interface IPlatFormBankInfoService {
    int save(PlatFormBankInfo platFormBankInfo);

    int update(PlatFormBankInfo platFormBankInfo);

    PlatFormBankInfo get(Long id);

    PageInfo queryPage(PlatFormBankInfoQueryObject qo);

    void saveOrUpdate(PlatFormBankInfo platFormBankInfo);

    List<PlatFormBankInfo> selectAll();
}
