package com.kspo.carefit.base.config.security;

import com.kspo.carefit.base.security.filter.JwtFilter;
import com.kspo.carefit.base.security.handler.LoginSuccessHandler;
import com.kspo.carefit.base.security.handler.LogoutSuccessHandlerImpl;
import com.kspo.carefit.base.security.service.Oauth2UserServiceImpl;
import com.kspo.carefit.base.security.util.JwtUtil;
import com.kspo.carefit.damain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final Oauth2UserServiceImpl oauth2UserServiceImpl;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LogoutSuccessHandlerImpl logoutSuccessHandler;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(jwtUtil,userService),
                        UsernamePasswordAuthenticationFilter.class)
                // CORS 설정
                .cors((corsCustomizer)->corsCustomizer
                        .configurationSource(corsConfig.configurationSource()))
                // 로그인
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(oauth2UserServiceImpl))
                        .successHandler(loginSuccessHandler)
                )// 로그아웃
                .logout(logout-> logout
                        .logoutUrl("/oauth2/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(false)
                        .clearAuthentication(true))
                // 세션 설정
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // URI 허용 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();

    }
}
