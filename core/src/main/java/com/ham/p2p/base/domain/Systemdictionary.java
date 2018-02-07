package com.ham.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Systemdictionary extends BaseDomain {

    private String sn;

    private String title;

    public String getJsonString() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id",id);
        param.put("sn",sn);
        param.put("title",title);
        return JSON.toJSONString(param);
    }


}