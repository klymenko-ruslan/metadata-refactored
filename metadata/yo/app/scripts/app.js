'use strict';

angular.module('ngMetaCrudApp', ['ngRoute', 'ngTable', 'ui.bootstrap', 'restangular', 'dialogs', 'gToast'])
    .config(function ($locationProvider, $httpProvider, $routeProvider, RestangularProvider) {
        $httpProvider.interceptors.push('loginRequiredInterceptor');

        RestangularProvider.setBaseUrl('/metadata/');
//        RestangularProvider.setBaseUrl('http://localhost:8080/metadata/');
        RestangularProvider.setParentless(true);
        RestangularProvider.setDefaultHttpFields({withCredentials: true});
        RestangularProvider.setDefaultHeaders({'Content-Type': 'text/plain'});
        RestangularProvider.setResponseExtractor(function (response) {
            return response;
          });

        $locationProvider.html5Mode(true);

        // Parts
        $routeProvider.when('/part/list', {
            templateUrl: 'views/part/PartList.html',
            controller: 'PartListCtrl'
          });
        $routeProvider.when('/part/createByPartTypeId/:typeId', {
            templateUrl: 'views/part/PartForm.html',
            controller: 'PartFormCtrl',
            resolve: {
              partTypes: ['PartTypes', function(PartTypes) {
                return PartTypes.getPromise();
              }]
            }
          });
        $routeProvider.when('/part/:id/form', {
            templateUrl: 'views/part/PartForm.html',
            controller: 'PartFormCtrl'
          });
        $routeProvider.when('/part/:id/interchange/search', {
          templateUrl: 'views/part/interchange/PartInterchangeSearch.html',
          controller: 'PartInterchangeSearchCtrl'
        });
        $routeProvider.when('/part/Kit/:id/component/search', {
          templateUrl: '../views/part/KitComponentSearch.html',
          controller: 'KitComponentSearchCtrl'
        });
        $routeProvider.when('/part/:id/bom/search', {
            templateUrl: 'views/part/bom/PartBomSearch.html',
            controller: 'PartBomSearchCtrl'
        });
        $routeProvider.when('/part/:id/application/search', {
            templateUrl: 'views/part/application/PartApplicationSearch.html',
            controller: 'PartApplicationSearchCtrl'
        });
        $routeProvider.when('/part/:id/bom/:bomId/search', {
          templateUrl: 'views/part/bom/BomAlternateSearch.html',
          controller: 'BomAlternateSearchCtrl'
        });
        $routeProvider.when('/part/:id/ancestors', {
          templateUrl: 'views/part/PartAncestors.html',
          controller: 'PartAncestorsCtrl'
        });
        $routeProvider.when('/part/:id', {
          templateUrl: 'views/part/PartDetail.html',
          controller: 'PartDetailCtrl'
        });

        // Model Engine Year
        $routeProvider.when('/modelengineyear/list', {
            templateUrl: 'views/application/modelengineyear/list.html',
            controller: 'PartApplicationSearchCtrl'
          });
        $routeProvider.when('/application/:id', {
          templateUrl: 'views/application/ApplicationDetail.html',
          controller: 'ApplicationDetailCtrl'
        });

        // Turbo Models
        $routeProvider.when('/other/turboModels', {
          templateUrl: 'views/other/TurboModels.html',
          controller: 'TurboModelsCtrl'
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

        // My Account
        $routeProvider.when('/security/me', {
          templateUrl: 'views/security/my-account.html',
          controller: 'MyAccountCtrl'
        });

        // Password Reset
        $routeProvider.when('/password/reset/:token', {
          templateUrl: 'views/security/pwreset.html',
          controller: 'LoginCtrl'
        });

        // Default / Login
        $routeProvider.when('/', {
          templateUrl: 'views/security/login.html',
          controller: 'LoginCtrl'
        });

        $routeProvider.otherwise({
          redirectTo: '/'
        });
      });
