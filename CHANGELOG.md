# Changelog

All notable changes to this project will be documented in this file.

This project follows semantic versioning where applicable.

## [0.1.0-SNAPSHOT] - Unreleased

### Added

- Unified SDK configuration model with `OkxConfigLoader`, `OkxConfigBinder`, `OkxHttpClients`, and `OkxObjectMappers`.
- REST client factory `OkxRestClients` and endpoint/live configuration sections.
- Spring Boot auto-configuration (`OkxProperties`, `OkxAutoConfiguration`) binding `okx.*` to `OkxConfig`.
- Example configuration files under `okx-sdk-examples/src/main/resources/`.
- Initialized Maven multi-module project structure.
- Added core, REST, WebSocket, Spring Boot Starter, and examples modules.
- Added UTF-8 editor configuration.
- Added Git ignore rules for IDE files, build outputs, local configuration, and environment files.
- Added Spring Boot example configuration with placeholder credentials.
- Added project planning documents.
- Added Apache License 2.0.
- Added contribution and security documentation.

### Changed

- `OkxConfig` is the single source of truth; accounts use a named map; REST base URL resolves from config.
- Live tests load `okx.*` via `OkxConfigLoader.load()` and remain disabled unless explicitly enabled.
- SDK modules no longer rely on classpath example configuration files for tests.
