package com.ham.p2p.base.service;

import com.ham.p2p.base.domain.Userinfo;

public interface IUserinfoService {
    int save(Userinfo record);

    Userinfo selectByPrimaryKey(Long id);

    Userinfo getCurrent();

    int update(Userinfo record);

    void bindPhone(String phoneNumber, String verifyCode);

    void bindEmail(String key);

    void basicInfoSave(Userinfo userinfo);
}
