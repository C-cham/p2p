package com.ham.p2p;

import com.ham.p2p.website.listener.MyRequestContextListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class AppConfig {
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean listenerRegistrationBean = new ServletListenerRegistrationBean();
        listenerRegistrationBean.setListener(requestContextListener());
        return listenerRegistrationBean;
    }
    @Bean
    public MyRequestContextListener requestContextListener(){
        return new MyRequestContextListener();
    }

    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class,args);
    }
}