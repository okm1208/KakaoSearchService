package com.kakaobank.search.external.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 카카오 API 공통 속성 및 기능 정의
 */
@Service
public abstract class KakaoApiAbstract{

   @Value("${external.kakao.rest-api-key}")
   protected String restApiKey;

   @Value("${external.kakao.place-search-base-url}")
   protected String placeSearchBaseUrl;

   protected RestTemplate restTemplate;

   protected KakaoApiAbstract(RestTemplate restTemplate){
      this.restTemplate = restTemplate;
   }

   protected HttpHeaders createDefaultHeader(){
      HttpHeaders httpHeaders =  new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);
      httpHeaders.set("Authorization", restApiKey);
      return httpHeaders;
   }
}
