# Keycloak 2FA Choose Method
Keycloak Authentication Provider implementation to allow users to choose their preferred method for two-factor authentication.
Available methods:
- SMS
- Authenticator App (Google Authenticator, Free Authenticator).

Compatible with Keycloak version:
- v16.1.0

### How it works
Displays a page where the user can choose their two-step authentication method. The user can choose between SMS and Authenticator App.

The page is only displayed if the `two_factor_auth` attribute is set to user and `two_factor_auth.required` true.

### Configuration
TODO

### Deployment
## Docker
TODO
