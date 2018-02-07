package com.ham.p2p.website.controller;

import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.business.domain.RechargeOffline;
import com.ham.p2p.business.service.IPlatFormBankInfoService;
import com.ham.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RechargeOffineController {
    @Autowired
    private IPlatFormBankInfoService platFormBankInfoService;
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;

    @RequestMapping("/recharge")
    public String rechargeOffinePage(Model model) {
        model.addAttribute("banks", platFormBankInfoService.selectAll());
        return "recharge";
    }

    @RequestMapping("/recharge_save")
    @ResponseBody
    public AjaxResult rechargeSave(RechargeOffline rechargeOffline) {
        AjaxResult ajaxResult = null;
        try {
            rechargeOfflineService.apply(rechargeOffline);
            ajaxResult = new AjaxResult("充值成功,等待审核", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }

        return ajaxResult;
    }
}
