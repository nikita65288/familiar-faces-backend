package com.github.nikita65288.security;

import com.github.nikita65288.entity.UserCredential;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record SocialNetworkUserDetails(UserCredential userCredential) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
        //return Collections.singletonList(new SimpleGrantedAuthority(userCredential.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return this.userCredential.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userCredential.getUsername();
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
