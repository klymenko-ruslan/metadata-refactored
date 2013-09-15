'use strict';

angular.module('ngMetaCrudApp', ['ui.bootstrap','restangular', 'ngTable'])
    .config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl('http://localhost:8080/');
        RestangularProvider.setDefaultHttpFields({accept:"application/json"})
        RestangularProvider.setExtraFields(['name']);
        RestangularProvider.setResponseExtractor(function(response, operation) {
            return response;
        })
    })
    .config(function ($routeProvider) {
        $routeProvider
            .when(['/part/:type'], {
                templateUrl: 'views/part/PartList.html',
                controller: 'PartListCtrl'
            })
            .when('/part/:type/form', {
                templateUrl: 'views/part/PartForm.html',
                controller: 'PartFormCtrl'
            })
            .when('/part/:type/:id/form', {
                templateUrl: 'views/part/PartForm.html',
                controller: 'PartFormCtrl'
            })
            .when('/part/:type/:id', {
                templateUrl: 'views/part/PartDetail.html',
                controller: 'PartDetailCtrl'
            })
            .otherwise({
                redirectTo: '/part/'
            });
    });
