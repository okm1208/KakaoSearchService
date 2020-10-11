package com.kakaobank.search.external.model.naver.place;

import lombok.Data;

/**
 * @author 오경무 (okm1208@gmail.com)
 * @created 2020-10-11
 */
@Data
public class Item {

    String title;
    String link;
    String category;
    String description;
    String telephone;
    String address;
    String roadAddress;
    String mapx;
    String mapy;

}
