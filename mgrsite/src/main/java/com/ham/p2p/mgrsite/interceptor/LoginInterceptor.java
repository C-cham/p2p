package com.ham.p2p.mgrsite.interceptor;

import com.ham.p2p.base.util.UserContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


            if (UserContext.getCurrentUser() == null) {
                response.sendRedirect("/login.html");
                return false;
            }
        return true;
    }
}
