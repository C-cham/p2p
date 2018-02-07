package com.ham.p2p.mgrsite.controller;

import com.ham.p2p.base.query.RealAuthQueryObject;
import com.ham.p2p.base.service.IRealAuthService;
import com.ham.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RealAuthController {
    @Autowired
    private IRealAuthService realAuthService;

    @RequestMapping("/realAuth")
    public String realAuth(@ModelAttribute("qo") RealAuthQueryObject qo, Model model) {
        model.addAttribute("pageResult", realAuthService.queryPage(qo));
        return "realAuth/list";
    }

    @RequestMapping("/realAuth_audit")
    @ResponseBody
    public AjaxResult audit(Long id ,int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            realAuthService.audit(id,state,remark);
            ajaxResult = new AjaxResult("审核成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult("审核失败");
        }
        return ajaxResult;
    }
}
