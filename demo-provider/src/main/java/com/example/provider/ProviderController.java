package com.example.provider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cui
 */
@RestController
public class ProviderController {
    @GetMapping("/provider")
    public Integer provider() {
        return 1;
    }
}
