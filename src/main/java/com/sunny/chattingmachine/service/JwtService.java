package com.sunny.chattingmachine.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public interface JwtService {

    String createAccessToken(String accountId);
    String createRefreshToken();

    void updateRefreshToken(String accountId, String refreshToken);

    void destroyRefreshToken(String accountId);

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);
    void sendAccessToken(HttpServletResponse response, String accessToken);

    Optional<String> extractAccessToken(HttpServletRequest request) throws IOException, ServletException;
    Optional<String> extractRefreshToken(HttpServletRequest request) throws IOException, ServletException;
    Optional<String> extractAccountId(String accessToken);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);
    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

    boolean isTokenValid(String token);
}
