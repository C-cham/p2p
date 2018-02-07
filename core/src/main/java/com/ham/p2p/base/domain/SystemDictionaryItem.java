package com.ham.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SystemDictionaryItem extends BaseDomain {
    private Long parentId;

    private String title;

    private Byte sequence;

    public String getJsonString() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id",id);
        param.put("parentId",parentId);
        param.put("sn",sequence);
        param.put("title",title);
        return JSON.toJSONString(param);
    }


}