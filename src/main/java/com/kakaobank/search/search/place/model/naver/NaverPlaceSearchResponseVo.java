package com.kakaobank.search.search.place.model.naver;

import com.kakaobank.search.external.model.naver.place.Item;
import lombok.Data;

import java.util.List;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-10-11
 *  description :
 */
@Data
public class NaverPlaceSearchResponseVo {
    String lastBuildDate;
    Integer total;
    Integer start;
    Integer display;
    private List<Item> items;

}
