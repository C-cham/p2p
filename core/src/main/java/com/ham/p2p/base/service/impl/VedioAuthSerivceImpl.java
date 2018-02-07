package com.ham.p2p.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.Logininfo;
import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.domain.VedioAuth;
import com.ham.p2p.base.mapper.VedioAuthMapper;
import com.ham.p2p.base.query.VedioAuthQueryObject;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.service.IVedioAuthSerivce;
import com.ham.p2p.base.util.BitStatesUtils;
import com.ham.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VedioAuthSerivceImpl implements IVedioAuthSerivce {
    @Autowired
    private VedioAuthMapper vedioAuthMapper;

    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public int save(VedioAuth vedioAuth) {
        return vedioAuthMapper.insert(vedioAuth);
    }

    @Override
    public int update(VedioAuth vedioAuth) {
        return vedioAuthMapper.updateByPrimaryKey(vedioAuth);
    }

    @Override
    public VedioAuth get(Long id) {
        return null;
    }

    @Override
    public PageInfo queryPage(VedioAuthQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = vedioAuthMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long loginInfoValue, int state, String remark) {
        Userinfo userinfo = userinfoService.selectByPrimaryKey(loginInfoValue);
        if (userinfo != null && !userinfo.getIsVedioAuth()) {
            VedioAuth vedioAuth = new VedioAuth();
            Logininfo applier = new Logininfo();
            applier.setId(loginInfoValue);
            vedioAuth.setApplier(applier);
            vedioAuth.setApplyTime(new Date());
            vedioAuth.setAuditor(UserContext.getCurrentUser());
            vedioAuth.setAuditTime(new Date());
            vedioAuth.setRemark(remark);
            if (state == VedioAuth.STATE_PASS) {
                vedioAuth.setState(VedioAuth.STATE_PASS);
                userinfo.addState(BitStatesUtils.OP_VEDIO_AUTH);
                userinfoService.update(userinfo);
            } else {
                vedioAuth.setState(VedioAuth.STATE_REJECT);
            }
            this.save(vedioAuth);
        }
    }


}
