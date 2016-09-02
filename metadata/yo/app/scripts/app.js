"use strict";

angular.module("ngMetaCrudApp", ["ngRoute", "ngTable", "ui.bootstrap",
    "restangular", "dialogs.main", "gToast", "angucomplete-alt", "jsonFormatter",
  ])
  .constant("METADATA_BASE", "/metadata/")
  .constant("VALID_IP_ADDRESS_REGEX", /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/)
  .constant("VALID_HOSTNAME_REGEX", /^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\-]*[A-Za-z0-9])$/)
  .constant("DATE_FORMAT", "yyyy-MM-dd")
  .config(["$locationProvider", "$httpProvider", "$routeProvider", "RestangularProvider", "METADATA_BASE",
    function($locationProvider, $httpProvider, $routeProvider, RestangularProvider, METADATA_BASE) {

    $httpProvider.interceptors.push("loginRequiredInterceptor");

    RestangularProvider.setBaseUrl(METADATA_BASE);
    RestangularProvider.setParentless(true);
    RestangularProvider.setDefaultHttpFields({
      withCredentials: true
    });
    RestangularProvider.setDefaultHeaders({
      "Content-Type": "application/json"
    });
    RestangularProvider.setFullResponse(false);
    RestangularProvider.setResponseExtractor(function(response) {
      return response;
    });

    $locationProvider.html5Mode(true);

    // Parts
    $routeProvider.when("/part/list", {
      templateUrl: "views/part/PartList.html",
      controller: "PartListCtrl",
      resolve: {
        partTypes: ["restService", function(restService) {
          return restService.listPartTypes();
        }],
        critDimsByPartTypes: ["restService", function(restService) {
          return restService.getCritDimsByPartTypes("ID");
        }],
        critDimEnumVals: ["restService", function(restService) {
          return restService.getAllCritDimEnumVals();
        }]
      }
    });
    $routeProvider.when("/part/createByPartTypeId/:typeId", {
      templateUrl: "views/part/PartForm.html",
      controller: "PartFormCtrl",
      resolve: {
        part: function () {
          return null;
        },
        partType: ["$log", "$route", "restService", function($log, $route, restService) {
          var typeId = $route.current.pathParams.typeId;
          return restService.findPartType(typeId)
        }]
      }
    });
    $routeProvider.when("/part/:id/form", {
      templateUrl: "views/part/PartForm.html",
      controller: "PartFormCtrl",
      resolve: {
        part: ["$route", "restService", function ($route, restService) {
          var partId = $route.current.pathParams.id;
          return restService.findPart(partId);
        }],
        partType: function () {
          return null;
        }
      }
    });
    $routeProvider.when("/part/:id/interchange/search", {
      templateUrl: "views/part/interchange/PartInterchangeSearch.html",
      controller: "PartInterchangeSearchCtrl",
      resolve: {
        partTypes: ["restService", function(restService) {
          return restService.listPartTypes();
        }],
        critDimsByPartTypes: ["restService", function(restService) {
          return restService.getCritDimsByPartTypes("ID");
        }],
        critDimEnumVals: ["restService", function(restService) {
          return restService.getAllCritDimEnumVals();
        }]
      }
    });
    $routeProvider.when("/part/Kit/:id/component/search", {
      templateUrl: "../views/part/KitComponentSearch.html",
      controller: "KitComponentSearchCtrl",
      resolve: {
        partTypes: ["restService", function(restService) {
          return restService.listPartTypes();
        }]
      }
    });
    $routeProvider.when("/part/:id/bom/search", {
      templateUrl: "views/part/bom/PartBomSearch.html",
      controller: "PartBomSearchCtrl",
      resolve: {
        part: ["$route", "restService", function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        partTypes: ["restService", function(restService) {
          return restService.listPartTypes();
        }]
      }
    });
    $routeProvider.when("/part/:id/application/search", {
      templateUrl: "views/part/application/PartApplicationSearch.html",
      controller: "PartApplicationSearchCtrl"
    });
    $routeProvider.when("/part/:id/bom/:bomId/search", {
      templateUrl: "views/part/bom/BomAlternateSearch.html",
      controller: "BomAlternateSearchCtrl",
      resolve: {
        partTypes: ["restService", function(restService) {
          return restService.listPartTypes();
        }]
      }
    });
    $routeProvider.when("/part/:id/ancestors", {
      templateUrl: "views/part/PartAncestors.html",
      controller: "PartAncestorsCtrl"
    });
    $routeProvider.when("/part/:id", {
      templateUrl: "views/part/PartDetail.html",
      controller: "PartDetailCtrl",
      resolve: {
        part: ["$route", "restService", function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        criticalDimensions: ["$route", "restService", function($route, restService) {
          return restService.findCriticalDimensionsForThePart($route.current.pathParams.id);
        }]
      }
    });

    // Part Types
    $routeProvider.when("/parttype/list", {
      templateUrl: "views/parttype/list.html",
      controller: "PartTypeListCtrl",
      resolve: {
        partTypes: ["restService", function(restService) {
          return restService.listPartTypes();
        }]
      }
    });
    $routeProvider.when("/parttype/:id", {
      templateUrl: "views/parttype/edit.html",
      controller: "PartTypeEditCtrl",
      resolve: {
        partType: ["$route", "restService", function($route, restService) {
          return restService.findPartType($route.current.pathParams.id);
        }]
      }
    });

    // Part Sales Notes
    $routeProvider.when("/part/:id/sales_notes", {
      templateUrl: "views/part/sales_note/SalesNoteListByPart.html",
      controller: "SalesNoteListCtrl",
      resolve: {
        primaryPartId: ["$route", "restService", function($route, restService) {
          return restService.findPrimaryPartIdForThePart($route.current.pathParams.id);
        }]
      }
    });
    $routeProvider.when("/part/:id/sales_note/create", {
      templateUrl: "views/part/sales_note/SalesNoteCreate.html",
      controller: "SalesNoteCreateCtrl"
    });

    $routeProvider.when("/part/:partId/sales_note/:salesNoteId", {
      templateUrl: "views/part/sales_note/SalesNoteDetail.html",
      controller: "SalesNoteDetailCtrl",
      resolve: {
        part: ["$route", "restService", function($route, restService) {
          return restService.findPart($route.current.pathParams.partId);
        }],
        salesNote: ["$route", "restService", function($route, restService) {
          return restService.findSalesNote($route.current.pathParams.salesNoteId);
        }]
      }
    });

    $routeProvider.when("/part/:partId/sales_note/:salesNoteId/related_part/search", {
      templateUrl: "views/part/sales_note/SalesNoteAddRelatedPart.html",
      controller: "SalesNoteAddRelatedPartCtrl",
      resolve: {
        part: ["$route", "restService", function($route, restService) {
          return restService.findPart($route.current.pathParams.partId);
        }],
        salesNote: ["$route", "restService", function($route, restService) {
          return restService.findSalesNote($route.current.pathParams.salesNoteId);
        }],
        partTypes: ["restService", function(restService) {
          return restService.listPartTypes();
        }],
      }
    });

    $routeProvider.when("/application/carfueltype/list", {
      templateUrl: "views/application/carfueltype/list.html",
    });
    $routeProvider.when("/application/carfueltype/form", {
      templateUrl: "views/application/carfueltype/entity.html",
      controller: "CarFuelTypeFormCtrl"
    });
    $routeProvider.when("/application/carmake/list", {
      templateUrl: "views/application/carmake/list.html",
    });
    $routeProvider.when("/application/carmake/form", {
      templateUrl: "views/application/carmake/entity.html",
      controller: "CarMakeFormCtrl"
    });

    $routeProvider.when("/application/carmodel/list", {
      templateUrl: "views/application/carmodel/list.html",
    });
    $routeProvider.when("/application/carmodel/form", {
      templateUrl: "views/application/carmodel/entity.html",
      controller: "CarModelFormCtrl"
    });
    $routeProvider.when("/application/carmodel/:id/form", {
      templateUrl: "views/application/carmodel/entity.html",
      controller: "CarModelFormCtrl"
    });
    $routeProvider.when("/application/carmodelengineyear/list", {
      templateUrl: "views/application/carmodelengineyear/list.html",
    });
    $routeProvider.when("/application/carmodelengineyear/form", {
      templateUrl: "views/application/carmodelengineyear/entity.html",
      controller: "CarModelEngineYearFormCtrl",
      resolve: {
        carEngines: ["restService", function(restService) {
          return restService.findAllCarEnginesOrderedByName();
        }],
        carMakes: ["restService", function(restService) {
          return restService.findAllCarMakesOrderedByName();
        }],
        carModelEngineYear: function() {
          return null;
        }
      }
    });
    $routeProvider.when("/application/carmodelengineyear/:id/form", {
      templateUrl: "views/application/carmodelengineyear/entity.html",
      controller: "CarModelEngineYearFormCtrl",
      resolve: {
        // TODO
        carEngines: ["restService", function(restService) {
          return restService.findAllCarEnginesOrderedByName();
        }],
        carMakes: ["restService", function(restService) {
          return restService.findAllCarMakesOrderedByName();
        }],
        carModelEngineYear: ["$route", "restService", function($route, restService) {
          return restService.findCarmodelengineyear($route.current.pathParams.id);
        }]
      }
    });
    $routeProvider.when("/application/carmodelengineyear/:id", {
      templateUrl: "views/application/carmodelengineyear/view.html",
      controller: "CarmodelengineyearViewCtrl"
    });

    $routeProvider.when("/application/carengine/list", {
      templateUrl: "views/application/carengine/list.html",
    });
    $routeProvider.when("/application/carengine/form", {
      templateUrl: "views/application/carengine/entity.html",
      controller: "CarEngineFormCtrl"
    });
    $routeProvider.when("/application/carengine/:id/form", {
      templateUrl: "views/application/carengine/entity.html",
      controller: "CarEngineFormCtrl"
    });

    // List All Sales Notes
    $routeProvider.when("/other/salesNotes", {
      templateUrl: "views/other/SalesNoteListAll.html",
      controller: "SalesNoteListCtrl",
      resolve: {
        primaryPartId: function() { return null; }
      }
    });

    // Turbo Models
    $routeProvider.when("/other/turboModels", {
      templateUrl: "views/other/TurboModels.html",
      controller: "TurboModelsCtrl"
    });

    // MAS90
    $routeProvider.when("/mas90/sync/status", {
      templateUrl: "views/mas90/sync/status.html",
      controller: "Mas90SyncCtrl",
      resolve: {
        status: ["restService", function(restService) {
          return restService.statusMas90Sync();
        }]
      }
    });

    // Indexing.
    $routeProvider.when("/search/indexing/status", {
      templateUrl: "views/indexing/status.html",
      controller: "IndexingCtrl as ctrl",
      resolve: {
        status: ["restService", function(restService) {
          return restService.getIndexingStatus();
        }]
      }
    });

    // Critical dimensions
    $routeProvider.when("/criticaldimension/enums", {
      templateUrl: "views/criticaldimension/enums.html",
      controller: "CriticalDimensionEnumsCtrl",
      resolve: {
        critDimEnums: ["restService", function(restService) {
          return restService.getAllCritDimEnums()
        }]
      }
    });

    // Users and groups
    $routeProvider.when("/security/groups", {
      templateUrl: "views/security/groups.html",
      controller: "GroupsCtrl"
    });
    $routeProvider.when("/security/group/:id", {
      templateUrl: "views/security/group.html",
      controller: "GroupCtrl"
    });

    $routeProvider.when("/security/users", {
      templateUrl: "views/security/users.html",
      controller: "UsersCtrl",
      resolve: {
        users: ["restService", function(restService) { return restService.findActiveUsers()}]
      }
    });
    $routeProvider.when("/security/user/:id", {
      templateUrl: "views/security/user.html",
      controller: "UserCtrl",
      resolve: {
        authProviders: ["restService", function(restService) {
          return restService.getAllAuthProviders("id", "asc", 0, 1000);
        }]
      }
    });
    // Chagelog.
    $routeProvider.when("/changelog/list", {
      templateUrl: "views/changelog/list.html",
      controller: "ChangelogListCtrl",
      resolve: {
        users: ["restService", function(restService) {
          return restService.findAllUsers();
        }]
      }
    });
    // Authentication providers.
    $routeProvider.when("/security/auth_providers", {
      templateUrl: "views/security/auth_providers.html",
      controller: "AuthProvidersCtrl"
    });
    $routeProvider.when("/security/auth_providers/create", {
      templateUrl: "views/security/auth_provider_create.html",
      controller: "AuthProviderFormCtrl"
    });
    // My Account
    $routeProvider.when("/security/me", {
      templateUrl: "views/security/my-account.html",
      controller: "MyAccountCtrl"
    });

    // Password Reset
    $routeProvider.when("/password/reset/:token", {
      templateUrl: "views/security/pwreset.html",
      controller: "LoginCtrl"
    });

    // Default / Login
    $routeProvider.when("/", {
      templateUrl: "views/security/login.html",
      controller: "LoginCtrl"
    });

    $routeProvider.otherwise({
      redirectTo: "/"
    });
  }]);
