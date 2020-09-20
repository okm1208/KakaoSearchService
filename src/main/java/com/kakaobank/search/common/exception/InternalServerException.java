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
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends CustomBusinessException {
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.INTERNAL_SERVER_ERROR;

    private InternalServerException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static InternalServerException of(String message) {
        return new InternalServerException(message);
    }
}
