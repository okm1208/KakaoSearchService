/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.UNAUTHORIZED;

    public AuthenticationException() {
        super(defaultErrorResponse);
    }

    private AuthenticationException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static AuthenticationException of(String message) {
        return new AuthenticationException(message);
    }
}
