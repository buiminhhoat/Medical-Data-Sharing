package com.medicaldatasharing.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medicaldatasharing.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class UserPrinciple implements UserDetails {
    private static final long SERIAL_VERSION_UID = 1L;

    private String id;
    @Getter
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(String id,
                         String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrinciple build(Object obj) {
        List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();

        String userRole = ((User) obj).getRole();
        auth.add(new SimpleGrantedAuthority(userRole));

        return new UserPrinciple(
                ((User) obj).getId(),
                ((User) obj).getEmail(),
                ((User) obj).getPassword(),
                auth);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
