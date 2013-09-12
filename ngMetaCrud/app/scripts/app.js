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
            .when(['/part/:partType'], {
                templateUrl: 'views/part/PartList.html',
                controller: 'PartListCtrl'
            })
            .when('/part/:partType/new', {
                templateUrl: 'views/part/PartForm.html',
                controller: 'PartFormCtrl'
            })
            .when('/part/:partType/:partId/edit', {
                templateUrl: 'views/part/PartForm.html',
                controller: 'PartFormCtrl'
            })
            .when('/part/:partType/:partId', {
                templateUrl: 'views/part/PartDetail.html',
                controller: 'PartDetailCtrl'
            })
            .otherwise({
                redirectTo: '/part/'
            });
    });
