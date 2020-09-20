package com.kakaobank.search.common.model;

import lombok.Builder;
import lombok.Data;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 공통 응답 객체
 */
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
    public static <Void> CommonResponse<Void> success() {
        return CommonResponse.<Void>builder()
                .build();
    }

}
