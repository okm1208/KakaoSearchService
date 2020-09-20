/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.BAD_REQUEST;

    public BadRequestException() {
        super(defaultErrorResponse);
    }

    private BadRequestException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static BadRequestException of(String message) {
        return new BadRequestException(message);
    }
}
