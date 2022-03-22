package com.sunny.chattingmachine.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {
    public static String getLoginUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userDetails.getUsername();
    }
}
