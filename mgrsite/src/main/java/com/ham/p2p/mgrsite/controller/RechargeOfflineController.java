package com.ham.p2p.mgrsite.controller;

import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.business.domain.RechargeOffline;
import com.ham.p2p.business.query.RechargeOfflineQueryObject;
import com.ham.p2p.business.service.IPlatFormBankInfoService;
import com.ham.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RechargeOfflineController {
    @Autowired
    private IPlatFormBankInfoService platFormBankInfoService;
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;

    @RequestMapping("/rechargeOffline")
    public String rechargeOfflinePage(@ModelAttribute("qo") RechargeOfflineQueryObject qo, Model model) {
        model.addAttribute("banks", platFormBankInfoService.selectAll());
        model.addAttribute("pageResult", rechargeOfflineService.queryPage(qo));
        return "rechargeoffline/list";
    }

    @RequestMapping("/rechargeOffline_audit")
    @ResponseBody
    public AjaxResult audit(Long id, int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            rechargeOfflineService.audit(id,state,remark);
            ajaxResult = new AjaxResult("", true);

        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }
}
