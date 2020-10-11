package com.kakaobank.search.aspect;

import com.kakaobank.search.common.advice.KeywordHistoryAdvice;
import com.kakaobank.search.common.exception.DataNotFoundException;
import com.kakaobank.search.external.model.kakao.pageable.Meta;
import com.kakaobank.search.external.service.SearchApiService;
import com.kakaobank.search.external.service.impl.KakaoPlaceKeywordSearchService;
import com.kakaobank.search.external.service.impl.NaverPlaceKeywordSearchService;
import com.kakaobank.search.history.entity.KeywordHistory;
import com.kakaobank.search.history.repository.KeywordHistoryRepository;
import com.kakaobank.search.search.place.model.kakao.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.kakao.KakaoPlaceSearchResponseVo;
import com.kakaobank.search.search.place.model.naver.NaverPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.naver.NaverPlaceSearchResponseVo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 검색 키워드 저장 테스트
 */

@EnableAspectJAutoProxy
@SpringBootTest(classes = {KakaoPlaceKeywordSearchService.class ,
        NaverPlaceKeywordSearchService.class,
        KeywordHistoryAdvice.class})
public class KeywordHistoryStoreTests {

    @Autowired
    @Qualifier("kakaoPlaceKeywordSearchService")
    private SearchApiService<KakaoPlaceSearchRequestVo, KakaoPlaceSearchResponseVo>
            kakaoPlaceKeywordSearchService;


    @Autowired
    @Qualifier("naverPlaceKeywordSearchService")
    private SearchApiService<NaverPlaceSearchRequestVo, NaverPlaceSearchResponseVo>
            naverPlaceSearchResponseVoSearchApiService;

    @MockBean(name = "kakaoApiRestTemplate")
    private RestTemplate restTemplate;

    @MockBean(name="naverApiRestTemplate")
    private RestTemplate naverRestTemplate;

    @MockBean
    private KeywordHistoryRepository keywordHistoryRepository;

    @Captor
    ArgumentCaptor<KeywordHistory> keywordHistoryCaptor;

    @Test
    public void KaKao_키워드_히스토리_저장_테스트(){
        KakaoPlaceSearchRequestVo requestVo = new KakaoPlaceSearchRequestVo();
        KakaoPlaceSearchResponseVo emptyResponse =  kakaoPlaceKeywordSearchService.search(requestVo);
        assertNotNull(emptyResponse);
        assertThat(emptyResponse.getMeta().getTotalCount(), is(0));
        assertThat(emptyResponse.getMeta().isEnd(), is(true));
        assertThat(emptyResponse.getMeta().getPageableCount(), is(0));
        assertTrue(emptyResponse.getDocuments().isEmpty());

        verify(keywordHistoryRepository, never()).save(any());

        requestVo.setQuery("낙성대");

        kakaoPlaceKeywordSearchService.search(requestVo);

        verify(keywordHistoryRepository, times(1)).save(keywordHistoryCaptor.capture());

        KeywordHistory arguments =  keywordHistoryCaptor.getValue();
        assertNotNull(arguments);
        assertThat(arguments.getKeyword(),is(requestVo.getQuery()));
    }

    @Test
    public void Naver_키워드_히스토리_저장_테스트(){
        NaverPlaceSearchRequestVo requestVo = new NaverPlaceSearchRequestVo();
        NaverPlaceSearchResponseVo emptyResponse =  naverPlaceSearchResponseVoSearchApiService.search(requestVo);
        assertNotNull(emptyResponse);
        assertThat(emptyResponse.getStart(), is(0));
        assertThat(emptyResponse.getDisplay(), is(0));
        assertThat(emptyResponse.getTotal(), is(0));
        assertTrue(emptyResponse.getItems().isEmpty());

        verify(keywordHistoryRepository, never()).save(any());

        requestVo.setQuery("낙성대");

        naverPlaceSearchResponseVoSearchApiService.search(requestVo);

        verify(keywordHistoryRepository, times(1)).save(keywordHistoryCaptor.capture());

        KeywordHistory arguments =  keywordHistoryCaptor.getValue();
        assertNotNull(arguments);
        assertThat(arguments.getKeyword(),is(requestVo.getQuery()));
    }
}
