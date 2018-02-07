package com.ham.p2p.base.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Systemdictionary;
import com.ham.p2p.base.query.SystemDictionaryQueryObject;

import java.util.List;

public interface ISystemDictionaryService {

    void save(Systemdictionary record);

    void update(Systemdictionary record);
    Systemdictionary get(Long id);

    PageInfo queryPage(SystemDictionaryQueryObject qo);

    void saveOrUpdate(Systemdictionary systemdictionary);

    List<Systemdictionary> selectAll();
}
