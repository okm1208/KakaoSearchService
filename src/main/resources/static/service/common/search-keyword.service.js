(function () {
    'use strict';

    angular
        .module('app')
        .factory('SearchKeywordService', Service);

    function Service($http) {
        var service = {};

        service.NaverSearchKeyword = NaverSearchKeyword
        service.KakaoSearchKeyword = KakaoSearchKeyword
        service.SearchTopKeyword = SearchTopKeyword
        return service;

        function NaverSearchKeyword(keyword, page, size, callback){
            $http({
                url: '/place/naver/search',
                method: "GET",
                params: {keyword : keyword , page : page, size : size}
            })
                .success(function(response){
                    callback(true,response)
                })
                .error(function(response){
                    callback(false,response)
                })
        }
        function KakaoSearchKeyword(keyword , page, size  , callback){
            $http({
                url: '/place/kakao/search',
                method: "GET",
                params: {keyword : keyword , page : page, size : size}
                })
                .success(function(response){
                    callback(true,response)
                })
                .error(function(response){
                    callback(false,response)
            })
        }

        function SearchTopKeyword(callback){
            $http({
                url: '/place/keyword/top',
                method: "GET"
            })
            .success(function(response){
                callback(true,response)
            })
            .error(function(response){
                callback(false, response)
            })
        }
    }
})();