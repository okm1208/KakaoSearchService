package com.kakaobank.search.history.service;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 검색 API 기능
 */
public interface HistoryService<T> {
    T findByTopHistory(int top);

}
