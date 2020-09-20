(function () {
    'use strict';
    angular
        .module('app')
        .controller('searchInfoController',  function ($uibModalInstance, $scope , selectedPlace ) {

            $scope.init = function() {
                // var container = document.getElementById('map');
                // var options = {
                //     center: new kakao.maps.LatLng(
                //         selectedPlace.y,
                //         selectedPlace.x
                //     ),
                //     level: 3
                // };
                //
                // var map = new kakao.maps.Map(container, options);
            }
            $scope.place = selectedPlace
            $scope.ok = function () {
                $uibModalInstance.close($scope.place);
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        });
})();