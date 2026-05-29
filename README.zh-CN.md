# OKX Java SDK

面向 OKX API 的社区维护版 Java SDK。

> 本项目不是 OKX 官方项目，也不隶属于 OKX、未获得 OKX 官方背书或赞助。

## 项目状态

本项目目前处于早期开发阶段，已完成基础配置、签名、REST 底层客户端、WebSocket 会话底座、Spring Boot 自动配置以及少量公共 API。

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

## 快速开始

### 普通 Java REST

```java
OkxConfig config = OkxConfigLoader.load();
StatusClient statusClient = new StatusClient(OkxRestClients.create(config));

List<StatusResponse> statusList = statusClient.getStatus();
```

### 普通 Java WebSocket

```java
OkxConfig config = OkxConfigLoader.load();
StatusChannelClient statusChannelClient = new StatusChannelClient();

OkxWebSocketSession session = new OkxWebSocketClient(config).publicSession(new OkxWebSocketListener() {
    @Override
    public void onText(OkxWebSocketConnection connection, String text) {
        System.out.println(text);
    }
});

session.start();
session.registerAndSend(statusChannelClient.subscribeJson("status-example"));
```

### Spring Boot

引入 `okx-spring-boot-starter` 后，在应用配置中声明 `okx.*`：

```yaml
okx:
  environment: production
  endpoints:
    rest-base-url: https://www.okx.com
    ws-public-url: wss://ws.okx.com:8443/ws/v5/public
  http:
    connect-timeout-millis: 10000
    read-timeout-millis: 30000
    write-timeout-millis: 30000
    proxy:
      enabled: false
      host: 127.0.0.1
      port: 7897
  websocket:
    heartbeat-interval-millis: 25000
    reconnect-delay-millis: 5000
    max-reconnect-attempts: 3
```

Starter 会自动注册：

- `OkxConfig`
- `OkxRestClient`
- `OkxWebSocketClient`
- `StatusClient`
- `SupportClient`

### 代理

代理只在显式启用时生效：

```properties
okx.http.proxy.enabled=true
okx.http.proxy.host=127.0.0.1
okx.http.proxy.port=7897
```

REST 和 WebSocket 都复用同一套代理配置。代理启用后如果 `host` 为空或 `port` 不合法，SDK 会在创建客户端时直接抛出配置异常。

## 当前覆盖范围

当前已实现的主要能力：

- 01 基础协议能力：配置、签名、时间戳、REST 鉴权、WebSocket 登录、代理、REST 重试、WebSocket session、心跳、重连、订阅恢复、错误事件转换。
- 12 错误码/异常体系：错误码目录、异常分类、REST 错误转换。
- 13 公告 Support：公告列表和公告类型接口。
- 14 系统状态 Status：REST 状态接口和 WebSocket status 频道基础能力。


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

## 配置规范

SDK 对外统一使用 `OkxConfig` 作为唯一配置模型。

普通 Java 项目和 Spring Boot 项目不需要同时维护多个真实配置文件。实际业务项目请选择一种方式：

- **代码配置**：业务项目创建 `OkxConfig`，设置 API Key、Secret Key、Passphrase、环境、代理和超时等，然后传给 SDK Client。
- **显式外部文件**：业务项目通过 `-Dokx.config.file=/path/to/okx.properties` 或 `OKX_CONFIG_FILE=/path/to/okx.properties` 指定自己的配置文件。
- **应用 classpath 文件**：普通 Java 项目可以把 `okx.properties` 放在自己应用的 `src/main/resources`，并显式调用 `OkxConfigLoader.loadFromClasspath("okx.properties")`。
- **环境变量**：也可以只使用 `OKX_API_KEY`、`OKX_SECRET_KEY`、`OKX_PASSPHRASE` 等环境变量。
- **Spring Boot**：在业务应用自己的 `application.yml` 或 `application-local.yml` 中配置 `okx.*`。

`OkxConfigLoader.load()` 不会隐式读取 SDK 源码仓库根目录或 SDK 模块 `src/main/resources` 下的用户配置文件，只会合并显式指定的外部文件和环境变量。

本仓库中的 SDK 模块不应在 `src/main/resources` 放置用户配置或示例配置，避免配置文件进入发布产物。

示例配置仅放在 `okx-sdk-examples/src/main/resources/`：

- `okx.properties.example`
- `application-okx.yml.example`

本地真实配置必须被 `.gitignore` 忽略，不能提交。

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
