package com.ham.p2p.base.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.SystemDictionaryItem;
import com.ham.p2p.base.mapper.SystemDictionaryItemMapper;
import com.ham.p2p.base.query.SystemDictionaryItemQueryObject;
import com.ham.p2p.base.service.ISystemDictionaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService {

    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    @Override
    public int save(SystemDictionaryItem record) {
        return systemDictionaryItemMapper.insert(record);
    }

    @Override
    public SystemDictionaryItem selectByPrimaryKey(Long id) {
        return systemDictionaryItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(SystemDictionaryItem record) {
        return systemDictionaryItemMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo queryPage(SystemDictionaryItemQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = systemDictionaryItemMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void saveOrUpdate(SystemDictionaryItem systemDictionaryItem) {
        if (systemDictionaryItem.getId() == null) {
            this.save(systemDictionaryItem);
        } else {
            this.update(systemDictionaryItem);
        }
    }

    @Override
    public List<SystemDictionaryItem> queryByParentSn(String sn) {
        return systemDictionaryItemMapper.queryListByParentSn(sn);
    }
}
