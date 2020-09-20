package com.kakaobank.search.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 유틸성 기능 테스트
 */
public class DateUtilsTests {

    @Test
    public void 두날짜_초_비교(){
        LocalDateTime currentDt = LocalDateTime.now();
        LocalDateTime plus1hourDt = currentDt.plusHours(1);

        assertThat( Duration.between(currentDt, plus1hourDt).toMillis() /1000 , is(3600L) );
    }
}
