package com.ham.p2p.business.domain;

import com.alibaba.fastjson.JSON;
import com.ham.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class PlatFormBankInfo extends BaseDomain {
    private String bankName;
    private String accountNumber;
    private String bankForkName;
    private String accountName;

    public String getJsonString(){
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("id",id);
        param.put("bankName",bankName);
        param.put("accountName",accountName);
        param.put("accountNumber",accountNumber);
        param.put("bankForkName",bankForkName);
        return JSON.toJSONString(param);
    }
}
