package com.kakaobank.search.history.service.impl;


import com.kakaobank.search.history.entity.KeywordHistoryStatistics;
import com.kakaobank.search.history.repository.KeywordHistoryRepository;
import com.kakaobank.search.history.repository.KeywordHistoryStatisticsRepository;
import com.kakaobank.search.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 인기 검색어 조회
 */
@Service
public class KeywordHistoryServiceImpl implements HistoryService<List<KeywordHistoryStatistics>>  {

    @Autowired
    private KeywordHistoryRepository keywordHistoryRepository;

    @Autowired
    private KeywordHistoryStatisticsRepository keywordHistoryStatisticsRepository;

    @Override
    public List<KeywordHistoryStatistics> findByTopHistory(int top) {
        return keywordHistoryStatisticsRepository.findAllByRegDtAfterOrderByAccumulateCountDesc(
                LocalDateTime.now().minusHours(1),
                PageRequest.of(0, top));
    }
}
