'use strict';

angular.module('ngMetaCrudApp', ['ngRoute', 'ngTable', 'ui.bootstrap', 'restangular', 'dialogs', 'gToast'])
    .config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl('/metadata/');
        RestangularProvider.setParentless(true);
        RestangularProvider.setDefaultHeaders({"Content-Type": "text/plain"});
        RestangularProvider.setResponseExtractor(function (response, operation) {
            return response;
        })
    })
    .config(function ($locationProvider, $routeProvider) {
//        $locationProvider.html5Mode(true);

        // Parts
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


        // Users and groups
        $routeProvider.when('/security/groups', {
          templateUrl: 'views/security/groups.html',
          controller: 'GroupsCtrl'
        });
        $routeProvider.when('/security/group/:id', {
          templateUrl: 'views/security/group.html',
          controller: 'GroupCtrl'
        });

        $routeProvider.when('/security/users', {
          templateUrl: 'views/security/users.html',
          controller: 'UsersCtrl'
        });
        $routeProvider.when('/security/user/:id', {
          templateUrl: 'views/security/user.html',
          controller: 'UserCtrl'
        });


        // Default
        $routeProvider.otherwise({
            redirectTo: '/'
        });
    })
    .run(function($rootScope, $location, User) {

      $rootScope.$location = $location;

      // Initialize the user
      User.init();
    });
