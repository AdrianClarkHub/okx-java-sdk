# OKX Java SDK

Community maintained Java SDK for OKX API.

> This project is not an official OKX project and is not affiliated with, endorsed by, or sponsored by OKX.

## Project Status

This project is currently in early development. Core configuration, signing, REST base client, WebSocket session support, Spring Boot auto-configuration, and a small set of public APIs are already implemented.

The goal is to provide a standard, maintainable, Maven-friendly Java SDK for OKX REST and WebSocket APIs.

## Goals

- Provide a community maintained Java SDK for OKX API.
- Support plain Java projects.
- Support Spring Boot projects through a dedicated starter module.
- Provide clear Maven coordinates for users.
- Keep package structure, naming, configuration, exceptions, and tests consistent.
- Use Java 11-compatible output while allowing development with JDK 17.

## Modules

```text
okx-java-sdk
├── okx-sdk-core
├── okx-sdk-rest
├── okx-sdk-websocket
├── okx-spring-boot-starter
└── okx-sdk-examples
```

### okx-sdk-core

Core configuration, authentication, signing, HTTP base support, exceptions, common enums, and shared response models.

### okx-sdk-rest

REST API clients, services, requests, responses, and domain models.

### okx-sdk-websocket

WebSocket connection management, subscriptions, heartbeats, reconnect logic, and message handling.

### okx-spring-boot-starter

Spring Boot auto-configuration, configuration properties, and SDK Bean registration.

### okx-sdk-examples

Plain Java, Spring Boot, REST, and WebSocket examples.

## Maven Coordinates

Planned groupId:

```xml
<groupId>io.github.adrianclarkhub</groupId>
```

REST module example:

```xml
<dependency>
    <groupId>io.github.adrianclarkhub</groupId>
    <artifactId>okx-sdk-rest</artifactId>
    <version>${version}</version>
</dependency>
```

Spring Boot users may use:

```xml
<dependency>
    <groupId>io.github.adrianclarkhub</groupId>
    <artifactId>okx-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

Actual release coordinates will be finalized before the first public Maven release.

## Quick Start

### Plain Java REST

```java
OkxConfig config = OkxConfigLoader.load();
StatusClient statusClient = new StatusClient(OkxRestClients.create(config));

List<StatusResponse> statusList = statusClient.getStatus();
```

### Plain Java WebSocket

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

After adding `okx-spring-boot-starter`, configure `okx.*` in your application:

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

The starter registers:

- `OkxConfig`
- `OkxRestClient`
- `OkxWebSocketClient`
- `StatusClient`
- `SupportClient`

### Proxy

Proxy is used only when explicitly enabled:

```properties
okx.http.proxy.enabled=true
okx.http.proxy.host=127.0.0.1
okx.http.proxy.port=7897
```

REST and WebSocket clients share the same proxy configuration. If proxy is enabled with an empty `host` or invalid `port`, the SDK fails fast with a configuration exception.

## Current Coverage

Implemented core areas:

- 01 foundation protocol: configuration, signing, timestamps, REST authentication, WebSocket login, proxy, REST retry, WebSocket session, heartbeat, reconnect, subscription replay, and error event conversion.
- 12 error code and exception model: error catalog, exception classification, REST error conversion.
- 13 support announcements: announcement list and announcement type endpoints.
- 14 system status: REST status endpoint and WebSocket status channel base support.


## Java Version

- Development JDK: JDK 17 is recommended.
- Minimum compatible runtime target: Java 11.
- Maven compiler release: 11.

## Configuration Safety

Never commit real credentials.

Do not commit:

- API Key
- Secret Key
- Passphrase
- Proxy username or password
- Local test configuration
- Personal machine paths

Use example placeholders such as:

```text
YOUR_API_KEY
YOUR_SECRET_KEY
YOUR_PASSPHRASE
```

## Configuration Convention

The SDK exposes `OkxConfig` as the single configuration model.

Plain Java projects and Spring Boot projects do not need to maintain multiple real configuration files at the same time. Choose one approach for an application:

- **Plain Java**: use one local `okx.properties`, or pass one external file with `-Dokx.config.file=/path/to/okx.properties`.
- **Spring Boot**: configure `okx.*` in the application's own `application.yml` or `application-local.yml`.
- **Environment variables**: use `OKX_API_KEY`, `OKX_SECRET_KEY`, `OKX_PASSPHRASE`, and related variables.

SDK modules in this repository should not place user configuration or example configuration under `src/main/resources`, so configuration files do not enter released SDK artifacts.

Example configuration files are kept only under `okx-sdk-examples/src/main/resources/`:

- `okx.properties.example`
- `application-okx.yml.example`

Real local configuration must be ignored by `.gitignore` and must not be committed.

## Documentation Language

- User-facing documentation supports English and Chinese.
- Java comments and Javadocs are written in Chinese.
- Runtime logs, exception messages, console output, and test output should be written in English.

## License

This project is licensed under the Apache License 2.0.

See [LICENSE](LICENSE) for details.

## Disclaimer

OKX is a trademark of its respective owner.

This SDK is a community/non-official SDK and is not an official product of OKX.
