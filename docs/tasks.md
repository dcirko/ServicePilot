# ServicePilot Tasks

## TODO

### Auth

- [x] Implement logout flow

- [x] Display current logged-in user

- [ ] Admin sidebar and service advisor sidebar

- [ ] Interceptor

- [ ] Auth guards

## Known issues

- [ ] Topbar current user does not render immediately after `/me` resolves. It appears only after toggling/collapsing the sidebar, which suggests change detection or state update timing in the topbar/shell.

- [ ] Logout can fail when the access token is expired but the refresh cookie still exists. `/api/auth/logout` is currently `authenticated()` in `SecurityConfig.java`, and `JwtAuthenticationFilter` authenticates only from the access cookie. If the access token is expired, `JwtService` returns unauthorized before `AuthServiceImpl.logout()` can revoke the refresh token or clear cookies. For robust logout, consider making logout `permitAll` while keeping CSRF protection, and skipping it in the JWT filter because the service can revoke the refresh token from the cookie.
