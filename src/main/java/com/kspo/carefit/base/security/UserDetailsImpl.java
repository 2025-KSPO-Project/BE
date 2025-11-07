package com.kspo.carefit.base.security;


import com.kspo.carefit.damain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                user::getRole
        );
    }

    @Override
    public String getPassword() {
        return "No Password in this Project";
        // OAuth2 소셜로그인에서 사용하지 않는 유저 필드
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
