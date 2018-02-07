package com.ham.p2p.mgrsite.controller;

import com.ham.p2p.base.query.VedioAuthQueryObject;
import com.ham.p2p.base.service.ILogininfoService;
import com.ham.p2p.base.service.IVedioAuthSerivce;
import com.ham.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class VedioAuthController {

    @Autowired
    private IVedioAuthSerivce vedioAuthSerivce;

    @Autowired
    private ILogininfoService logininfoService;

    @RequestMapping("/vedioAuth")
    public String vedioAuthPage(@ModelAttribute("qo") VedioAuthQueryObject qo, Model model) {
        model.addAttribute("pageResult", vedioAuthSerivce.queryPage(qo));
        return "vedioAuth/list";
    }

    @RequestMapping("/vedioAuth_audit")
    @ResponseBody
    public AjaxResult audit(Long loginInfoValue, int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            vedioAuthSerivce.audit(loginInfoValue, state, remark);
            ajaxResult = new AjaxResult("审核成功", true);

        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }

        return ajaxResult;
    }

    @RequestMapping("/vedioAuth_autocomplate")
    @ResponseBody
    public List<Map<String, Object>> autoComlate(String keyword) {

        return logininfoService.autoComplate(keyword);
    }
}
