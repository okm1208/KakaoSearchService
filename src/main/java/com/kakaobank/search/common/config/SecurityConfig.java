package com.kakaobank.search.common.config;

import com.kakaobank.search.auth.provider.AccountAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
//        prePostEnabled = true,
        securedEnabled = true
//        jsr250Enabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final ArrayList<String> permitAllAntPatterns = new ArrayList<>(Arrays.asList(
            "/h2/**",
            "/error"
    ));
    final ArrayList<String> authorizationPatterns = new ArrayList<>(Arrays.asList(
            "/search/**"
    ));

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity
                .ignoring()
                .antMatchers(
            "/css/**",
                        "/js/**",
                        "/excel/**",
                        "/fonts/**",
                        "/img/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final ArrayList<String> denyAllAntPatterns = new ArrayList<>(0);

        http
            .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                    .antMatchers(permitAllAntPatterns.toArray(new String[0])).permitAll()
                    .antMatchers(authorizationPatterns.toArray(new String[0])).authenticated();

//            .and()
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    public JwtAuthenticationProcessingFilter jwtAuthenticationFilter()throws Exception{
//
//        JwtAuthenticationProcessingFilter authenticationFilter =
//                new JwtAuthenticationProcessingFilter(jwtAuthenticationFailureHandler(),
//                        new SkipPathRequestMatcher(permitAllAntPatterns,authenticationPatterns));
//
//        authenticationFilter.setAuthenticationManager(super.authenticationManager());
//        return authenticationFilter;
//    }
//    @Bean
//    public JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler(){
//        return new JwtAuthenticationFailureHandler();
//    }
//
    @Bean
    public AccountAuthenticationProvider accountAuthenticationProvider(){
        return new AccountAuthenticationProvider();
    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(jwtAuthenticationProvider());
//    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(accountAuthenticationProvider());
//        auth.authenticationProvider(jwtAuthenticationProvider());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
