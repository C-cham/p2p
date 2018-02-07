package com.ham.p2p.base.service.impl;

import com.ham.p2p.base.domain.MailVerify;
import com.ham.p2p.base.service.IEmailService;
import com.ham.p2p.base.service.IMailVerifyService;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class EmailServiceImpl implements IEmailService {
    @Value("${email.applicationUrl}")
    private String applicationUrl;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private IMailVerifyService mailVerifyService;
    @Autowired
    private JavaMailSender sender;

    @Override
    public void sendEmail(String email) {
        //生成UUID
        String uuid = UUID.randomUUID().toString();
        //构建邮箱内容
        StringBuilder msg = new StringBuilder(50);
        msg.append("感谢注册p2p平台,这是您的认证邮件,点击<a href='").append(applicationUrl).append("/bindEmail?key=").append(uuid)
                .append("'>这里</a>完成认证,有效期为")
                .append(BidConst.EMAIL_VALID_TIME).append("天,妈的,请尽快认证啊");
//        System.out.println(msg);
        try {
            //发送邮件
            MailVerify mailVerify = new MailVerify();
            mailVerify.setEmail(email);
            mailVerify.setSendTime(new Date());
            mailVerify.setUserId(UserContext.getCurrentUser().getId());
            mailVerify.setUuid(uuid);
            mailVerifyService.save(mailVerify);
            sendEmail(email, msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String email, String context) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("来自远方的一封验证邮件");
        helper.setTo(email);
        helper.setFrom(fromEmail);
        helper.setText(context, true);
        sender.send(message);
    }
}

