'use strict';

angular.module('ngMetaCrudApp', ['ui.bootstrap','restangular', 'ngTable', 'Mac', 'ui.select2'])
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
            .when('/part/:type/:id/interchange/search', {
                templateUrl: 'views/part/interchange/PartInterchangeSearch.html',
                controller: 'PartInterchangeSearchCtrl'
            })
            .when('/part/:type/:id/bom/search', {
                templateUrl: 'views/part/bom/PartBomSearch.html',
                controller: 'PartBomSearchCtrl'
            })
            .otherwise({
                redirectTo: '/part/'
            });
    });
