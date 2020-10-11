package com.kakaobank.search.common.config;

import com.kakaobank.search.common.exception.handler.KakaoApiExceptionHandler;
import com.kakaobank.search.common.exception.handler.NaverApiExceptionHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : RestTemplate 설정
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public ResponseErrorHandler kakaoApiErrorHandler(){
        return new KakaoApiExceptionHandler();
    }
    @Bean
    public ResponseErrorHandler naverApiErrorHandler() {
        return new NaverApiExceptionHandler();
    }
    @Bean
    public RestTemplate kakaoApiRestTemplate(){
        HttpClient httpClient = HttpClients.custom()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(150)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(kakaoApiErrorHandler());

        return restTemplate;
    }

    @Bean
    public RestTemplate naverApiRestTemplate(){
        HttpClient httpClient = HttpClients.custom()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(150)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(naverApiErrorHandler());

        return restTemplate;
    }
}
