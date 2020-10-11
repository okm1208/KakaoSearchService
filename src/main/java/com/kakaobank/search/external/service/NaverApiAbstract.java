package com.kakaobank.search.external.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-10-11
 *  description : 네이버 API 공통 속성 및 기능 정의
 */
@Service
public abstract class NaverApiAbstract {

   @Value("${external.naver.client-id}")
   protected String clientId;

   @Value("${external.naver.client-secret}")
   protected String clientSecret;

   @Value("${external.naver.place-search-base-url}")
   protected String placeSearchBaseUrl;


   protected Integer placeSearchMaxDisplay = 5;
   protected Integer placeSearchStart = 1;

   protected RestTemplate restTemplate;

   protected NaverApiAbstract(RestTemplate restTemplate){
      this.restTemplate = restTemplate;
   }

   protected HttpHeaders createNonLoginDefaultHeader(){
      HttpHeaders httpHeaders =  new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);
      httpHeaders.set("X-Naver-Client-Id", clientId);
      httpHeaders.set("X-Naver-Client-Secret", clientSecret);
      return httpHeaders;
   }
}
