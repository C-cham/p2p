package com.ham.p2p.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.business.domain.PlatFormBankInfo;
import com.ham.p2p.business.mapper.PlatFormBankInfoMapper;
import com.ham.p2p.business.query.PlatFormBankInfoQueryObject;
import com.ham.p2p.business.service.IPlatFormBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPlatFormBankInfoServiceImpl implements IPlatFormBankInfoService {
    @Autowired
    private PlatFormBankInfoMapper platFormBankInfoMapper;

    @Override
    public int save(PlatFormBankInfo platFormBankInfo) {
        return platFormBankInfoMapper.insert(platFormBankInfo);
    }

    @Override
    public int update(PlatFormBankInfo platFormBankInfo) {
        return platFormBankInfoMapper.updateByPrimaryKey(platFormBankInfo);
    }

    @Override
    public PlatFormBankInfo get(Long id) {
        return platFormBankInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(PlatFormBankInfoQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = platFormBankInfoMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void saveOrUpdate(PlatFormBankInfo platFormBankInfo) {
        if (platFormBankInfo.getId() == null) {
            this.save(platFormBankInfo);
        } else {
            this.update(platFormBankInfo);
        }
    }

    @Override
    public List<PlatFormBankInfo> selectAll() {
        return platFormBankInfoMapper.selectAll();
    }
}
