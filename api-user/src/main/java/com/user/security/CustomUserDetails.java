package com.user.security;

import com.storage.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * Admin 에서는 권한 관련해서 생각해야할게 있지만, ( 예를 들어 admin, user 둘다 권한 부여 등 )
     * user에서는 권한은 user 하나면 되기 때문에 "role_user" 로 고정
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> "ROLE_USER");

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getAccount().getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount().getEmail();
    }
}
