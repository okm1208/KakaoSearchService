/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.DATA_NOT_FOUND;

    public DataNotFoundException() {
        super(defaultErrorResponse);
    }

    private DataNotFoundException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static DataNotFoundException of(String message) {
        return new DataNotFoundException(message);
    }
}
