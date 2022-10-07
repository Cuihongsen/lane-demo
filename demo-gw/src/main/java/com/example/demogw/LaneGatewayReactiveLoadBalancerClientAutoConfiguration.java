package com.example.demogw;

import com.example.demogw.cloud.lb.LaneReactiveLoadBalancerClientFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cuihongsen
 */
@Configuration
public class LaneGatewayReactiveLoadBalancerClientAutoConfiguration {
    public LaneGatewayReactiveLoadBalancerClientAutoConfiguration() {
    }


    @Bean
    @ConditionalOnMissingBean({LaneReactiveLoadBalancerClientFilter.class})
    public LaneReactiveLoadBalancerClientFilter grayReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory, LoadBalancerProperties properties) {
        return new LaneReactiveLoadBalancerClientFilter(clientFactory, properties);
    }
}