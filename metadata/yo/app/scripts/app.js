'use strict';

angular.module('ngMetaCrudApp', ['ngRoute', 'ngTable', 'ui.bootstrap', 'restangular', 'dialogs.main', 'gToast'])
  .constant('METADATA_BASE', '/metadata/')
//  .constant('METADATA_BASE', 'http://192.168.42.10:8080/metadata/')
//  .constant('METADATA_BASE', 'http://localhost:8080/metadata/')
  .config(function ($locationProvider, $httpProvider, $routeProvider, RestangularProvider, METADATA_BASE) {
    $httpProvider.interceptors.push('loginRequiredInterceptor');
    
    RestangularProvider.setBaseUrl(METADATA_BASE);
    RestangularProvider.setParentless(true);
    RestangularProvider.setDefaultHttpFields({withCredentials: true});
    RestangularProvider.setDefaultHeaders({'Content-Type': 'application/json'});
    RestangularProvider.setFullResponse(false);
    RestangularProvider.setResponseExtractor(function(response) {
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
    
    
    // Part Sales Notes
    $routeProvider.when('/part/:id/sales_notes', {
      templateUrl: 'views/part/sales_note/SalesNoteListByPart.html',
      controller: 'SalesNoteListByPartCtrl'
    });
    $routeProvider.when('/part/:id/sales_note/create', {
      templateUrl: 'views/part/sales_note/SalesNoteCreate.html',
      controller: 'SalesNoteCreateCtrl'
    });
    $routeProvider.when('/part/:partId/sales_note/:salesNoteId', {
      templateUrl: 'views/part/sales_note/SalesNoteDetail.html',
      controller: 'SalesNoteDetailCtrl'
    });
    $routeProvider.when('/part/:partId/sales_note/:salesNoteId/related_part/search', {
      templateUrl: 'views/part/sales_note/SalesNoteAddRelatedPart.html',
      controller: 'SalesNoteAddRelatedPartCtrl'
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
