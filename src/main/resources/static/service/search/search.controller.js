(function () {
    'use strict';

    angular
        .module('app')
        .controller('searchController',  function ($scope, $uibModal, $log, $document, SearchKeywordService, AuthenticationService ) {
            $scope.keyword = '';

            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.maxSize = 10;
            $scope.selectedPlace = null;
            $scope.places = []

            $scope.nPlaces = []
            $scope.selectedNPlace = null;
            $scope.topKeywords = []

            $scope.searchTargets = [
                {name : "Kakao", value : 1},
                {name : "Naver", value : 2}
            ];

            $scope.selectedSearchTarget = null;

            $scope.searchKeyword = function() {

                //select box 값으로 분기

                if( $scope.selectedSearchTarget == '1') {
                    SearchKeywordService.KakaoSearchKeyword($scope.keyword, $scope.currentPage, $scope.maxSize,function(success,response){
                        if(success && response && response.data){
                            var meta = response.data.meta;
                            $scope.totalItems = meta.pageable_count
                            $scope.places = response.data.documents

                        }else if(!success){
                            if( response.type == 'NotAuthenticated' || response.status == 'NotAuthorized' ){
                                AuthenticationService.Logout()
                            }else{
                                alert("검색 실패 : "+response.errorMessage)
                            }
                        }
                    })

                }else{
                    SearchKeywordService.NaverSearchKeyword($scope.keyword, $scope.currentPage, $scope.maxSize,function(success,response){
                        if(success && response && response.data){
                            // var meta = response.data.total;
                            $scope.totalItems = response.data.total
                            $scope.nPlaces = response.data.items
                            console.log(response)

                        }else if(!success){
                            if( response.type == 'NotAuthenticated' || response.status == 'NotAuthorized' ){
                                AuthenticationService.Logout()
                            }else{
                                alert("검색 실패 : "+response.errorMessage)
                            }
                        }
                    });
                }

            }

            $scope.init = function(){
                $scope.searchTopKeyword()
            }
            $scope.setPage = function (pageNo) {
                $scope.currentPage = pageNo;
            };

            $scope.pageChanged = function() {
                $scope.searchKeyword()
            };

            $scope.animationsEnabled = true;
            $scope.goToLink = function(link){
                if(link){
                    window.open(link, "_blank");
                }
            }
            $scope.searchViewInfo = function(place){
                $scope.selectedPlace = place
                $scope.modalOpen('lg')
            }

            $scope.searchTopKeyword = function(){
                SearchKeywordService.SearchTopKeyword(function (success,response) {
                    if(success && response && response.data){
                        $scope.topKeywords = response.data
                    }else if(!success){
                        $scope.topKeywords = []
                    }
                })
            }
            $scope.modalOpen = function (size, parentSelector) {
                var parentElem = parentSelector ?
                    angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
                var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: 'service/search-info/search-info.view.html',
                    controller: 'searchInfoController',
                    size: size,
                    appendTo: parentElem,
                    resolve: {
                        selectedPlace: function () {
                            return $scope.selectedPlace;
                        }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };
        });

})();