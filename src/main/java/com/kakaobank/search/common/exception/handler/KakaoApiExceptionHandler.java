package com.kakaobank.search.common.exception.handler;


import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.exception.AuthenticationException;
import com.kakaobank.search.common.exception.AuthorityException;
import com.kakaobank.search.common.exception.BadRequestException;
import com.kakaobank.search.common.exception.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 카카오 API Error\handler
 */
@Slf4j
public class KakaoApiExceptionHandler implements ResponseErrorHandler {
    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {

        log.error("kakao api error : {} , {} , {}",url , method.name(), response.getStatusText());
        this.handleError(response);
    }
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return ! HttpStatus.OK.equals(httpResponse.getStatusCode());
    }
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        switch (response.getStatusCode()){
            case BAD_REQUEST:
                throw BadRequestException.of(ErrorMessageProperties.KAKAO_API_BAD_REQUEST);
            case UNAUTHORIZED:
                throw AuthenticationException.of(ErrorMessageProperties.KAKAO_API_UNAUTHORIZED);
            case FORBIDDEN:
                throw AuthorityException.of(ErrorMessageProperties.KAKAO_API_FORBIDDEN);
            case SERVICE_UNAVAILABLE:
                throw InternalServerException.of(ErrorMessageProperties.KAKAO_API_SERVICE_UNAVAILABLE);
            case BAD_GATEWAY:
            case INTERNAL_SERVER_ERROR:
            default:
                throw InternalServerException.of(ErrorMessageProperties.KAKAO_API_INTERNAL_SERVER_ERROR);

        }
    }
}
