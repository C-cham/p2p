package com.ham.p2p.base.service;

import com.ham.p2p.base.domain.Logininfo;

import java.util.List;
import java.util.Map;

public interface ILogininfoService {
    Logininfo register(String username, String password);

    boolean checkUsername(String username);

    Logininfo login(String username, String password, int userType);

    //初始化后台管理员
    void initAdmin();

    List<Map<String, Object>> autoComplate(String keyword);
}
