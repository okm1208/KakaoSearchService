package com.kakaobank.search.common.config;

import com.kakaobank.search.common.exception.CustomBusinessException;
import com.kakaobank.search.common.exception.CustomizableErrorResponse;
import com.kakaobank.search.common.exception.ErrorResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Configuration
public class CustomExceptionAdvice {
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = new LinkedHashMap<>();
                options.including(ErrorAttributeOptions.Include.MESSAGE);

                ErrorResponse errorResponse = Optional.ofNullable(getError(webRequest))
                        .filter(throwable -> CustomBusinessException.class.isAssignableFrom(throwable.getClass()))
                        .map(throwable -> ((CustomBusinessException) throwable).getErrorResponse())
                        .orElse(errorResponse(webRequest));

                errorAttributes.put("errorType", errorResponse.getErrorType());
                errorAttributes.put("message", errorResponse.getErrorMessage());

                return errorAttributes;
            }
            private ErrorResponse errorResponse(WebRequest webRequest) {
                return CustomizableErrorResponse.of(errorStatus(webRequest), extractErrorMessage(webRequest));
            }

            private Integer errorStatus(RequestAttributes requestAttributes) {
                return getAttribute(requestAttributes, "javax.servlet.error.status_code");
            }

            private String extractErrorMessage(WebRequest webRequest) {
                return Optional.ofNullable(getError(webRequest))
                        .map(this::getErrorMessage)
                        .orElseGet(() -> ErrorResponse.of(errorStatus(webRequest)).getErrorMessage());
            }

            private String getErrorMessage(Throwable throwable) {
                BindingResult result = extractBindingResult(throwable);

                if (Objects.isNull(result)) {
                    return throwable.getMessage();
                }

                if (result.hasErrors()) {
                    return "Validation failed for object='" + result.getObjectName()
                            + "'. Error count: " + result.getErrorCount();
                } else {
                    return "No errors";
                }
            }

            private BindingResult extractBindingResult(Throwable error) {
                if (error instanceof BindingResult) {
                    return (BindingResult) error;
                }
                if (error instanceof MethodArgumentNotValidException) {
                    return ((MethodArgumentNotValidException) error).getBindingResult();
                }
                return null;
            }

            private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
                return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
            }
        };
    }
}
