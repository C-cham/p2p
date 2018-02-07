package com.ham.p2p.mgrsite.listener;

import com.ham.p2p.base.service.ILogininfoService;
import com.ham.p2p.business.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitAdminListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ILogininfoService logininfoService;

    @Autowired
    private ISystemAccountService systemAccountService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logininfoService.initAdmin();
        systemAccountService.initSystemAccount();
    }
}
