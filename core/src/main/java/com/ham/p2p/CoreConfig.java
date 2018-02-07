package com.ham.p2p;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:application-core.properties"),
        @PropertySource("classpath:msg.properties"),
        @PropertySource("classpath:email.properties")
})
@MapperScan("com.ham.p2p.*.mapper")
public class CoreConfig {
}
