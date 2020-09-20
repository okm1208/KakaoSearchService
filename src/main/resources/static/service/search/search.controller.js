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
            $scope.topKeywords = []


            $scope.searchKeyword = function() {
                SearchKeywordService.SearchKeyword($scope.keyword, $scope.currentPage, $scope.maxSize,function(success,response){
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