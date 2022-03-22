package com.sunny.chattingmachine.handler;

import com.sunny.chattingmachine.service.JwtService;
import com.sunny.chattingmachine.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final AccountRepository accountRepository;

    public LoginSuccessJWTProvideHandler(JwtService jwtService, AccountRepository accountRepository) {
        this.jwtService = jwtService;
        this.accountRepository = accountRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        String accountId = extractAccountId(authentication);
        String accessToken = jwtService.createAccessToken(accountId);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        accountRepository.findByAccountId(accountId).ifPresent(
                account -> account.updateRefreshToken(refreshToken)
        );

        log.info("Login succeed. account id : {}", authentication);
        log.info("Issue accessToken : {}", accessToken);
        log.info("Issue refreshToken : {}", refreshToken);
    }

    private String extractAccountId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
