package com.ham.p2p.website.controller;

import com.ham.p2p.base.domain.RealAuth;
import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.service.IRealAuthService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.base.util.UploadUtil;
import com.ham.p2p.website.annotation.RequiredLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RealAuthController {
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserinfoService userinfoService;

    @Value("${file.path}")
    private String filePath;

    @RequestMapping("/realAuth")
    @RequiredLogin
    public String realAuthPage(Model model) {
        Userinfo userinfo = userinfoService.getCurrent();
        if (userinfo.getIsRealAuth()) {
            RealAuth realAuth = realAuthService.get(userinfo.getRealAuthId());
            model.addAttribute("realAuth", realAuth);
            model.addAttribute("auditing", false);
            return "realAuth_result";
        } else {
            if (userinfo.getRealAuthId() == null) {
                return "realAuth";
            } else {
                model.addAttribute("auditing", true);
                return "realAuth_result";
            }
        }
    }

    @RequestMapping("/realAuth_save")
    @ResponseBody
    public AjaxResult realAuthSave(RealAuth realAuth) {
        AjaxResult ajaxResult = null;
        try {
            realAuthService.realAuthSave(realAuth);
            ajaxResult = new AjaxResult("提交成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());


        }
        return ajaxResult;
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(MultipartFile image) {
        return   UploadUtil.upload(image,filePath);

    }

}
