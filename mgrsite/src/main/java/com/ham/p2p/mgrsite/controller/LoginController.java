package com.ham.p2p.mgrsite.controller;

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

    @RequestMapping("/managerLogin")
    @ResponseBody
    public AjaxResult managerLogin(String username, String password) {
        AjaxResult ajaxResult = null;
        Logininfo logininfo = logininfoService.login(username, password, Logininfo.USERTYPE_MANAGER);
        if (logininfo != null) {
            ajaxResult = new AjaxResult("登录成功", true);
        } else {
            ajaxResult = new AjaxResult("登录失败");

        }
        return ajaxResult;
    }

    @RequestMapping("/index")
public String indexPage(){
        return "main";
}
}
