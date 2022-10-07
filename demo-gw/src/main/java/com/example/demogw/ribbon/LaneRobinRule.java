package com.example.demogw.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Cui
 */
@Component
public class LaneRobinRule extends RoundRobinRule {

    private final AtomicInteger nextServerCyclicCounter;

    public LaneRobinRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
    }


    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            System.out.println("no load balancer");
            return null;
        }
        Server server = null;
        int count = 0;
        // 如果失败，重试 10 次
        while (count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();

            List<NacosServer> grayServerList = new ArrayList<>();
            //正常的服务
            List<Server> normalServerList = new ArrayList<>();

            String currentEnvironmentVersion = LaneThreadLocalEnvironment.getCurrentEnvironment();

            for (Server serverInfo : reachableServers) {
                NacosServer nacosServer = (NacosServer) serverInfo;
                final Map<String, String> metadata = nacosServer.getMetadata();
                if (metadata.containsKey("lane") && !metadata.get("lane").isEmpty() && metadata.get("lane").equals(currentEnvironmentVersion)) {
                    grayServerList.add(nacosServer);
                } else if (!metadata.containsKey("lane") || metadata.get("lane").isEmpty()) {
                    normalServerList.add(nacosServer);
                }
            }

            final List<? extends Server> servers = grayServerList.isEmpty() ? normalServerList : grayServerList;

            int filterServerCount = servers.size();
            int nextServerIndex = incrementAndGetModulo(filterServerCount);
            server = servers.get(nextServerIndex);

            if (server == null) {
                // 让出线程
                Thread.yield();
                continue;
            }
            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }
            server = null;
        }

        System.out.println("No available alive servers after 10 tries from load balancer: " + lb);
        return null;
    }

    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next)) {
                return next;
            }
        }
    }
}
