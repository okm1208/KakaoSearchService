(function () {
    'use strict';

    angular
        .module('app')
        .factory('AuthenticationService', Service);

    function Service($http, $location,$localStorage) {
        var service = {};

        service.Login = Login;
        service.Logout = Logout;

        return service;

        function Login(username, password, callback) {
            $http.post('/auth/login', { userId: username, password: password })
                .success(function (response) {
                    if (response.data.accessToken) {
                        $localStorage.currentUser = { username: username, accessToken: response.data.accessToken , refreshToken  : response.data.refreshToken };

                        $http.defaults.headers.common.Authorization = response.data.accessToken;
                        callback(true, undefined);
                    }
                }).error(function(response){
                    callback(false,response)
                });
        }

        function Logout() {
            if( $http.defaults.headers.common.Authorization ){
                $http.defaults.headers.common.Authorization = $localStorage.currentUser.refreshToken;
                $http.post('/auth/logout').success(function(response){
                    if(response){
                        $http.defaults.headers.common.Authorization = '';
                        delete $localStorage.currentUser;
                    }
                    $location.path('/login');
                })
            }

        }
    }
})();