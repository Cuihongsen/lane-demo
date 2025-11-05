# 工程简介

`demo-provider` 是一个最简单的 Spring Boot 示例服务，暴露 `GET /provider`
接口并返回固定数值 `1`。应用默认会以 `spring.application.name` 指定的名称注
册到 Nacos（示例环境通常设置为 `demo1`），供其它服务（例如 `demo-consumer`
或 Spring Cloud Gateway）进行服务发现和负载均衡访问。

核心特性：

- 使用 Spring Cloud Alibaba Nacos 作为配置中心与服务注册中心；
- 通过 `NacosConfigConfiguration` 与 `NacosDiscoveryConfiguration`
  快速启用配置与注册能力；
- 提供简单、可验证的返回值，便于配合灰度或分流方案演示。

## 本地启动

1. 确保可以访问示例中的 Nacos 实例
   `mse-16f4a4b0-p.nacos-ans.mse.aliyuncs.com:8848`，或修改
   `src/main/resources` 下的配置以指向自己的 Nacos 服务。
2. 在项目根目录执行 `./mvnw spring-boot:run`（Windows 用户可使用
   `mvnw.cmd`）。
3. 启动完成后，访问 `http://localhost:8080/provider` 可获得示例返回值。
4. 如需打包，可运行 `./mvnw clean package` 生成可执行 JAR。
5. 若调用方按照默认示例查找 `demo1` 服务，请在 Nacos 配置中心将
   `spring.application.name` 调整为 `demo1`，或修改调用方的服务名。

## 延伸阅读

- [Spring Cloud Alibaba Nacos 官方文档](https://nacos.io/zh-cn/docs/concepts.html)
- [Spring Boot 参考文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
