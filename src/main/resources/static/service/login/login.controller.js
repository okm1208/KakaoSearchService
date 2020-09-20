(function () {
    'use strict';

    angular
        .module('app')
        .controller('loginController', function($scope, $location, AuthenticationService){

            $scope.loading = false;
            $scope.error = '';
            $scope.username = '';
            $scope.password = '';

            $scope.login = function() {
                $scope.loading = true;
                AuthenticationService.Login($scope.username, $scope.password, function (success , response) {
                    if (success === true) {
                        $location.path('/');
                    } else {
                        $scope.error = response.message
                        $scope.loading = false;
                    }
                });
            }

            $scope.logout = function(){
                AuthenticationService.Logout();
            }

        });
})();