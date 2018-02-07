package com.ham.p2p.mgrsite.controller;

import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.service.IRealAuthService;
import com.ham.p2p.base.service.IUserFileService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.business.domain.BidRequest;
import com.ham.p2p.business.query.BidRequestQueryObject;
import com.ham.p2p.business.service.IBidRequestAuditHistoryService;
import com.ham.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;

@Controller
public class BorrowController {
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IUserFileService userFileService;

    @RequestMapping("bidrequest_publishaudit_list")
    public String publishAuditPage(@ModelAttribute("qo") BidRequestQueryObject qo, Model model) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidrequest/publish_audit";
    }

    @RequestMapping("/bidrequest_publishaudit")
    @ResponseBody
    public AjaxResult publishAudit(Long id, int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.publishAudit(id, state, remark);
            ajaxResult = new AjaxResult("审核成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }

    @RequestMapping("/borrow_info")
    public String borrowInfoPage(Long id, Model model) {
        BidRequest bidRequest = bidRequestService.selectByPrimaryKey(id);
        if (bidRequest != null) {
            model.addAttribute("bidRequest", bidRequest);
            Userinfo userinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
            model.addAttribute("userInfo", userinfo);
            model.addAttribute("realAuth", realAuthService.get(userinfo.getRealAuthId()));
            model.addAttribute("audits", bidRequestAuditHistoryService.queryByBidRequestId(bidRequest.getId()));
            model.addAttribute("userFiles", userFileService.queryByUserId(bidRequest.getCreateUser().getId()));
        }
        return "bidrequest/borrow_info";
    }

    @RequestMapping("/bidrequest_audit1_list")
    public String audit1(@ModelAttribute("qo") BidRequestQueryObject qo, Model model) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidrequest/audit1";
    }

    @RequestMapping("/bidrequest_audit1")
    @ResponseBody
    public AjaxResult audit1(Long id, int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.audit(id, state, remark);
            ajaxResult = new AjaxResult("满标一审成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }

    @RequestMapping("/bidrequest_audit2_list")
    public String audit2(@ModelAttribute("qo") BidRequestQueryObject qo, Model model) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidrequest/audit2";
    }

    @RequestMapping("/bidrequest_audit2")
    @ResponseBody
    public AjaxResult audit2(Long id, int state, String remark) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.audit2(id, state, remark);
            ajaxResult = new AjaxResult("满标二审成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }
        return ajaxResult;
    }
}
