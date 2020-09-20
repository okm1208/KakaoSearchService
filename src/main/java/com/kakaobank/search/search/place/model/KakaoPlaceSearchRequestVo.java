package com.kakaobank.search.search.place.model;

import lombok.Data;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
@Data
public class KakaoPlaceSearchRequestVo{
    String query;
    String categoryGroupCode;
    Double x;
    Double y;
    Integer radius;
    String rect;
    Integer page;
    Integer size;
    String sort;
}
