package com.doan.nhansu.admin.security;

import com.doan.nhansu.admin.dto.UserResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserAuthentication extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 530L;
    private final UserResponse principal;
    private String credentials;

    public UserAuthentication(String credentials) {
        super(null);
        this.principal = null;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public UserAuthentication(UserResponse principal, String credentials) {
        super(principal.getGrantedAuthority());
        this.principal = principal;
        this.credentials = credentials;
        this.principal.setTicket(this.credentials);
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
