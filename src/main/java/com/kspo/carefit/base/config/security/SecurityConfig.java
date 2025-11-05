package com.kspo.carefit.base.config.security;

import com.kspo.carefit.base.security.handler.LoginSuccessHandler;
import com.kspo.carefit.base.security.handler.LogoutSuccessHandlerImpl;
import com.kspo.carefit.base.security.service.Oauth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final Oauth2UserServiceImpl oauth2UserServiceImpl;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable);
        http
                .formLogin(AbstractHttpConfigurer::disable);
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        http // CORS 설정
                .cors((corsCustomizer)->corsCustomizer
                        .configurationSource(corsConfig.configurationSource()));
        http // 로그인
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(oauth2UserServiceImpl))
                        .successHandler(loginSuccessHandler)
                );
        http // 로그아웃
                .logout(logout-> logout
                        .logoutUrl("/oauth2/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(false)
                        .clearAuthentication(true));
        http // 세션 설정
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http // URI 허용 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();

    }
}
