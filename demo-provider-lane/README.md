# 工程简介

`demo-provider-lane` 与 `demo-provider` 保持相同的接口契约，同样提供
`GET /provider` 接口，但返回值为 `2`，用于在演示环境中模拟“车道”或
灰度发布场景下的不同实例行为。服务会以配置指定的名称注册到 Nacos（通常
与主实例使用同一服务名），由上游按照灰度规则进行流量分配。

核心特性：

- 保持与主提供者一致的 API，便于 A/B 或多车道验证；
- 使用 `NacosConfigConfiguration`、`NacosDiscoveryConfiguration` 快速接入
  Nacos 配置与注册能力；
- 可结合 `nginx/lane.lua` 与 Spring Cloud Gateway 的定制负载均衡策略，演
  示不同用户命中的实例差异。

## 本地启动

1. 确认 Nacos 服务地址（示例配置指向
   `mse-16f4a4b0-p.nacos-ans.mse.aliyuncs.com:8848`），如需本地化请修改
   `src/main/resources` 下的配置。
2. 运行 `./mvnw spring-boot:run` 启动服务，或使用 `./mvnw clean package`
   进行打包。
3. 启动后访问 `http://localhost:8080/provider` 可看到返回值 `2`。
4. 配合 `demo-consumer` 或 `demo-spring-cloud-gateway` 时，可通过请求头、
   查询参数等方式由自定义规则路由到该实例。

## 延伸阅读

- [Spring Cloud Alibaba Nacos 官方文档](https://nacos.io/zh-cn/docs/concepts.html)
- [多活与灰度发布最佳实践（阿里云 MSE）](https://help.aliyun.com/document_detail/162408.html)
