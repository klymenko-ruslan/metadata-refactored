'use strict';

angular.module('ngMetaCrudApp', ['ui.bootstrap','restangular', 'ngTable'])
    .config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl('http://timetadata.herokuapp.com/');
        RestangularProvider.setExtraFields(['name']);
        RestangularProvider.setResponseExtractor(function(response, operation) {
            return response.data;
        })
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/part/:partId', {
                templateUrl: 'views/part/PartDetail.html',
                controller: 'PartDetailCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
