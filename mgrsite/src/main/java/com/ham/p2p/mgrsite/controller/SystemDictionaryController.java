package com.ham.p2p.mgrsite.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.ham.p2p.base.domain.Systemdictionary;
import com.ham.p2p.base.query.SystemDictionaryQueryObject;
import com.ham.p2p.base.service.ISystemDictionaryService;
import com.ham.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemDictionaryController {

    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    @RequestMapping("/systemDictionary_list")
    public String systemDictionaryPage(@ModelAttribute("qo")SystemDictionaryQueryObject qo, Model model) {
   model.addAttribute("pageResult",systemDictionaryService.queryPage(qo));
        return "systemdic/systemDictionary_list";
    }

    @RequestMapping("/systemDictionary_update")
    @ResponseBody
    public AjaxResult saveOrUpdate(Systemdictionary systemdictionary){
        AjaxResult ajaxResult = null;
        try {
            systemDictionaryService.saveOrUpdate(systemdictionary);
            ajaxResult=new AjaxResult("",true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }
}
