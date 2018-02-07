package com.ham.p2p.website.controller;

import com.ham.p2p.base.domain.UserFile;
import com.ham.p2p.base.mapper.SystemDictionaryItemMapper;
import com.ham.p2p.base.service.ISystemDictionaryItemService;
import com.ham.p2p.base.service.IUserFileService;
import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.base.util.UploadUtil;
import com.ham.p2p.website.annotation.RequiredLogin;
import com.sun.tools.internal.xjc.ModelLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserFileController {
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @Value("${file.path}")
    private String filePath;

    @RequestMapping("/userFile")
    @RequiredLogin
    public String userFile(Model model) {
        List<UserFile> unselectFileTypeList = userFileService.selectFileTypeByCondition(false);
        if (unselectFileTypeList.size() > 0) {
            model.addAttribute("userFiles", unselectFileTypeList);
            model.addAttribute("fileTypes", systemDictionaryItemService.queryByParentSn("userfileType"));
            return "userFiles_commit";
        } else {

        List<UserFile> selectFileTypeList = userFileService.selectFileTypeByCondition(true);
            model.addAttribute("userFiles", selectFileTypeList);
            return "userFiles";
        }
    }

    @RequestMapping("/userFileUpload")
    @ResponseBody
    public String userFileUpload(MultipartFile image) {
        String imgPath = UploadUtil.upload(image, filePath);
        userFileService.apply(imgPath);
        return imgPath;
    }

    @RequestMapping("/userFile_selectType")
    @ResponseBody
    public AjaxResult selectType(Long[] id, Long[] fileType) {
        AjaxResult ajaxResult = null;
        try {
            userFileService.selectType(id, fileType);
            ajaxResult = new AjaxResult("", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }

}
