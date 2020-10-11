package com.kakaobank.search.external.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kakaobank.search.common.exception.DataNotFoundException;
import com.kakaobank.search.external.model.pageable.Meta;
import com.kakaobank.search.external.service.KakaoApiAbstract;
import com.kakaobank.search.external.service.SearchApiService;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchResponseVo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 카카오 장소 검색을 처리 한다.
 */
@Service("kakaoPlaceKeywordSearchService")
@Slf4j
public class KakaoPlaceKeywordSearchService extends KakaoApiAbstract
        implements SearchApiService<KakaoPlaceSearchRequestVo, KakaoPlaceSearchResponseVo>{

    @Autowired
    public KakaoPlaceKeywordSearchService(@Qualifier("kakaoApiRestTemplate") RestTemplate restTemplate){
        super(restTemplate);
    }

    @Override
    @HystrixCommand(commandKey = "kakaoSearchPlace" , fallbackMethod = "kakaoSearchFallback")
    public KakaoPlaceSearchResponseVo search(KakaoPlaceSearchRequestVo request) {
        UriComponents builder = uriBuilder(request);
        HttpHeaders httpHeaders = createDefaultHeader();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<KakaoPlaceSearchResponseVo> responseEntity = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                KakaoPlaceSearchResponseVo.class);

        if(responseEntity != null){
            return responseEntity.getBody();
        }
        return emptyKakaoPlaceSearchResponse();
    }

    //search api fallback method
    private KakaoPlaceSearchResponseVo kakaoSearchFallback(KakaoPlaceSearchRequestVo requestVo, Throwable t) throws Exception{
        if(t instanceof RuntimeException && HYSRCIRCUIT_OPEN_MESSAGE.equals(t.getMessage())){
            return emptyKakaoPlaceSearchResponse();
        }
        throw (RuntimeException)t;
    }
    private KakaoPlaceSearchResponseVo emptyKakaoPlaceSearchResponse(){
        KakaoPlaceSearchResponseVo response = new KakaoPlaceSearchResponseVo();
        Meta meta = new Meta();
        meta.setEnd(true);
        meta.setPageableCount(0);
        meta.setTotalCount(0);
        response.setDocuments(Collections.emptyList());
        response.setMeta(meta);
        return response;
    }

    private UriComponents uriBuilder(KakaoPlaceSearchRequestVo request) {
          UriComponents builder = UriComponentsBuilder.fromHttpUrl(placeSearchBaseUrl)
          .queryParam("query", request.getQuery())
          .queryParam("page", request.getPage())
          .queryParam("size", request.getSize())
          .build(false);

          return builder;
    }
}
