package com.ham.p2p.website.controller;

import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.service.IAccountService;
import com.ham.p2p.base.service.IRealAuthService;
import com.ham.p2p.base.service.IUserFileService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.AjaxResult;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.base.util.UserContext;
import com.ham.p2p.business.domain.BidRequest;
import com.ham.p2p.business.service.IBidRequestService;
import com.ham.p2p.business.service.impl.BidRequestServiceImpl;
import com.ham.p2p.website.annotation.RequiredLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
public class BorrowController {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserFileService userFileService;

    @RequestMapping("/borrow")
    public String borrow(Model model) {
        if (UserContext.getCurrentUser() == null) {
            return "redirect:borrowStatic.html";
        }
        model.addAttribute("account", accountService.getCurrent());

        model.addAttribute("userinfo", userinfoService.getCurrent());

        model.addAttribute("creditBorrowScore", BidConst.CREDIT_BORROW_SCORE);

        return "borrow";
    }

    @RequestMapping("/borrowInfo")

    public String borrowInfo(Model model) {
        if (UserContext.getCurrentUser() != null) {
            //是否已经登录,判断是否满足借款条件
            Userinfo userinfo = userinfoService.getCurrent();
            if (bidRequestService.canApplyBorrow(userinfo)) {
                //判断用户是否由借款的流程正在审核

                if (!userinfo.getHasBidRequestProcess()) {
                    //进入申请的页面
                    //最小借款金额(minBidRequestAmount)
                    model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_BIDREQUEST_AMOUNT);
                    //账户信息(account)
                    model.addAttribute("account", accountService.getCurrent());
                    //系统最小投标金额(minBidAmount)
                    model.addAttribute("minBidAmount", BidConst.SMALLEST_BID_AMOUNT);

                    return "borrow_apply";
                } else {
                    //进入结果页面
                    return "borrow_apply_result";
                }
            }
        }
        return "redirect:/borrow";
    }

    @RequestMapping("/borrow_apply")
    @RequiredLogin
    public String apply(BidRequest bidRequest) {
        bidRequestService.apply(bidRequest);
        return "redirect:/borrowInfo";
    }

    @RequestMapping("/borrow_info")
    public String frontBorrowInfoPage(Long id, Model model) {
        BidRequest bidRequest = bidRequestService.selectByPrimaryKey(id);
        if (bidRequest != null) {
            model.addAttribute("bidRequest", bidRequest);
            Userinfo userinfo = userinfoService.selectByPrimaryKey(bidRequest.getCreateUser().getId());
            model.addAttribute("userInfo", userinfo);
            model.addAttribute("realAuth", realAuthService.get(userinfo.getRealAuthId()));
            model.addAttribute("userFiles", userFileService.queryByUserId(bidRequest.getCreateUser().getId()));
//判断是否登录
            if (UserContext.getCurrentUser() == null) {
                model.addAttribute("self", false);

            } else {
                //判断当前登录用户是否借款人
                if (UserContext.getCurrentUser().getId().equals(bidRequest.getCreateUser().getId())) {
                    //本人
                    model.addAttribute("self", true);
                } else {
                    //投资人
                    model.addAttribute("self", false);
                    model.addAttribute("account", accountService.getCurrent());
                }
            }
        }
        return "borrow_info";
    }

    @RequestMapping("/borrow_bid")
    @ResponseBody
    public AjaxResult bid(Long bidRequestId, BigDecimal amount) {
        AjaxResult ajaxResult = null;
        try {
            bidRequestService.bid(bidRequestId, amount);
            ajaxResult = new AjaxResult("投标成功", true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult = new AjaxResult(e.getMessage());
        }

        return ajaxResult;
    }
}
