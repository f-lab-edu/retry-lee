package com.user.security.filter;

import com.user.exception.CustomException;
import com.user.exception.type.ErrorCode;
import com.user.security.CustomUserDetailService;
import com.user.security.CustomUserDetails;
import com.user.utils.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        if (StringUtils.hasText(token)) {
            try {
                Long id = jwtTokenProvider.getClaim(token, "id", Long.class);
                String userType = jwtTokenProvider.getClaim(token, "userType", String.class);
                String email = customUserDetailService.findEmail(userType, id);
                CustomUserDetails userDetails = (CustomUserDetails) customUserDetailService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                log.error("Error during authentication: {} ", e.getMessage());
                throw new CustomException(ErrorCode.ERROR_BE1005);
            }
        }

        filterChain.doFilter(request, response);
    }
}
