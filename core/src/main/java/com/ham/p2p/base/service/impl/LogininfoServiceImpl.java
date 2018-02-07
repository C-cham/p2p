package com.ham.p2p.base.service.impl;

import com.ham.p2p.base.domain.Account;
import com.ham.p2p.base.domain.Iplog;
import com.ham.p2p.base.domain.Logininfo;
import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.mapper.LogininfoMapper;
import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.service.IIpLogService;
import com.ham.p2p.base.service.ILogininfoService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.base.util.MD5;
import com.ham.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LogininfoServiceImpl implements ILogininfoService {
    @Autowired
    private LogininfoMapper logininfoMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IIpLogService ipLogService;

    @Override
    public Logininfo register(String username, String password) {

        int count = logininfoMapper.selectCountByUsername(username);
        if (count > 0) {
            throw new RuntimeException("该账户已存在");
        }
        //如果不存在,保存进数据库
        Logininfo logininfo = new Logininfo();
        logininfo.setUsername(username);
        logininfo.setPassword(MD5.encode(password));
        logininfo.setState(Logininfo.STATE_NORMAL);
        logininfo.setUserType(Logininfo.USERTYPE_USER);
        logininfoMapper.insert(logininfo);

        Account account = new Account();
        account.setId(logininfo.getId());
        accountService.save(account);

        Userinfo userinfo = new Userinfo();
        userinfo.setId(logininfo.getId());
        userinfoService.save(userinfo);

        return logininfo;
    }

    public boolean checkUsername(String username) {
        return logininfoMapper.selectCountByUsername(username) <= 0;
    }

    @Override
    public Logininfo login(String username, String password, int userType) {
        Logininfo logininfo = logininfoMapper.login(username, MD5.encode(password), userType);

        //记录登录日志
        Iplog iplog = new Iplog();
        iplog.setUsername(username);
        iplog.setLogintime(new Date());
        iplog.setIp(UserContext.getIp());
        iplog.setUserType(userType);


        if (logininfo != null) {
            iplog.setState(Iplog.LOGIN_SUCCESS);
            UserContext.setCurrentUser(logininfo);
            ipLogService.save(iplog);
        } else {
            iplog.setState(Iplog.LOGIN_FAILED);
            ipLogService.save(iplog);
        }
        return logininfo;
    }

    @Override
    public void initAdmin() {
        int count = logininfoMapper.queryCountByUserType(Logininfo.USERTYPE_MANAGER);
        if (count <= 0) {
            Logininfo logininfo = new Logininfo();
            logininfo.setState(Logininfo.STATE_NORMAL);
            logininfo.setUserType(Logininfo.USERTYPE_MANAGER);
            logininfo.setUsername(BidConst.MANAGER_ACCOUNT);
            logininfo.setPassword(MD5.encode(BidConst.MANAGER_PASSWORD));
            this.logininfoMapper.insert(logininfo);
        }
    }

    @Override
    public List<Map<String, Object>> autoComplate(String keyword) {
        return logininfoMapper.autoComplate(keyword, Logininfo.USERTYPE_USER);
    }
}
