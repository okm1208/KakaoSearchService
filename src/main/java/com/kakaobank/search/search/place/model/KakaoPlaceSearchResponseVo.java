package com.kakaobank.search.search.place.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kakaobank.search.external.model.pageable.Meta;
import com.kakaobank.search.external.model.place.Document;
import lombok.Data;

import java.util.List;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoPlaceSearchResponseVo {

    private Meta meta;
    private List<Document> documents;

}
