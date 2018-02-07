package com.ham.p2p.base.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.SystemDictionaryItem;
import com.ham.p2p.base.query.SystemDictionaryItemQueryObject;

import java.util.List;

public interface ISystemDictionaryItemService {
    int save(SystemDictionaryItem record);

    SystemDictionaryItem selectByPrimaryKey(Long id);

    int update(SystemDictionaryItem record);

    PageInfo queryPage(SystemDictionaryItemQueryObject qo);

    void saveOrUpdate(SystemDictionaryItem systemDictionaryItem);

    List<SystemDictionaryItem> queryByParentSn(String sn);
}
