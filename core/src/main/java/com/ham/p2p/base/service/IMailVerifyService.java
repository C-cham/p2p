package com.ham.p2p.base.service;


import com.ham.p2p.base.domain.MailVerify;

public interface IMailVerifyService {
    int save(MailVerify mailVerify);

    MailVerify selectByUUID(String uuid);
}
