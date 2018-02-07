package com.ham.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UserFile extends BaseAuthDomain {
    private String image;//风控材料图片
    private int score;//风控材料得分
    private SystemDictionaryItem fileType;//风控材料类型

    public String getJsonString() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("applier", applier.getUsername());
        param.put("fileType", fileType.getTitle());
        param.put("image", image);
        return JSON.toJSONString(param);
    }
}
