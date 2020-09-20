package com.kakaobank.search.scheduler;


import com.kakaobank.search.common.scheduler.KeywordHistoryBatchJob;
import com.kakaobank.search.history.entity.KeywordHistory;
import com.kakaobank.search.history.entity.KeywordHistoryStatistics;
import com.kakaobank.search.history.repository.KeywordHistoryRepository;
import com.kakaobank.search.history.repository.KeywordHistoryStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 키워드 통계 배치 테스트
 */

@Slf4j
@SpringBootTest(classes = {KeywordHistoryBatchJob.class})
public class KeywordSchedulerTests {

    @Autowired
    private KeywordHistoryBatchJob keywordHistoryBatchJob;

    @MockBean
    private KeywordHistoryRepository keywordHistoryRepository;

    @MockBean
    private KeywordHistoryStatisticsRepository keywordHistoryStatisticsRepository;

    @Captor
    ArgumentCaptor<KeywordHistoryStatistics> keywordHistoryStatCaptor;

    @Test
    public void 통계_배치_테스트(){
        given(keywordHistoryRepository.findAllByRegDtBetween(Mockito.any(),Mockito.any())).willReturn(null);
        verify(keywordHistoryStatisticsRepository, never()).save(Mockito.any());

        keywordHistoryBatchJob.keywordHistoryBachJob();

        //Mock data create
        List<KeywordHistory> mockKeywordHistoryList = new ArrayList<>();
        mockKeywordHistoryList.add(makeMockKeywordHistory("낙성대", LocalDateTime.now()));
        mockKeywordHistoryList.add(makeMockKeywordHistory("낙성대", LocalDateTime.now().minusSeconds(2)));
        mockKeywordHistoryList.add(makeMockKeywordHistory("강남역", LocalDateTime.now().minusSeconds(10)));
        mockKeywordHistoryList.add(makeMockKeywordHistory("곱창집", LocalDateTime.now().minusMinutes(30)));


        given(keywordHistoryRepository.findAllByRegDtBetween(Mockito.any(),Mockito.any())).willReturn(mockKeywordHistoryList);
        keywordHistoryBatchJob.keywordHistoryBachJob();

        //예상 결과 : 유니크한 키워드 3개
        verify(keywordHistoryStatisticsRepository, times(3)).save(keywordHistoryStatCaptor.capture());

        List<KeywordHistoryStatistics> arguments =  keywordHistoryStatCaptor.getAllValues();
        assertTrue(!arguments.isEmpty());
        assertThat(arguments.size(),is(3));

        //예상 결과 : 낙성대 , 2
        KeywordHistoryStatistics nakseongdaeArgument =
                arguments.stream().filter(val -> "낙성대".equals(val.getKeyword())).findFirst().orElse(null);

        assertNotNull(nakseongdaeArgument);
        assertThat(nakseongdaeArgument.getAccumulateCount(), is(2L));

        //예상 결과 : 강남역 , 1
        KeywordHistoryStatistics gangnamArgument =
                arguments.stream().filter(val -> "강남역".equals(val.getKeyword())).findFirst().orElse(null);

        assertNotNull(gangnamArgument);
        assertThat(gangnamArgument.getAccumulateCount(), is(1L));
    }

    private KeywordHistory makeMockKeywordHistory(String keyword, LocalDateTime regDt){
        KeywordHistory keywordHistory = new KeywordHistory();
        keywordHistory.setKeyword(keyword);
        keywordHistory.setRegDt(regDt);
        return keywordHistory;
    }

}
