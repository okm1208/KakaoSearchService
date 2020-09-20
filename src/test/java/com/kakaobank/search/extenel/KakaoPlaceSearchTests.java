package com.kakaobank.search.extenel;

import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.config.RestTemplateConfig;
import com.kakaobank.search.common.exception.BadRequestException;
import com.kakaobank.search.external.service.SearchApiService;
import com.kakaobank.search.external.service.impl.KakaoPlaceKeywordSearchService;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 카카오 API 예외 발생 테스트
 */
@SpringBootTest(classes = { RestTemplateConfig.class, KakaoPlaceKeywordSearchService.class})
@Slf4j
public class KakaoPlaceSearchTests {
    @Autowired
    @Qualifier("kakaoPlaceKeywordSearchService")
    private SearchApiService<KakaoPlaceSearchRequestVo, KakaoPlaceSearchResponseVo> kakaoPlaceKeywordSearchService;

    @Ignore("오프라인 환경에서 테스트 불가")
    @Test
    public void 예외_처리_테스트(){
        KakaoPlaceSearchRequestVo request = new KakaoPlaceSearchRequestVo();
        BadRequestException thrown = assertThrows(
                BadRequestException.class,
                () -> kakaoPlaceKeywordSearchService.search(request)
        );
        assertThat(thrown.getMessage(), is(ErrorMessageProperties.KAKAO_API_BAD_REQUEST));

    }
}
