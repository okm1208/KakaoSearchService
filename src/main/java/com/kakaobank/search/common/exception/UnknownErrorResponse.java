/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Optional;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public class UnknownErrorResponse implements ErrorResponse {
    private HttpStatus status;

    public UnknownErrorResponse(int status) {
        this.status = HttpStatus.valueOf(status);
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

    @Override
    public String getErrorType() {
        return Optional.ofNullable(this.status)
                .map(HttpStatus::getReasonPhrase)
                .orElse(DefaultErrorResponse.INTERNAL_SERVER_ERROR.getErrorType())
                ;
    }

    @Override
    public String getErrorMessage() {
        return DefaultErrorResponse.INTERNAL_SERVER_ERROR.getErrorMessage();
    }
}
