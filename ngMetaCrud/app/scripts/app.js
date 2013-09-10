'use strict';

angular.module('ngMetaCrudApp', ['ui.bootstrap','restangular', 'ngTable'])
    .config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl('http://localhost:8084/');
        RestangularProvider.setDefaultHttpFields({accept:"application/json"})
        RestangularProvider.setExtraFields(['name']);
        RestangularProvider.setResponseExtractor(function(response, operation) {
            return response.data;
        })
    })
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/part/PartSearch.html',
                controller: 'PartSearchCtrl'
            })
            .when('/part/new', {
                templateUrl: 'views/part/PartCreate.html',
                controller: 'PartCreateCtrl'
            })
            .when('/part/:partId/edit', {
                templateUrl: 'views/part/PartEdit.html',
                controller: 'PartEditCtrl'
            })
            .when('/part/:partId', {
                templateUrl: 'views/part/PartDetail.html',
                controller: 'PartDetailCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
