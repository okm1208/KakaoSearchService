package com.kakaobank.search.auth.filter.matchers;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class AuthPathRequestMatcher implements RequestMatcher {

    private OrRequestMatcher processingMatcher;

    public AuthPathRequestMatcher( List<String> pathsToProcessing) {

        List<RequestMatcher> processMatcherList = pathsToProcessing.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        processingMatcher = new OrRequestMatcher(processMatcherList);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return processingMatcher.matches(request) ? true : false;
    }
}
