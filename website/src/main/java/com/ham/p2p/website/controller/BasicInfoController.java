package com.ham.p2p.website.controller;

import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.service.IRealAuthService;
import com.ham.p2p.base.service.ISystemDictionaryItemService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.website.annotation.RequiredLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicInfoController {
    @Autowired
    private IUserinfoService userinfoService;

    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
   @Autowired
    private IRealAuthService realAuthService;

    @RequiredLogin
    @RequestMapping("/basicInfo")
    public String basicInfoPage(Model model) {
        model.addAttribute("userinfo", userinfoService.getCurrent());

        model.addAttribute("educationBackgrounds", systemDictionaryItemService.queryByParentSn("educationBackground"));
        model.addAttribute("incomeGrades", systemDictionaryItemService.queryByParentSn("incomeGrade"));
        model.addAttribute("marriages", systemDictionaryItemService.queryByParentSn("marriage"));
        model.addAttribute("kidCounts", systemDictionaryItemService.queryByParentSn("kidCount"));
        model.addAttribute("houseConditions", systemDictionaryItemService.queryByParentSn("houseCondition"));
        return "userInfo";
    }

    @RequestMapping("basicInfo_save")
    @ResponseBody
    public AjaxResult basicInfoSave(Userinfo userinfo) {
        AjaxResult ajaxResult = null;
        try {
            userinfoService.basicInfoSave(userinfo);
            ajaxResult = new AjaxResult("保存成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }
}
