package com.kakaobank.search.search.place.controller;

import com.kakaobank.search.common.model.CommonResponse;
import com.kakaobank.search.external.service.SearchApiService;
import com.kakaobank.search.history.entity.KeywordHistoryStatistics;
import com.kakaobank.search.search.place.model.kakao.KakaoPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.kakao.KakaoPlaceSearchResponseVo;
import com.kakaobank.search.history.service.HistoryService;
import com.kakaobank.search.search.place.model.naver.NaverPlaceSearchRequestVo;
import com.kakaobank.search.search.place.model.naver.NaverPlaceSearchResponseVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @Qualifier("naverPlaceKeywordSearchService")
    private SearchApiService<NaverPlaceSearchRequestVo, NaverPlaceSearchResponseVo> naverPlaceKeywordSearchService;

    @Autowired
    HistoryService<List<KeywordHistoryStatistics>> historyService;

    @GetMapping("/kakao/search")
    @ApiOperation(value = "장소 검색 리스트 API ( kakao )")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "15")
    })
    public CommonResponse<KakaoPlaceSearchResponseVo> kakaoSearchKeyword( @RequestParam("keyword") String keyword,
                                                                    @ApiIgnore @PageableDefault(size = 15) Pageable pageable){

        KakaoPlaceSearchRequestVo request = new KakaoPlaceSearchRequestVo();
        request.setPage(pageable.getPageNumber() + 1);
        request.setSize(pageable.getPageSize());
        request.setQuery(keyword);

        return CommonResponse.success(kakaoPlaceKeywordSearchService.search(request));
    }

    @GetMapping("/naver/search")
    @ApiOperation(value = "장소 검색 리스트 API ( naver )")
    public CommonResponse<NaverPlaceSearchResponseVo> naverSearchKeyword(
            @RequestParam("keyword") String keyword){

        NaverPlaceSearchRequestVo request = new NaverPlaceSearchRequestVo();
        request.setQuery(keyword);

        return CommonResponse.success(naverPlaceKeywordSearchService.search(request));
    }


    @GetMapping("/keyword/top")
    @ApiOperation(value = "인기 검색 리스트 API")
    public CommonResponse<List<KeywordHistoryStatistics>> searchTopKeyword(){
        return CommonResponse.success(historyService.findByTopHistory(10));

    }
}

