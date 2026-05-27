# OKX Java SDK

Community maintained Java SDK for OKX API.

> This project is not an official OKX project and is not affiliated with, endorsed by, or sponsored by OKX.

## Project Status

This project is currently in the early initialization and design stage.

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
