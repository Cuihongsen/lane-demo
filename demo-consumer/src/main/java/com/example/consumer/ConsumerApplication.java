package com.example.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author cui
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.example.consumer.feign"})
//@RibbonClients(value = {
//        //指定对comments这个服务开启灰度部署
//        @RibbonClient(value = "demo1", configuration = GrayRuleConfig.class)
//})
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
