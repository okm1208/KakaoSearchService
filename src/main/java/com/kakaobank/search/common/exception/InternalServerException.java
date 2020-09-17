/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.INTERNAL_SERVER_ERROR;

    public InternalServerException() {
        super(defaultErrorResponse);
    }

    private InternalServerException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static InternalServerException of(String message) {
        return new InternalServerException(message);
    }
}
