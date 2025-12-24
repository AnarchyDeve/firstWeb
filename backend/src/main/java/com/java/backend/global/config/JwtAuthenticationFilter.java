package com.java.backend.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        System.out.println("1. 추출된 토큰: " + token); // 로그 추가

        if (token != null) {
            boolean isValid = jwtTokenProvider.validateToken(token);
            System.out.println("2. 토큰 유효성: " + isValid); // 로그 추가

            if (isValid) {
                String email = jwtTokenProvider.getEmail(token);
                System.out.println("3. 추출된 이메일: " + email); // 로그 추가

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("4. SecurityContext에 인증 정보 저장 완료");
            }
        }

        filterChain.doFilter(request, response);
    }

    // Header에서 "Bearer "를 제외한 토큰 값만 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}