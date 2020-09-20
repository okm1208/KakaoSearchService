package com.kakaobank.search.aspect;

import com.kakaobank.search.common.advice.KeywordHistoryAdvice;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.exception.DataNotFoundException;
import com.kakaobank.search.common.scheduler.KeywordHistoryBatchJob;
import com.kakaobank.search.external.service.SearchApiService;
import com.kakaobank.search.external.service.impl.KakaoPlaceKeywordSearchService;
import com.kakaobank.search.history.entity.KeywordHistory;
import com.kakaobank.search.history.entity.KeywordHistoryStatistics;
import com.kakaobank.search.history.repository.KeywordHistoryRepository;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchResponseVo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 검색 키워드 저장 테스트
 */

@EnableAspectJAutoProxy
@SpringBootTest(classes = {KakaoPlaceKeywordSearchService.class , KeywordHistoryAdvice.class})
public class KeywordHistoryStoreTests {

    @Autowired
    private SearchApiService<KakaoPlaceSearchRequestVo, KakaoPlaceSearchResponseVo>
            kakaoPlaceKeywordSearchService;

    @MockBean(name = "kakaoApiRestTemplate")
    private RestTemplate restTemplate;

    @MockBean
    private KeywordHistoryRepository keywordHistoryRepository;

    @Captor
    ArgumentCaptor<KeywordHistory> keywordHistoryCaptor;

    @Test
    public void 키워드_히스토리_저장_테스트(){
        KakaoPlaceSearchRequestVo requestVo = new KakaoPlaceSearchRequestVo();
        assertThrows(
                DataNotFoundException.class,
                () -> kakaoPlaceKeywordSearchService.search(requestVo)
        );

        verify(keywordHistoryRepository, never()).save(any());

        requestVo.setQuery("낙성대");
        assertThrows(
                DataNotFoundException.class,
                () -> kakaoPlaceKeywordSearchService.search(requestVo)
        );
        verify(keywordHistoryRepository, times(1)).save(keywordHistoryCaptor.capture());

        KeywordHistory arguments =  keywordHistoryCaptor.getValue();
        assertNotNull(arguments);
        assertThat(arguments.getKeyword(),is(requestVo.getQuery()));

    }

}
