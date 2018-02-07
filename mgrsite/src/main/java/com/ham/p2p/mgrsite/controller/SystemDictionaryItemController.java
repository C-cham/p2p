package com.ham.p2p.mgrsite.controller;

import com.ham.p2p.base.domain.SystemDictionaryItem;
import com.ham.p2p.base.domain.Systemdictionary;
import com.ham.p2p.base.query.SystemDictionaryItemQueryObject;
import com.ham.p2p.base.service.ISystemDictionaryItemService;
import com.ham.p2p.base.service.ISystemDictionaryService;
import com.ham.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemDictionaryItemController {
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    @RequestMapping("/systemDictionaryItem_list")
    public String SystemDictionaryItem(@ModelAttribute("qo") SystemDictionaryItemQueryObject qo, Model model) {

        model.addAttribute("systemDictionaryGroups", systemDictionaryService.selectAll());
        model.addAttribute("pageResult", systemDictionaryItemService.queryPage(qo));
        return "systemdic/systemDictionaryItem_list";
    }
    @RequestMapping("/systemDictionaryItem_update")
    @ResponseBody
    public AjaxResult saveOrUpdate(SystemDictionaryItem systemDictionaryItem){
        AjaxResult ajaxResult = null;
        try {
            /*if(systemDictionaryItem.getId()==null){
            ajaxResult=new AjaxResult("请选择数据字典分类");
                return ajaxResult;
            }*/
            systemDictionaryItemService.saveOrUpdate(systemDictionaryItem);
            ajaxResult=new AjaxResult("保存成功",true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }
}
