package com.sunny.chattingmachine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.chattingmachine.filter.JsonAccountAuthenticationFilter;
import com.sunny.chattingmachine.filter.JwtAuthenticationProcessingFilter;
import com.sunny.chattingmachine.handler.LoginFailureHandler;
import com.sunny.chattingmachine.handler.LoginSuccessJWTProvideHandler;
import com.sunny.chattingmachine.repository.AccountRepository;
import com.sunny.chattingmachine.service.JwtService;
import com.sunny.chattingmachine.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    public SecurityConfig(LoginService loginService, ObjectMapper objectMapper,
                          AccountRepository accountRepository, JwtService jwtService) {
        this.loginService = loginService;
        this.objectMapper = objectMapper;
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .formLogin().disable()  //disable formLogin authentication
                .httpBasic().disable()  //disable httpBasic authentication
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/login", "/signUp","/").permitAll()
                .anyRequest().authenticated();

        http.addFilterAfter(jsonAccountAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), JsonAccountAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler() {
        return new LoginSuccessJWTProvideHandler(jwtService, accountRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public JsonAccountAuthenticationFilter jsonAccountAuthenticationFilter() {
        JsonAccountAuthenticationFilter jsonLoginFilter = new JsonAccountAuthenticationFilter(objectMapper);

        jsonLoginFilter.setAuthenticationManager(authenticationManager());
        jsonLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        jsonLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, accountRepository);
    }
}
