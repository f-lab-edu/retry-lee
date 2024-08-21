package com.user.security;

import com.storage.entity.Account;
import com.storage.entity.Admin;
import com.storage.entity.User;
import com.user.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Account account;
    private final UserType userType;
    private final Long id;

    public static CustomUserDetails of(User user) {
        return new CustomUserDetails(user.getAccount(), UserType.USER, user.getUserId());
    }

    public static CustomUserDetails of(Admin admin) {
        return new CustomUserDetails(admin.getAccount(), UserType.ADMIN, admin.getAdminId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (userType == UserType.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }
}
