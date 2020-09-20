package com.kakaobank.search.common.advice;


import com.kakaobank.search.search.place.model.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.history.entity.KeywordHistory;
import com.kakaobank.search.history.repository.KeywordHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 장소 검색시 사용한 키워드를 저장한다. ( History 방식 )
 */
@Aspect
@Component
@Slf4j
public class KeywordHistoryAdvice {

    @Autowired
    private KeywordHistoryRepository keywordHistoryRepository;

    @Before("execution(* com.kakaobank.search.external.service.SearchApiService.search(..)) && args(request)")
    public void historySaveJoinPoint(Object request) {
        if(request instanceof KakaoPlaceSearchRequestVo){
            KakaoPlaceSearchRequestVo requestVo = (KakaoPlaceSearchRequestVo)request;
            if(!StringUtils.isEmpty(requestVo.getQuery())){
                KeywordHistory keywordHistory = new KeywordHistory();
                keywordHistory.setKeyword(requestVo.getQuery());
                keywordHistory.setRegDt(LocalDateTime.now());
                keywordHistoryRepository.save(keywordHistory);
            }
        }
    }
}
