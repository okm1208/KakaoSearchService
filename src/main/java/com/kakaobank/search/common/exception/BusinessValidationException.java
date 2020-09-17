/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessValidationException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.INVALID;

    public BusinessValidationException() {
        super(defaultErrorResponse);
    }

    private BusinessValidationException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static BusinessValidationException of(String message) {
        return new BusinessValidationException(message);
    }
}