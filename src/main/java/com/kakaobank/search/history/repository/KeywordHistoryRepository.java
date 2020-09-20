package com.kakaobank.search.history.repository;

import com.kakaobank.search.history.entity.KeywordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public interface KeywordHistoryRepository extends JpaRepository<KeywordHistory, Long > {

    List<KeywordHistory> findAllByRegDtBetween(LocalDateTime startDt, LocalDateTime endDt);
}
