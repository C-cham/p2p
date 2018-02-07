package com.ham.p2p.website.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.query.IpLogQueryObject;
import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.service.IIpLogService;
import com.ham.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IpLogController {
    @Autowired
    private IIpLogService ipLogService;

    @RequestMapping("/ipLog")
    public String ipLog(Model model, @ModelAttribute("qo") IpLogQueryObject qo) {
        qo.setUsername(UserContext.getCurrentUser().getUsername());

        PageInfo pageInfo = ipLogService.queryPage(qo);
        model.addAttribute("pageResult", pageInfo);
        return "ipLog_list";
    }
}
