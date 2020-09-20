package com.kakaobank.search.common.config;

import com.kakaobank.search.auth.filter.JwtAuthenticationProcessingFilter;
import com.kakaobank.search.auth.filter.matchers.AuthPathRequestMatcher;
import com.kakaobank.search.auth.provider.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.Arrays;


/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 웹 인증 관련 설정
 */

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final ArrayList<String> permitAllPatterns = new ArrayList<>(Arrays.asList(
            "/error",
            "/auth/**"
            ));
    final ArrayList<String> authorizationPatterns = new ArrayList<>(Arrays.asList(
            "/place/**"
    ));

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity
                .ignoring()
                .antMatchers(
            "/css/**",
                        "/js/**",
                        "/fonts/**",
                        "/img/**",
                        "/service/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()

                    .antMatchers(permitAllPatterns.toArray(new String[0])).permitAll()
                    .antMatchers(authorizationPatterns.toArray(new String[0])).hasRole("USER")
                    .anyRequest().permitAll()
            .and()
                .addFilterBefore(jwtAuthenticationFilter(), FilterSecurityInterceptor.class)
            .headers()
                .frameOptions().disable();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationFilter()throws Exception{
        JwtAuthenticationProcessingFilter authenticationFilter =
                new JwtAuthenticationProcessingFilter(
                        new AuthPathRequestMatcher(authorizationPatterns));

        authenticationFilter.setAuthenticationManager(super.authenticationManager());
        return authenticationFilter;
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(jwtAuthenticationProvider());
    }
    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(){
        return new JwtAuthenticationProvider();
    }
}
