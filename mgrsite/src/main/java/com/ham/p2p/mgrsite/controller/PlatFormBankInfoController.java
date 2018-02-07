package com.ham.p2p.mgrsite.controller;

import com.ham.p2p.business.domain.PlatFormBankInfo;
import com.ham.p2p.business.query.PlatFormBankInfoQueryObject;
import com.ham.p2p.business.service.IPlatFormBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlatFormBankInfoController {
    @Autowired
    private IPlatFormBankInfoService platFormBankInfoService;

    @RequestMapping("/companyBank_list")
    public String platforBankInfoPage(@ModelAttribute("qo") PlatFormBankInfoQueryObject qo, Model model) {

        model.addAttribute("pageResult", platFormBankInfoService.queryPage(qo));

        return "platformbankinfo/list";
    }

    @RequestMapping("/companyBank_update")
    public String saveOrUpdate(PlatFormBankInfo platFormBankInfo) {
        platFormBankInfoService.saveOrUpdate(platFormBankInfo);
        return "redirect:/companyBank_list";
    }
}
