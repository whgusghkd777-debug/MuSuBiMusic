package com.mysite.sbb.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.List; // 이 줄이 누락되면 안 됩니다!

public class UserPrincipal implements UserDetails {
    private final SiteUser user;

    public UserPrincipal(SiteUser user) {
        this.user = user;
    }

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    // DB의 role이 ADMIN이라면 -> ROLE_ADMIN으로 만들어서 시큐리티에게 전달
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
}

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public SiteUser getUser() { return user; }
}