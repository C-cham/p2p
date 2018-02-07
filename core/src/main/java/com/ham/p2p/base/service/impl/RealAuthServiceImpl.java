package com.ham.p2p.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.RealAuth;
import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.mapper.RealAuthMapper;
import com.ham.p2p.base.query.RealAuthQueryObject;
import com.ham.p2p.base.service.IRealAuthService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.BitStatesUtils;
import com.ham.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RealAuthServiceImpl implements IRealAuthService {
    @Autowired
    private RealAuthMapper realAuthMapper;
    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public int save(RealAuth realAuth) {
        return realAuthMapper.insert(realAuth);
    }

    @Override
    public int update(RealAuth realAuth) {
        return realAuthMapper.updateByPrimaryKey(realAuth);
    }

    @Override
    public RealAuth get(Long id) {
        return realAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public void realAuthSave(RealAuth realAuth) {
        RealAuth ra = new RealAuth();
        ra.setAddress(realAuth.getAddress());
        ra.setApplier(UserContext.getCurrentUser());
        ra.setApplyTime(new Date());
        ra.setBornDate(realAuth.getBornDate());
        ra.setIdNumber(realAuth.getIdNumber());
        ra.setImage1(realAuth.getImage1());
        ra.setImage2(realAuth.getImage2());
        ra.setRealName(realAuth.getRealName());
        ra.setSex(realAuth.getSex());
        ra.setState(realAuth.STATE_NORMAL);
        this.save(ra);
        //把实名认证对象设置到userinfo中的realAuthId字段中
        Userinfo userinfo = userinfoService.getCurrent();
        userinfo.setRealAuthId(ra.getId());
        userinfoService.update(userinfo);
    }

    @Override
    public PageInfo queryPage(RealAuthQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = realAuthMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        RealAuth realAuth = this.get(id);
        if (realAuth != null && realAuth.getState() == realAuth.STATE_NORMAL) {
            realAuth.setAuditor(UserContext.getCurrentUser());
            realAuth.setAuditTime(new Date());
            realAuth.setRemark(remark);

            Userinfo applierUserinfo = userinfoService.selectByPrimaryKey(realAuth.getApplier().getId());
            if (state == RealAuth.STATE_PASS) {
                realAuth.setState(RealAuth.STATE_PASS);
                applierUserinfo.setBitState(BitStatesUtils.OP_REAL_AUTH);
                applierUserinfo.setRealName(realAuth.getRealName());
                applierUserinfo.setIdNumber(realAuth.getIdNumber());
            } else {
                realAuth.setState(RealAuth.STATE_REJECT);
                applierUserinfo.setRealAuthId(null);
            }
            this.update(realAuth);
            userinfoService.update(applierUserinfo);
        }
    }
}
