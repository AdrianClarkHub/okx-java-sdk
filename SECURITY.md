# Security Policy

## Supported Versions

This project is currently in early development and has not published a stable release yet.

Security support policy will be updated before the first stable release.

## Reporting a Vulnerability

If you discover a security issue, please do not publish it publicly before it is reviewed.

You may report it through GitHub security advisories or by opening a private communication channel with the maintainer when available.

Please include:

- A clear description of the issue.
- Affected module or API.
- Steps to reproduce if possible.
- Potential impact.
- Suggested fix if available.

## Credential Safety

Never commit real OKX credentials.

Do not commit:

- API Key
- Secret Key
- Passphrase
- Proxy username or password
- Local real configuration files
- Personal environment files

If a credential is accidentally committed:

1. Revoke or delete the leaked credential immediately in OKX.
2. Generate a new credential.
3. Remove the credential from Git history if necessary.
4. Review logs and recent account activity.

## Logging Safety

The SDK must not log sensitive information in plain text.

Sensitive fields include:

- API Key
- Secret Key
- Passphrase
- Signature
- Proxy password

## Dependency Security

Dependencies should be reviewed before upgrades.

Security-related dependency updates should be prioritized and tested carefully.
