package com.kakaobank.search.external.model.kakao.pageable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Meta {

    @JsonProperty("is_end")
    boolean isEnd;
    Integer pageableCount;
    SameName sameName;
    Integer totalCount;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class SameName {
        List<String> region;
        String keyword;
        String selectedRegion;
    }


}
