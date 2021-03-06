package com.kakaobank.search.history.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 검색 키워드
 */
@Data
@Entity
@Table(name="keyword_history")
public class KeywordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histNo;

    @Column(length = 100, nullable = false)
    private String keyword;
    @Column(nullable = false)
    private LocalDateTime regDt;


}
