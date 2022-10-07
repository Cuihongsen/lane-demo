package com.example.demo2;

import com.example.demo2.gray.GrayRuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.example.demo2.feign"})
//@RibbonClients(value = {
//        //指定对comments这个服务开启灰度部署
//        @RibbonClient(value = "demo1", configuration = GrayRuleConfig.class)
//})
public class Demo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo2Application.class, args);
    }

}
