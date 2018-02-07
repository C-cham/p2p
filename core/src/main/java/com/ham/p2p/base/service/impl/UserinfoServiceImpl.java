package com.ham.p2p.base.service.impl;

import com.ham.p2p.base.domain.MailVerify;
import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.mapper.UserinfoMapper;
import com.ham.p2p.base.service.IMailVerifyService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.service.IVerifyCodeService;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.base.util.BitStatesUtils;
import com.ham.p2p.base.util.DateUtil;
import com.ham.p2p.base.util.UserContext;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserinfoServiceImpl implements IUserinfoService {
    @Autowired
    private UserinfoMapper userinfoMapper;

    @Autowired
    private IVerifyCodeService verifyCodeService;
    @Autowired
    private IMailVerifyService mailVerifyService;

    @Override
    public int save(Userinfo record) {
        return userinfoMapper.insert(record);
    }

    @Override
    public Userinfo selectByPrimaryKey(Long id) {
        return userinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Userinfo getCurrent() {
        return this.selectByPrimaryKey(UserContext.getCurrentUser().getId());
    }

    @Override
    public int update(Userinfo record) {

        int count = userinfoMapper.updateByPrimaryKey(record);
        if (count <= 0) {
            throw new RuntimeException("乐观锁异常,userinfoId" + record.getId());
        }
        return count;
    }

    @Override
    public void bindPhone(String phoneNumber, String verifyCode) {
        boolean isValid = verifyCodeService.validate(phoneNumber, verifyCode);
        if (!isValid) {
            throw new RuntimeException("验证码有误,请重新输入");
        }
        Userinfo userinfo = this.getCurrent();
        if (userinfo.getHasBindPhone()) {
            throw new RuntimeException("您已经绑定手机号码,请不要重复绑定");
        }

        userinfo.addState(BitStatesUtils.OP_BIND_PHONE);
        userinfo.setPhoneNumber(phoneNumber);
        this.update(userinfo);
    }

    @Override
    public void bindEmail(String key) {
        //从数据库中根据uuid查询记录
        MailVerify mailVerify = mailVerifyService.selectByUUID(key);
        if (mailVerify == null) {
            throw new RuntimeException("验证邮箱地址有误,请重新发送");
        }
        //判断邮箱地址是否在有效时间内
        if (DateUtil.getBetweenTime(mailVerify.getSendTime(), new Date()) > BidConst.EMAIL_VALID_TIME * 24 * 60 * 60) {
            throw new RuntimeException("验证邮箱已失效,请重新发送");
        }
        //判断用户是否已经绑定邮箱了
        Userinfo userinfo = this.selectByPrimaryKey(mailVerify.getUserId());
        if (userinfo.getHasBindEmail()) {
            throw new RuntimeException("您已经绑定了邮箱,请不要重复绑定!");
        }
        //给用户添加邮箱的验证码
        userinfo.addState(BitStatesUtils.OP_BIND_EMAIL);
        //给用户的userinfo中的email设置值
        userinfo.setEmail(mailVerify.getEmail());
        //更新userinfo对象
        this.update(userinfo);
    }

    @Override
    public void basicInfoSave(Userinfo userinfo) {
        Userinfo currentUserInfo = this.getCurrent();
        currentUserInfo.setEducationBackground(userinfo.getEducationBackground());
        currentUserInfo.setHouseCondition(userinfo.getHouseCondition());
        currentUserInfo.setKidCount(userinfo.getKidCount());
        currentUserInfo.setMarriage(userinfo.getMarriage());
        currentUserInfo.setIncomeGrade(userinfo.getIncomeGrade());
        if(!currentUserInfo.getIsBasicInfo()){
            currentUserInfo.addState(BitStatesUtils.OP_BASIC_INFO);
        }
        this.update(currentUserInfo);
    }
}
