package com.example.gateway.cloud.lb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.reactive.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 泳道的负载均衡实现
 *
 * @author cui
 */
public class LaneLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Log log = LogFactory.getLog(LaneLoadBalancer.class);
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String serviceId;


    public LaneLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        log.info("GrayLoadBalancer init " + serviceId);
    }


    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        final Object context = request.getContext();
        // 从 request 取出 context 我们规定 类型为 HttpHeaders
        if (context instanceof HttpHeaders && this.serviceInstanceListSupplierProvider != null) {
            HttpHeaders headers = (HttpHeaders) context;
            ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
            return ((Flux) supplier.get()).next().map(list -> getInstanceResponse((List<ServiceInstance>) list, headers));
        }
        return null;
    }


    /**
     * 获取需要的实例
     *
     * @param instances 全部的实例列表
     * @param headers   当前请求的header
     * @return 选中的实例
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, HttpHeaders headers) {
        if (instances.isEmpty()) {
            return getServiceInstanceEmptyResponse();
        } else {
            return getServiceInstanceResponseWithLane(instances, headers);
        }
    }

    /**
     * 根据在nacos中配置的权重值，进行分发
     *
     * @param instances 全部的实例列表
     * @return 选中的实例
     */
    private Response<ServiceInstance> getServiceInstanceResponseWithLane(List<ServiceInstance> instances, HttpHeaders headers) {

        // 泳道的实例列表
        final ArrayList<ServiceInstance> laneServiceInstances = new ArrayList<>();
        // 基线的实例列表
        final ArrayList<ServiceInstance> normalServiceInstances = new ArrayList<>();
        // header 带有 laneTag 的流量为泳道流量
        final String currentEnvironment = headers.getFirst("laneTag");

        instances.forEach(serviceInstance -> {
            final Map<String, String> metadata = serviceInstance.getMetadata();
            final String laneName = metadata.getOrDefault("lane", "");
            if (laneName.isEmpty()) {
                normalServiceInstances.add(serviceInstance);
            } else if (currentEnvironment != null && !currentEnvironment.isEmpty() && currentEnvironment.equals(laneName)) {
                laneServiceInstances.add(serviceInstance);
            }
        });

        if (log.isTraceEnabled()) {
            log.trace("laneServiceInstances = " + laneServiceInstances.stream().map(ServiceInstance::getUri).collect(Collectors.toList()));
            log.trace("normalServiceInstances = " + normalServiceInstances.stream().map(ServiceInstance::getUri).collect(Collectors.toList()));
        }

        // 取不到泳道就走基线
        final ArrayList<ServiceInstance> serviceInstances = !laneServiceInstances.isEmpty() ? laneServiceInstances : normalServiceInstances;

        int size = serviceInstances.size();
        Random random = new Random();
        ServiceInstance instance = serviceInstances.get(random.nextInt(size));
        if (log.isTraceEnabled()) {
            log.trace("use instance = " + instance.getUri());
        }
        return new DefaultResponse(instance);
    }

    private Response<ServiceInstance> getServiceInstanceEmptyResponse() {
        log.warn("No servers available for service: " + this.serviceId);
        return new EmptyResponse();
    }
}