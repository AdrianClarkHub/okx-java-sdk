# OKX Java SDK

面向 OKX API 的社区维护版 Java SDK。

> 本项目不是 OKX 官方项目，也不隶属于 OKX、未获得 OKX 官方背书或赞助。

## 项目状态

本项目目前处于初始化和设计阶段。

目标是提供一个标准、可维护、适合 Maven 引入的 OKX Java SDK，覆盖 OKX REST API 和 WebSocket API。

## 项目目标

- 提供社区维护的 OKX Java SDK。
- 支持普通 Java 项目使用。
- 通过独立 Starter 模块支持 Spring Boot 项目。
- 提供清晰的 Maven 坐标。
- 保持模块、包名、配置、异常、测试等规范统一。
- 使用 JDK 17 开发，同时保证产物兼容 Java 11。

## 模块结构

```text
okx-java-sdk
├── okx-sdk-core
├── okx-sdk-rest
├── okx-sdk-websocket
├── okx-spring-boot-starter
└── okx-sdk-examples
```

### okx-sdk-core

核心配置、鉴权、签名、HTTP 基础能力、异常、通用枚举和通用响应结构。

### okx-sdk-rest

REST API Client、Service、请求对象、响应对象和业务模型。

### okx-sdk-websocket

WebSocket 连接管理、订阅、心跳、重连和消息处理。

### okx-spring-boot-starter

Spring Boot 自动配置、配置绑定和 SDK Bean 注册。

### okx-sdk-examples

普通 Java、Spring Boot、REST 和 WebSocket 使用示例。

## Maven 坐标

计划使用统一 groupId：

```xml
<groupId>io.github.adrianclarkhub</groupId>
```

REST 模块示例：

```xml
<dependency>
    <groupId>io.github.adrianclarkhub</groupId>
    <artifactId>okx-sdk-rest</artifactId>
    <version>${version}</version>
</dependency>
```

Spring Boot 用户可优先使用：

```xml
<dependency>
    <groupId>io.github.adrianclarkhub</groupId>
    <artifactId>okx-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

实际发布坐标会在首次公开发布 Maven 包前最终确认。

## Java 版本

- 推荐开发 JDK：JDK 17。
- 最低兼容运行版本：Java 11。
- Maven compiler release：11。

## 配置安全

不要提交真实密钥。

禁止提交：

- API Key
- Secret Key
- Passphrase
- 代理用户名或密码
- 本地真实测试配置
- 个人机器路径

示例配置请使用占位符：

```text
YOUR_API_KEY
YOUR_SECRET_KEY
YOUR_PASSPHRASE
```

## 文档和语言规范

- 面向用户阅读的文档支持中文和英文。
- Java 注释和 Javadoc 使用中文。
- 日志、异常消息、控制台输出和测试输出使用英文。

## 许可证

本项目使用 Apache License 2.0。

详见 [LICENSE](LICENSE)。

## 免责声明

OKX 是其对应所有者的商标。

本 SDK 是社区维护的非官方 SDK，不是 OKX 官方产品。
