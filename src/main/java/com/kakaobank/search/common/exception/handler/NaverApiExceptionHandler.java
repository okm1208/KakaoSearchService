package com.kakaobank.search.common.exception.handler;


import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-10-11
 *  description : 네이버 API Errorhandler
 */
@Slf4j
public class NaverApiExceptionHandler implements ResponseErrorHandler {
    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {

        log.error("naver api error : {} , {} , {}",url , method.name(), response.getStatusText());
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
                throw BadRequestException.of(ErrorMessageProperties.NAVER_API_BAD_REQUEST);
            case UNAUTHORIZED:
                throw AuthenticationException.of(ErrorMessageProperties.NAVER_API_UNAUTHORIZED);
            case FORBIDDEN:
                throw AuthorityException.of(ErrorMessageProperties.NAVER_API_FORBIDDEN);
            case NOT_FOUND:
                throw DataNotFoundException.of(ErrorMessageProperties.NAVER_API_NOT_FOIND);
            case METHOD_NOT_ALLOWED:
                throw MethodNotAllowedException.of(ErrorMessageProperties.NAVER_METHOD_NOT_ALLOWED);
            case TOO_MANY_REQUESTS:
                throw TooManyRequestException.of(ErrorMessageProperties.NAVER_TOO_MANY_REQUESTS);
            case BAD_GATEWAY:
            case INTERNAL_SERVER_ERROR:
            default:
                throw InternalServerException.of(ErrorMessageProperties.NAVER_API_INTERNAL_SERVER_ERROR);
        }
    }

}
