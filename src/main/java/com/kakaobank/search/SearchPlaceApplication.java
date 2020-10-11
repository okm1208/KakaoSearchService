package com.kakaobank.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
@EnableAspectJAutoProxy
@EnableCircuitBreaker
@SpringBootApplication
public class SearchPlaceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SearchPlaceApplication.class, args);
    }

}
