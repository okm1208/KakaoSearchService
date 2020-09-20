package com.kakaobank.search.history.repository;

import com.kakaobank.search.history.entity.KeywordHistoryStatistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public interface KeywordHistoryStatisticsRepository extends JpaRepository<KeywordHistoryStatistics, Integer > {
    List<KeywordHistoryStatistics> findAllByRegDtAfterOrderByAccumulateCountDesc(
            LocalDateTime regDt,
            Pageable pageable);
}
