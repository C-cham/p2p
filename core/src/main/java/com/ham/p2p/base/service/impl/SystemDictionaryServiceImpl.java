package com.ham.p2p.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Systemdictionary;
import com.ham.p2p.base.mapper.SystemdictionaryMapper;
import com.ham.p2p.base.query.SystemDictionaryQueryObject;
import com.ham.p2p.base.service.ISystemDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {
    @Autowired
    private SystemdictionaryMapper systemdictionaryMapper;

    @Override
    public void save(Systemdictionary record) {
        systemdictionaryMapper.insert(record);
    }

    @Override
    public void update(Systemdictionary record) {
        systemdictionaryMapper.updateByPrimaryKey(record);
    }

    @Override
    public Systemdictionary get(Long id) {
        return systemdictionaryMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(SystemDictionaryQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = systemdictionaryMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void saveOrUpdate(Systemdictionary systemdictionary) {
        if(systemdictionary.getId()==null){
            this.save(systemdictionary);
        }else {
            this.update(systemdictionary);
        }
    }

    @Override
    public List<Systemdictionary> selectAll() {
        return systemdictionaryMapper.selectAll();
    }
}
