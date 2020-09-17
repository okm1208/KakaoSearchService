/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorityException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.FORBIDDEN;

    public AuthorityException() {
        super(defaultErrorResponse);
    }

    private AuthorityException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static AuthorityException of(String message) {
        return new AuthorityException(message);
    }
}
