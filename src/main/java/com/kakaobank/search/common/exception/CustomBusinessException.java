/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import lombok.Getter;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public class CustomBusinessException extends RuntimeException {
    @Getter
    protected ErrorResponse errorResponse;

    public CustomBusinessException(ErrorResponse errorResponse) {
        super(errorResponse.getErrorMessage());
        this.errorResponse = errorResponse;
    }
}
