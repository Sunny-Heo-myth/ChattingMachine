package com.sunny.chattingmachine.filter;

import com.sunny.chattingmachine.domain.Account;
import com.sunny.chattingmachine.repository.AccountRepository;
import com.sunny.chattingmachine.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AccountRepository accountRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final String NO_CHECK_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).flatMap(jwtService::extractAccountId).ifPresent(accountId -> accountRepository.findByAccountId(accountId).ifPresent(
                this::saveAuthentication
        ));
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Account account) {
        UserDetails userDetails = User.builder()
                .username(account.getAccountId())
                .password(account.getPassword())
                .roles(account.getAccountRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        accountRepository.findByRefreshToken(refreshToken).ifPresent(
                account -> jwtService.sendAccessToken(response, jwtService.createAccessToken(account.getAccountId()))
        );
    }

}
