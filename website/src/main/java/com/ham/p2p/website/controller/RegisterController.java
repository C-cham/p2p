package com.ham.p2p.website.controller;

import com.ham.p2p.base.service.ILogininfoService;
import com.ham.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {
    @Autowired
    private ILogininfoService logininfoService;

    @RequestMapping("/userRegister")
    @ResponseBody
    public AjaxResult test(String username, String password) {
        AjaxResult ajaxResult = new AjaxResult();

        try {
            logininfoService.register(username, password);
            ajaxResult = new AjaxResult("注册成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult("注册失败");
        }
        return ajaxResult;
    }

    @RequestMapping("/checkUsername")
    @ResponseBody
    public boolean checkUsername(String username) {
        return logininfoService.checkUsername(username);
    }
}
