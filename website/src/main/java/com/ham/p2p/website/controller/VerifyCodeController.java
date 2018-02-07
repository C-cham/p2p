package com.ham.p2p.website.controller;

import com.ham.p2p.base.service.IVerifyCodeService;
import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.website.annotation.RequiredLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VerifyCodeController {
    @Autowired
    private IVerifyCodeService verifyCodeService;

    @RequestMapping("/sendVerifyCode")
    @ResponseBody
    @RequiredLogin
    public AjaxResult sendVerifyCode(String phoneNumber) {

        //后台发送代码
        AjaxResult ajaxResult = null;
        try {
            ajaxResult = new AjaxResult("发送成功", true);
            verifyCodeService.sendVerifyCode(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }
}
