# 工程简介

`demo-spring-cloud-gateway` 基于 Spring Cloud Gateway 构建，示范如何结合自定
义负载均衡策略在多实例（多车道）之间转发流量。项目提供 `/l/**` 路由，
会将请求改写后转发到注册在 Nacos 的下游服务（示例配置指向 `lb://demo1`），
并可通过 `LaneLoadBalancer` 等组件注入灰度决策逻辑。

关键模块：

- `LaneGlobalFilter` 与 `LaneThreadLocalEnvironment`：提取请求头等上下文，供
  负载均衡策略判定；
- `LaneReactiveLoadBalancerClientFilter` / `LaneLoadBalancer`：在 Spring Cloud
  Gateway 中接管默认的负载均衡行为，实现自定义路由；
- `LaneRobinRule`：保留了 Ribbon 版本的示例实现，可用于理解思路。

## 本地启动

1. 根据需要修改 `src/main/resources/bootstrap.yml` 中的 Nacos 地址与路由配置；
2. 使用 `./mvnw spring-boot:run` 启动网关（Windows 使用 `mvnw.cmd`）；
3. 服务默认监听 `9080` 端口，可通过 `http://localhost:9080/l/provider`
   验证转发效果；
4. 若需以打包方式运行，可执行 `./mvnw clean package` 后启动生成的 JAR。

## 延伸阅读

- [Spring Cloud Gateway 官方文档](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- Spring Cloud LoadBalancer 参考：
  <https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#spring-cloud-loadbalancer>
- [Spring Cloud Alibaba Nacos 官方文档](https://nacos.io/zh-cn/docs/concepts.html)
