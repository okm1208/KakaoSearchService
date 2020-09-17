package com.kakaobank.search.common.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommonResponse<T> {
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(T t) {
        return CommonResponse.<T>builder()
                .data(t)
                .build();
    }


}
