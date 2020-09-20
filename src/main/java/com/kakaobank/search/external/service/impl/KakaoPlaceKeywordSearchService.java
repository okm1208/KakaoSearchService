package com.kakaobank.search.external.service.impl;

import com.kakaobank.search.common.exception.DataNotFoundException;
import com.kakaobank.search.external.service.KakaoApiAbstract;
import com.kakaobank.search.external.service.SearchApiService;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 카카오 장소 검색을 처리 한다.
 */
@Service("kakaoPlaceKeywordSearchService")
public class KakaoPlaceKeywordSearchService extends KakaoApiAbstract
        implements SearchApiService<KakaoPlaceSearchRequestVo, KakaoPlaceSearchResponseVo>{

    @Autowired
    public KakaoPlaceKeywordSearchService(@Qualifier("kakaoApiRestTemplate") RestTemplate restTemplate){
        super(restTemplate);
    }

    @Override
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
        throw new DataNotFoundException();
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
