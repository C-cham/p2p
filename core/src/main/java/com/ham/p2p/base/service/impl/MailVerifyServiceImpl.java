package com.ham.p2p.base.service.impl;

import com.ham.p2p.base.domain.MailVerify;
import com.ham.p2p.base.mapper.MailVerifyMapper;
import com.ham.p2p.base.service.IMailVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service@Transactional
public class MailVerifyServiceImpl implements IMailVerifyService {
    @Autowired
    private MailVerifyMapper mailVerifyMapper;
    @Override
    public int save(MailVerify mailVerify) {
        return mailVerifyMapper.insert(mailVerify);
    }

    @Override
    public MailVerify selectByUUID(String uuid) {
        return mailVerifyMapper.selectByUUID(uuid);
    }
}
