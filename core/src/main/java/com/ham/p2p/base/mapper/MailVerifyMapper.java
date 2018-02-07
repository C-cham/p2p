package com.ham.p2p.base.mapper;


import com.ham.p2p.base.domain.MailVerify;

public interface MailVerifyMapper {
    int insert(MailVerify record);

    MailVerify selectByUUID(String uuid);
}