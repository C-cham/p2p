package com.ham.p2p.mgrsite.controller;

import com.ham.p2p.base.query.UserFileQueryObject;
import com.ham.p2p.base.service.IUserFileService;
import com.ham.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserFileAuthController {
    @Autowired
    private IUserFileService userFileService;

    @RequestMapping("/userFileAuth")
    public String userFileAuthPage(@ModelAttribute("qo") UserFileQueryObject qo, Model model) {
        qo.setSelectFileType(true);
        model.addAttribute("pageResult", userFileService.queryPage(qo));
        return "userFileAuth/list";
    }

    @RequestMapping("/userFile_audit")
    @ResponseBody
    public AjaxResult audit(Long id, int state, int score, String remark) {
        AjaxResult ajaxResult = null;
        try {
            userFileService.audit(id, state, score, remark);
            ajaxResult = new AjaxResult("", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }

}
