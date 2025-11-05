# 工程简介

`demo-consumer` 是面向 `demo-provider` 服务的调用端示例，基于 Spring Cloud
OpenFeign 发起远程调用，并集成自定义的灰度路由规则（`GrayRule`）以便在多
个提供者实例之间按标签或请求上下文分配流量。

核心组件：

- `ProviderFeign`：声明式 Feign 客户端，映射 `GET /provider` 调用；
- `GrayRule` / `GrayRuleConfig`：示例性的灰度决策逻辑，可结合请求头、
  用户标识等信息将流量引导至特定实例；
- `ConsumerController`：对外暴露 `GET /consumer` 接口，返回上游提供者的响应。

## 本地启动

1. 调整 `src/main/resources` 下的 Nacos 地址信息，使其能够发现提供者实例。
2. 在项目根目录执行 `./mvnw spring-boot:run`（Windows 可执行 `mvnw.cmd`）。
3. 服务默认监听 `8081` 端口，可通过 `http://localhost:8081/consumer`
   验证整体链路；根据灰度规则不同，返回值可能为 `1` 或 `2`。
4. 若需要以可执行 JAR 方式运行，可执行 `./mvnw clean package` 后运行
   `java -jar target/demo-consumer-0.0.1-SNAPSHOT.jar`。

## 延伸阅读

- [Spring Cloud OpenFeign 官方文档](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)
- [Spring Cloud Alibaba Nacos 官方文档](https://nacos.io/zh-cn/docs/concepts.html)
