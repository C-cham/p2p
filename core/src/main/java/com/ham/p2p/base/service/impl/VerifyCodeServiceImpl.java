package com.ham.p2p.base.service.impl;

import com.ham.p2p.base.service.IVerifyCodeService;
import com.ham.p2p.base.util.BidConst;
import com.ham.p2p.base.util.DateUtil;
import com.ham.p2p.base.util.UserContext;
import com.ham.p2p.base.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class VerifyCodeServiceImpl implements IVerifyCodeService {
    @Value("${msg.messageHost}")
    private String messageHost;
    @Value("${msg.username}")
    private String username;
    @Value("${msg.password}")
    private String password;
    @Value("${msg.apikey}")
    private String apikey;


    @Override
    public void sendVerifyCode(String phoneNumber) {
        VerifyCodeVo vo = UserContext.getVerifyCodeVo();
        if (vo == null || DateUtil.getBetweenTime(vo.getSendTime(), new Date()) > BidConst.MESSAGE_INTERVAL_TIME) {


            String verifyCode = UUID.randomUUID().toString().substring(0, 4);
            StringBuilder msg = new StringBuilder(50);
            msg.append("这是您的验证码:").append(verifyCode).append(",有效期为").append(BidConst.MESSAGE_VALID_TIME).append("分钟内,请尽快使用");
            try {
                sendRealMessage(phoneNumber, msg.toString());
                vo = new VerifyCodeVo();
                vo.setPhoneNumber(phoneNumber);
                vo.setSendTime(new Date());
                vo.setVerifyCode(verifyCode);
                UserContext.setVerifyCodeVo(vo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("操作频繁,请稍后再试");
        }


    }

    //发送真的短信
    public void sendRealMessage(String phoneNumber, String content) throws Exception {
        //复制一个地址
        URL url = new URL("http://utf8.api.smschinese.cn/");
        //打开浏览器,在地址栏输入地址
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求的方式
        conn.setRequestMethod("POST");
        //是否要输出内容
        conn.setDoOutput(true);
        //给地址栏添加参数信息
        StringBuilder param = new StringBuilder(50);
        param.append("Uid=").append("cham")
                .append("&key=").append("5c01c032d090204b32cd")
                .append("&smsMob=").append(phoneNumber)
                .append("&smsText=").append(content);
        //输入数据
        conn.getOutputStream().write(param.toString().getBytes("utf-8"));
        //按下回车键
        conn.connect();
        //获取到服务器相应的内容
        String responseStr = StreamUtils.copyToString(conn.getInputStream(), Charset.forName("utf-8"));
        int responseCode = Integer.parseInt(responseStr);
        if (responseCode < 0) {
            throw new RuntimeException("验证码发送异常,请联系管理员!!!!!");
        }
    }

    public void sendMessage(String phoneNumber, String content) throws Exception {
        //复制一个地址
        URL url = new URL(messageHost);
        //打开浏览器,在地址栏输入地址
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求的方式
        conn.setRequestMethod("POST");
        //是否要输出内容
        conn.setDoOutput(true);
        //给地址栏添加参数信息
        StringBuilder param = new StringBuilder(50);
        param.append("username=").append(username)
                .append("&password=").append(password)
                .append("&apikey=").append(apikey)
                .append("&phoneNumber=").append(phoneNumber)
                .append("&content=").append(content);
        //输入数据
        conn.getOutputStream().write(param.toString().getBytes("utf-8"));
        //按下回车键
        conn.connect();
        //获取到服务器相应的内容
        String responseStr = StreamUtils.copyToString(conn.getInputStream(), Charset.forName("utf-8"));
        if (!"success".equals(responseStr)) {
            throw new RuntimeException("验证码发送异常,请联系管理员");
        }
    }

    @Override
    public boolean validate(String phoneNumber, String verifyCode) {
        VerifyCodeVo verifyCodeVo = UserContext.getVerifyCodeVo();
        if (verifyCodeVo != null &&
                verifyCodeVo.getVerifyCode().equalsIgnoreCase(verifyCode) &&
                verifyCodeVo.getPhoneNumber().equals(phoneNumber) &&
                DateUtil.getBetweenTime(verifyCodeVo.getSendTime(), new Date()) <= BidConst.MESSAGE_VALID_TIME * 60) {
            return true;
        }

        return false;
    }
}
