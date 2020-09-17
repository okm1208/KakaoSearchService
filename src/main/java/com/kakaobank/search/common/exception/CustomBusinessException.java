/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import lombok.Getter;
public class CustomBusinessException extends RuntimeException {
    @Getter
    protected ErrorResponse errorResponse;

    protected CustomBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomBusinessException(ErrorResponse errorResponse) {
        super(errorResponse.getErrorMessage());
        this.errorResponse = errorResponse;
    }
}
