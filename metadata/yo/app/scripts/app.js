'use strict';

angular.module('ngMetaCrudApp', ['ngRoute', 'ngTable', 'ui.bootstrap', 'restangular', 'dialogs', 'gToast'])
    .config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl('/metadata/');
        RestangularProvider.setDefaultHeaders({"Content-Type": "text/plain"});
        RestangularProvider.setResponseExtractor(function (response, operation) {
            return response;
        })
    })
    .config(function ($locationProvider, $routeProvider) {
//        $locationProvider.html5Mode(true);

        $routeProvider.when('/', {
            templateUrl: 'views/part/PartList.html',
            controller: 'PartListCtrl'
        });
        $routeProvider.when('/part/createByPartTypeId/:typeId', {
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
            redirectTo: '/'
        });
    })
    .run(function(User) {

      // Initialize the user
      User.init();
    });
