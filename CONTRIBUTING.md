# Contributing

Thank you for your interest in contributing to OKX Java SDK.

This project is a community maintained, non-official Java SDK for OKX API.

## Development Principles

- Keep the SDK standard, maintainable, and Maven-friendly.
- Keep public APIs clear and stable.
- Keep modules and packages organized by responsibility.
- Keep Java output compatible with Java 11.
- Use traditional Java syntax and avoid newer syntax such as `record`.

## Code Style

- Java comments and Javadocs should be written in Chinese.
- Logs, exception messages, console output, and test output should be written in English.
- All files must use UTF-8 encoding.
- Public `Service`, `Client`, configuration classes, enums, requests, and responses should include clear Javadocs.
- Enum classes should end with `Enum`.
- Service implementation classes should end with `Impl`.

## Package Guidelines

Use the root package:

```text
io.github.adrianclarkhub.okx
```

Module package roots:

```text
io.github.adrianclarkhub.okx.core
io.github.adrianclarkhub.okx.rest
io.github.adrianclarkhub.okx.websocket
io.github.adrianclarkhub.okx.spring
io.github.adrianclarkhub.okx.examples
```

## Testing Requirements

Every implemented API should include tests.

Recommended test types:

- Unit tests for signing, configuration, enum conversion, parameter conversion, exception classification, and JSON serialization.
- Integration or live tests for real OKX API calls.
- Live tests must be disabled by default and enabled explicitly by local configuration or environment variables.

Do not commit real API credentials or proxy credentials.

## Sensitive Information

Never commit:

- API Key
- Secret Key
- Passphrase
- Proxy username or password
- Local real configuration files
- Personal machine paths

Use placeholders such as:

```text
YOUR_API_KEY
YOUR_SECRET_KEY
YOUR_PASSPHRASE
```

## Commit Message Guidelines

Use clear commit prefixes:

- `feat:` new feature or API
- `fix:` bug fix
- `docs:` documentation update
- `refactor:` refactoring without behavior change
- `test:` test changes
- `build:` build, dependency, or release configuration
- `chore:` maintenance changes

Examples:

```text
feat: add core exception hierarchy
fix: correct REST signing path handling
docs: update Spring Boot configuration example
```

## Pull Request Guidelines

Before submitting a pull request:

- Run Maven validation or tests when applicable.
- Confirm no real credentials are included.
- Confirm new public APIs include Javadocs.
- Confirm new API implementations include tests.
- Update documentation when behavior or usage changes.
