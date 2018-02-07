package com.ham.p2p.base.service;

public interface IVerifyCodeService {

    void sendVerifyCode(String phoneNumber);

    boolean validate(String phoneNumber, String verifyCode);
}
