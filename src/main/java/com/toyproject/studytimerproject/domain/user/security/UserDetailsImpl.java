package com.toyproject.studytimerproject.domain.user.security;

import com.toyproject.studytimerproject.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUsername(){
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // role(authorities 사용)
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        UserRole role = user.getRole();
//        String authority = role.getAuthority();
//
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(simpleGrantedAuthority);
//
//        return authorities;
//    }

    // role(authorities 미사용)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;
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