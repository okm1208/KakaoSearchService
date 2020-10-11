package com.kakaobank.search.search.place.model.naver;

import lombok.Data;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-10-11
 *  description :
 */
@Data
public class NaverPlaceSearchRequestVo {
    String query;
    String sort;
}
