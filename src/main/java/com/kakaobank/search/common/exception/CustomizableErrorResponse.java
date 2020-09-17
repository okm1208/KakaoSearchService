/*
 * Copyright (c) 2019 Ryan Information Test
 */

package com.kakaobank.search.common.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomizableErrorResponse implements ErrorResponse {

    private HttpStatus status;
    private String errorType;
    private String errorMessage;

    public static CustomizableErrorResponse of(Integer httpStatus, String errorMessage) {
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus);

        return new CustomizableErrorResponse(errorResponse.getStatus(), errorResponse.getErrorType(), errorMessage);
    }

    public static CustomizableErrorResponse of(DefaultErrorResponse errorResponse, String errorMessage) {
        return new CustomizableErrorResponse(errorResponse.getStatus(), errorResponse.getErrorType(), errorMessage);
    }
}
