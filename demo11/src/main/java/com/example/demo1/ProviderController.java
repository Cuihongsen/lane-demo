package com.example.demo1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cui
 */
@RestController
public class ProviderController {
    @GetMapping("/provider")
    public Integer provider() {
        return 2;
    }
}
