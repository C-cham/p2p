package com.ham.p2p.website.controller;

import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.business.domain.PaymentSchedule;
import com.ham.p2p.business.query.PaymentScheduleQueryObject;
import com.ham.p2p.business.service.IPaymentScheduleDetailService;
import com.ham.p2p.business.service.IPaymentScheduleService;
import com.ham.p2p.website.annotation.RequiredLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReturnMoneyController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;

    @RequestMapping("/borrowBidReturn_list")
    public String returnMoneyPage(@ModelAttribute("qo") PaymentScheduleQueryObject qo, Model model) {
        model.addAttribute("account", accountService.getCurrent());
        model.addAttribute("pageResult", paymentScheduleService.queryPage(qo));

        return "returnmoney_list";
    }

    @RequestMapping("/returnMoney")
    @ResponseBody
    @RequiredLogin
    public AjaxResult returnMoney(Long id) {
        AjaxResult ajaxResult = null;
        try {
            paymentScheduleService.returnMoney(id);
            ajaxResult = new AjaxResult("还款成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }

        return ajaxResult;
    }
}
