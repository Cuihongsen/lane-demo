package com.example.demo2.gray;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 灰度部署的负载规则配置类
 * 注意：这个类一定不要被Spring Boot 扫描进入IOC容器中，一旦扫描进入则对全部的服务都将生效
 */

@Component
public class GrayRuleConfig {
    @Bean
    public GrayRule grayRule() {
        return new GrayRule();
    }
}