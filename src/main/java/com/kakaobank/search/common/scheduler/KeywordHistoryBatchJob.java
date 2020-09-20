package com.kakaobank.search.common.scheduler;

import com.kakaobank.search.history.entity.KeywordHistory;
import com.kakaobank.search.history.entity.KeywordHistoryStatistics;
import com.kakaobank.search.history.repository.KeywordHistoryRepository;
import com.kakaobank.search.history.repository.KeywordHistoryStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 장소 검색시 저장 되어 있는 키워드 누적 카운트 값 배치 처리
 */
@Component
public class KeywordHistoryBatchJob {

    @Autowired
    private KeywordHistoryRepository keywordHistoryRepository;

    @Autowired
    private KeywordHistoryStatisticsRepository keywordHistoryStatisticsRepository;

    @Scheduled(cron = "0 0/10 * * * *")
    @Transactional
    public void keywordHistoryBachJob() {

       List<KeywordHistory> keywordHistoryList =
            keywordHistoryRepository.findAllByRegDtBetween(LocalDateTime.now().minusHours(1) ,
                    LocalDateTime.now());

       if(!CollectionUtils.isEmpty(keywordHistoryList)){
           Map<String, Long> keywordCount =
                   keywordHistoryList.stream().collect(
                           Collectors.groupingBy(keywordHistory ->
                                    keywordHistory.getKeyword(), Collectors.counting()
                           )
                   );
           keywordCount.entrySet().stream().forEach(keywordEntry->{
               KeywordHistoryStatistics  keywordHistoryStatistics = new KeywordHistoryStatistics();
               keywordHistoryStatistics.setKeyword(keywordEntry.getKey());
               keywordHistoryStatistics.setAccumulateCount(keywordEntry.getValue());
               keywordHistoryStatisticsRepository.save(keywordHistoryStatistics);
           });
       }
    }

}
