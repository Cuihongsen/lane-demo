package com.example.demo2.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Cui
 */
@FeignClient(name = "demo1")
public interface ProviderFeign {
    @GetMapping("/provider")
    Integer provider();
}
