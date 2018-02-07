package com.ham.p2p.base.util;

import javax.servlet.http.HttpServletRequest;

public class MyRequestContextHolder {
    public static ThreadLocal<HttpServletRequest> local = new ThreadLocal<HttpServletRequest>();

    public static void setHttpRequest(HttpServletRequest request) {
        local.set(request);
    }

    public static HttpServletRequest getHttpRequest() {
        return local.get();
    }
}
