package com.ham.p2p.base.mapper;

import com.ham.p2p.base.domain.SystemDictionaryItem;
import com.ham.p2p.base.query.SystemDictionaryItemQueryObject;
import java.util.List;

public interface SystemDictionaryItemMapper {


    int insert(SystemDictionaryItem record);

    SystemDictionaryItem selectByPrimaryKey(Long id);

    List<SystemDictionaryItem> selectAll();

    int updateByPrimaryKey(SystemDictionaryItem record);

    List queryPage(SystemDictionaryItemQueryObject qo);

    List<SystemDictionaryItem> queryListByParentSn(String sn);
}