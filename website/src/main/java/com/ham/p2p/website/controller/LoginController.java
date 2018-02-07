package com.ham.p2p.website.controller;

import com.ham.p2p.base.domain.Logininfo;
import com.ham.p2p.base.service.ILogininfoService;
import com.ham.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    private ILogininfoService logininfoService;

    @RequestMapping("/userLogin")
    @ResponseBody
    public AjaxResult userLogin(String username, String password) {
        AjaxResult ajaxResult = new AjaxResult();


        Logininfo logininfo = logininfoService.login(username, password,Logininfo.USERTYPE_USER);

        if (logininfo != null) {

            ajaxResult = new AjaxResult("登录成功", true);
        } else {
            ajaxResult = new AjaxResult("用户名或密码错误");
        }

        return ajaxResult;
    }

}
