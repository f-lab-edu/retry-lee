package com.user.security;

import com.storage.entity.Admin;
import com.storage.entity.User;
import com.storage.repository.AdminRepository;
import com.storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return CustomUserDetails.of(user);
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return CustomUserDetails.of(admin);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    public String findEmail(String userType, Long id){
        // user, admin Id 로 account table 과 조인하여 email 찾아오기
        if(userType.equals("USER")){
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                return user.getAccount().getEmail();
            }
        }

        if(userType.equals("ADMIN")){
            return adminRepository.findEmailById(id).orElse(null);
        }

        return "";
    }
}
