'use strict';

angular.module('ngMetaCrudApp', ['ui.bootstrap','restangular', 'ngTable'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/details', {
                templateUrl: 'views/details.html',
                controller: 'DetailsCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });