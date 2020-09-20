package com.kakaobank.search.search.place.controller;

import com.kakaobank.search.common.model.CommonResponse;
import com.kakaobank.search.external.service.SearchApiService;
import com.kakaobank.search.history.entity.KeywordHistoryStatistics;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.KakaoPlaceSearchResponseVo;
import com.kakaobank.search.external.service.impl.KakaoPlaceKeywordSearchService;
import com.kakaobank.search.history.service.HistoryService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 장소 검색 컨트롤러
 */
@RestController
@RequestMapping("/place")
@Api(tags = "장소 검색 APIs")
public class PlaceSearchController {

    @Autowired
    @Qualifier("kakaoPlaceKeywordSearchService")
    private SearchApiService<KakaoPlaceSearchRequestVo,KakaoPlaceSearchResponseVo> kakaoPlaceKeywordSearchService;

    @Autowired
    HistoryService<List<KeywordHistoryStatistics>> historyService;

    @GetMapping("/search")
    @ApiOperation(value = "장소 검색 리스트 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "30")
    })
    public CommonResponse<KakaoPlaceSearchResponseVo> searchKeyword( @RequestParam("keyword") String keyword,
                                                                    @ApiIgnore @PageableDefault(size = 30) Pageable pageable){

        KakaoPlaceSearchRequestVo request = new KakaoPlaceSearchRequestVo();
        request.setPage(pageable.getPageNumber() + 1);
        request.setSize(pageable.getPageSize());
        request.setQuery(keyword);

        return CommonResponse.success(kakaoPlaceKeywordSearchService.search(request));
    }

    @GetMapping("/keyword/top")
    @ApiOperation(value = "인기 검색 리스트 API")
    public CommonResponse<List<KeywordHistoryStatistics>> searchTopKeyword(){
        return CommonResponse.success(historyService.findByTopHistory(10));

    }
}

