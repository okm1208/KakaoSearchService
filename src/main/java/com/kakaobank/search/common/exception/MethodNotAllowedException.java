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
@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class MethodNotAllowedException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.METHOD_NOT_ALLOWED;


    public MethodNotAllowedException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static MethodNotAllowedException of(String message) {
        return new MethodNotAllowedException(message);
    }
}
