package com.kakaobank.search.external.service;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 검색 API 기능
 */
public interface SearchApiService<T,U> {
    String HYSRCIRCUIT_OPEN_MESSAGE = "Hystrix circuit short-circuited and is OPEN";
    U search(T request) throws RuntimeException;
}
