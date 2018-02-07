package com.ham.p2p.website.controller;

import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.base.util.UserContext;
import com.ham.p2p.website.annotation.RequiredLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PersonalController {
    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserinfoService userinfoService;

    @RequestMapping("/personal")
    @RequiredLogin
    public String personalPage(Model model) {
        model.addAttribute("account", accountService.getCurrent());
        model.addAttribute("userinfo", userinfoService.getCurrent());

        return "personal";
    }

    @RequestMapping("/bindPhone")
    @ResponseBody
    @RequiredLogin
    public AjaxResult bindPhone(String phoneNumber, String verifyCode) {
        AjaxResult ajaxResult = null;
        try {
            userinfoService.bindPhone(phoneNumber,verifyCode);
            ajaxResult = new AjaxResult("", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());

        }
        return ajaxResult;
    }

    @RequestMapping("/bindEmail")
    public String bindEmail(String key,Model model){
        try {
            userinfoService.bindEmail(key);
            model.addAttribute("success",true);
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("success",false);
            model.addAttribute("msg",e.getMessage());
        }
        return "checkmail_result";
    }
}
