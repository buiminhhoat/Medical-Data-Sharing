package com.medicaldatasharing.security.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class JwtResponse {
    private String accessToken;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(String accessToken, String username, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken = accessToken;
        this.username = username;
        this.authorities = authorities;
    }
}
