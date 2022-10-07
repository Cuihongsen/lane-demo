package com.example.demo2;

import com.example.demo2.feign.ProviderFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Cui
 */
@RestController
public class ConsumerController {

    @Resource
    private ProviderFeign providerFeign;

    @GetMapping("/consumer")
    public Integer consumer() {
        return providerFeign.provider();
    }
}
