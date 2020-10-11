/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-10-11
 *  description :
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.TOO_MANY_REQUESTS;

    public TooManyRequestException() {
        super(defaultErrorResponse);
    }

    private TooManyRequestException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static TooManyRequestException of(String message) {
        return new TooManyRequestException(message);
    }
}
