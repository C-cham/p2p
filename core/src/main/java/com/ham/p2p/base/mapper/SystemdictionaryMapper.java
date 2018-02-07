package com.ham.p2p.base.mapper;

import com.ham.p2p.base.domain.Systemdictionary;
import com.ham.p2p.base.query.SystemDictionaryQueryObject;
import java.util.List;

public interface SystemdictionaryMapper {


    int insert(Systemdictionary record);

    Systemdictionary selectByPrimaryKey(Long id);

    List<Systemdictionary> selectAll();

    int updateByPrimaryKey(Systemdictionary record);

    List queryPage(SystemDictionaryQueryObject qo);
}