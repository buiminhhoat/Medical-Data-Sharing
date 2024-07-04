package com.medicaldatasharing.security.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class JwtResponse {
    private String access_token;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(String access_token, String username, Collection<? extends GrantedAuthority> authorities) {
        this.access_token = access_token;
        this.username = username;
        this.authorities = authorities;
    }
}
