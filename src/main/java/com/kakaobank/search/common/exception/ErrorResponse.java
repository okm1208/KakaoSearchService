/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;

import java.util.stream.Stream;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public interface ErrorResponse {
    HttpStatus getStatus();

    String getErrorType();

    String getErrorMessage();

    static ErrorResponse of(int status) {
        Stream<ErrorResponse> stream = Stream.of(DefaultErrorResponse.values());

        return stream
                .filter(e -> e.getStatus().value() == status)
                .findFirst()
                .orElse(new UnknownErrorResponse(status))
                ;
    }
}
