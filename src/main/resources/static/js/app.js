(function () {
    'use strict';
    angular
        .module('app', ['ui.router', 'ngMessages', 'ngStorage','ui.bootstrap'])
        .config(config)
        .run(run);

    function config($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/");

        $stateProvider
            .state('search', {
                url: '/',
                templateUrl: 'service/search/search.view.html',
                controller: 'searchController'
            })
            .state('login', {
                url: '/login',
                templateUrl: 'service/login/login.view.html',
                controller: 'loginController'
            });
    }

    function run($rootScope, $http, $location, $localStorage) {
        if ($localStorage.currentUser) {
            $http.defaults.headers.common.Authorization = $localStorage.currentUser.accessToken;
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            var publicPages = ['/login'];
            var restrictedPage = publicPages.indexOf($location.path()) === -1;
            if (restrictedPage && !$localStorage.currentUser) {
                $location.path('/login');
            }
        });
    }
})();