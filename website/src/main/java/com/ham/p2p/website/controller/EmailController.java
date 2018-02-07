package com.ham.p2p.website.controller;

import com.ham.p2p.base.service.IEmailService;
import com.ham.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailController {
    @Autowired
    private IEmailService emailService;

    @RequestMapping("/sendEmail")
    @ResponseBody
    public AjaxResult sendEmail(String email) {
        try {

            emailService.sendEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxResult(e.getMessage());
        }
        return new AjaxResult("发送邮件成功", true);
    }
}
