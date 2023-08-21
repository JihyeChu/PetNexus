package com.sparta.petnexus.common.security.config;

import com.sparta.petnexus.common.security.filter.TokenAuthenticationFilter;
import com.sparta.petnexus.common.security.jwt.TokenProvider;
import com.sparta.petnexus.common.security.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.sparta.petnexus.common.security.oauth.OAuth2SuccessHandler;
import com.sparta.petnexus.common.security.oauth.OAuth2UserCustomService;
import com.sparta.petnexus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class securityConfig {

    private final TokenProvider tokenProvider;
    private final OAuth2UserCustomService oAuth2UserCustomService;

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,

                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userRepository
        );
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 ->
                        oauth2
                                .loginPage("/login")
                                .authorizationEndpoint(authorization ->
                                        authorization
                                                .baseUri("/oauth2/authorization")
                                                .authorizationRequestRepository(
                                                        oAuth2AuthorizationRequestBasedOnCookieRepository()))
                                .redirectionEndpoint(redirection ->
                                        redirection
                                                .baseUri("/*/oauth2/code/*"))
                                .userInfoEndpoint(userInfo ->
                                        userInfo
                                                .userService(oAuth2UserCustomService))
                                .successHandler(oAuth2SuccessHandler()))
                .logout((logout) ->
                        logout
                                .logoutSuccessUrl("/login"))
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .defaultAuthenticationEntryPointFor(
                                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                        new AntPathRequestMatcher("/api/**"))
                );
        return http.build();
    }


}