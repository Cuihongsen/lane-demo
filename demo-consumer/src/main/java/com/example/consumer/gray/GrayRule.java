package com.example.consumer.gray;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GrayRule extends ZoneAvoidanceRule {

    public static final String METADATA_lane = "lane";
    public static final String LANE_PREFER_TAG = "laneTag";

    @Override
    public Server choose(Object key) {
        try {
            //从ThreadLocal中获取灰度标记
            // 我用x-asm-prefer-tag: news-02成功调用到了泳道上的服务
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            final String laneValue = request.getHeader(LANE_PREFER_TAG);
            System.out.println("laneValue = " + laneValue);

            boolean useWorkLoad = !"".equals(laneValue);

            //获取所有可用服务
            List<Server> serverList = this.getLoadBalancer().getReachableServers();
            //灰度发布的服务
            List<Server> grayServerList = new ArrayList<>();
            //正常的服务
            List<Server> normalServerList = new ArrayList<>();
            for (Server server : serverList) {
                NacosServer nacosServer = (NacosServer) server;
                //从nacos中获取元素剧进行匹配
                final Map<String, String> metadata = nacosServer.getMetadata();
                if (metadata.containsKey(METADATA_lane) && metadata.get(METADATA_lane).equals(laneValue)) {
                    grayServerList.add(server);
                } else if (!metadata.containsKey("lane")) {
                    normalServerList.add(server);
                }
            }
            //如果被标记为灰度发布，则调用灰度发布的服务
            if (useWorkLoad && !grayServerList.isEmpty()) {
                System.out.println("grayServerList = " + grayServerList);
                return originChoose(grayServerList, key);
            } else {
                System.out.println("normalServerList = " + normalServerList);
                return originChoose(normalServerList, key);
            }
        } finally {
            //清除灰度标记
//            GrayRequestContextHolder.remove();
        }
    }

    private Server originChoose(List<Server> noMetaServerList, Object key) {
        Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(noMetaServerList, key);
        if (server.isPresent()) {
            return server.get();
        } else {
            return null;
        }
    }
}