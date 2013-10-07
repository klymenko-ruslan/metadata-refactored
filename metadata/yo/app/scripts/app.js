'use strict';

angular.module('ngMetaCrudApp', ['ngRoute', 'ngTable', 'ui.bootstrap','restangular', 'Mac', 'localytics.directives'])
    .config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl('http://localhost:8080/');
        RestangularProvider.setDefaultHttpFields({accept:"application/json"})
        RestangularProvider.setExtraFields(['name']);
        RestangularProvider.setResponseExtractor(function(response, operation) {
            return response;
        })
    })
    .config(function ($routeProvider) {
        $routeProvider.when('/part/:type', {
            templateUrl: 'views/part/PartList.html',
            controller: 'PartListCtrl'
        });
        $routeProvider.when('/part/:type/form', {
            templateUrl: 'views/part/PartForm.html',
            controller: 'PartFormCtrl'
        });
        $routeProvider.when('/part/:type/:id/form', {
            templateUrl: 'views/part/PartForm.html',
            controller: 'PartFormCtrl'
        });
        $routeProvider.when('/part/:type/:id', {
            templateUrl: 'views/part/PartDetail.html',
            controller: 'PartDetailCtrl'
        });
        $routeProvider.when('/part/:type/:id/interchange/search', {
            templateUrl: 'views/part/interchange/PartInterchangeSearch.html',
            controller: 'PartInterchangeSearchCtrl'
        });
        $routeProvider.when('/part/:type/:id/bom/search', {
            templateUrl: 'views/part/bom/PartBomSearch.html',
            controller: 'PartBomSearchCtrl'
        });
        $routeProvider.otherwise({
            redirectTo: '/part/'
        });
    });
