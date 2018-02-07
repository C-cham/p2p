package com.ham.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Iplog extends BaseDomain {
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILED = 1;
    private String ip;

    private int state;
    private int userType;

    private String username;

    private Date logintime;

    public String getStateDisplay() {
        return this.state == LOGIN_SUCCESS ? "登录成功" : "登录失败";
    }

}