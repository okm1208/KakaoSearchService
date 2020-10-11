package com.kakaobank.search.external.service.impl;

import com.kakaobank.search.external.model.kakao.pageable.Meta;
import com.kakaobank.search.external.service.NaverApiAbstract;
import com.kakaobank.search.external.service.SearchApiService;
import com.kakaobank.search.search.place.model.kakao.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.kakao.KakaoPlaceSearchResponseVo;
import com.kakaobank.search.search.place.model.naver.NaverPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.naver.NaverPlaceSearchResponseVo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-10-11
 *  description : 네이버 장소 검색을 처리 한다.
 */
@Service("naverPlaceKeywordSearchService")
@Slf4j
public class NaverPlaceKeywordSearchService extends NaverApiAbstract
        implements SearchApiService<NaverPlaceSearchRequestVo, NaverPlaceSearchResponseVo>{

    @Autowired
    public NaverPlaceKeywordSearchService(@Qualifier("naverApiRestTemplate") RestTemplate restTemplate){
        super(restTemplate);
    }

    @Override
    @HystrixCommand(commandKey = "naverSearchPlace" , fallbackMethod = "naverSearchFallback")
    public NaverPlaceSearchResponseVo search(NaverPlaceSearchRequestVo request) {
        UriComponents builder = uriBuilder(request);
        HttpHeaders httpHeaders = createNonLoginDefaultHeader();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<NaverPlaceSearchResponseVo> responseEntity = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                NaverPlaceSearchResponseVo.class);

        if(responseEntity != null){
            return responseEntity.getBody();
        }
        return emptyNaverPlaceSearchResponse();
    }

    //search api fallback method
    private NaverPlaceSearchResponseVo naverSearchFallback(NaverPlaceSearchRequestVo requestVo, Throwable t) throws Exception{
        if(t instanceof RuntimeException && HYSRCIRCUIT_OPEN_MESSAGE.equals(t.getMessage())){
            return emptyNaverPlaceSearchResponse();
        }
        throw (RuntimeException)t;
    }
    private NaverPlaceSearchResponseVo emptyNaverPlaceSearchResponse(){
        NaverPlaceSearchResponseVo response = new NaverPlaceSearchResponseVo();
        response.setStart(0);
        response.setDisplay(0);
        response.setTotal(0);

        response.setItems(Collections.emptyList());
        return response;
    }

    private UriComponents uriBuilder(NaverPlaceSearchRequestVo request) {
          UriComponents builder = UriComponentsBuilder.fromHttpUrl(placeSearchBaseUrl)
          .queryParam("query", request.getQuery())
          .queryParam("start", placeSearchStart)
          .queryParam("display", placeSearchMaxDisplay)
          .build(false);
          return builder;
    }
}
