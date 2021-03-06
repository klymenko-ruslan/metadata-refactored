'use strict';

/* globals alert:false */

angular.module('ngMetaCrudApp', ['ngCookies', 'ngRoute', 'ngTable',
    'ui.bootstrap', 'restangular', 'dialogs.main',
    'dialogs.default-translations', 'toastr', 'angucomplete-alt',
    'jsonFormatter', 'angular-loading-bar', 'hc.marked', 'hljs',
    'angular-markdown-editor', 'angularjs-dropdown-multiselect',
    'daterangepicker'
  ])
  .constant('METADATA_BASE', '/metadata/')
  .constant('VALID_IP_ADDRESS_REGEX', /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/)
  .constant('VALID_HOSTNAME_REGEX', /^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\-]*[A-Za-z0-9])$/)
  .constant('ASCII_ONLY_REGEX', /^[\x20-\x7F]+$/)
  .constant('DATE_FORMAT', 'yyyy-MM-dd')
  .config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    // cfpLoadingBarProvider.spinnerTemplate = '<div><span class='fa fa-spinner'>Loading...</div>';
    cfpLoadingBarProvider.latencyThreshold = 5;
  }])
  .config(['toastrConfig', function(toastrConfig) {
    angular.extend(toastrConfig, {
      autoDismiss: true,
      containerId: 'toast-container',
      maxOpened: 3,
      newestOnTop: true,
      positionClass: 'toast-top-left',
      preventDuplicates: false,
      preventOpenDuplicates: true,
      target: '#toastr',
      allowHtml: false,
      closeButton: true,
      timeOut: 5000
    });
  }])
  .config(['markedProvider', 'hljsServiceProvider', function(markedProvider, hljsServiceProvider) {
    Dropzone.autoDiscover = false;
    // marked config
    markedProvider.setOptions({
      gfm: true,
      tables: true,
      sanitize: true,
      highlight: function (code, lang) {
        if (lang) {
          return hljs.highlight(lang, code, true).value;
        } else {
          return hljs.highlightAuto(code).value;
        }
      }
    });

    // highlight config
    hljsServiceProvider.setOptions({
      // replace tab with 4 spaces
      tabReplace: '    '
    });
  }])
  .filter('ifEmpty', function() {
    return function(input, defaultValue) {
      if (angular.isUndefined(input) || input === null || input === '') {
        return defaultValue;
      }
      return input;
    };
  })
  .config(['$locationProvider', '$httpProvider', '$routeProvider', 'RestangularProvider', 'METADATA_BASE',
    function($locationProvider, $httpProvider, $routeProvider, RestangularProvider, METADATA_BASE) {

    $httpProvider.interceptors.push('loginRequiredInterceptor');

    RestangularProvider.setBaseUrl(METADATA_BASE);
    RestangularProvider.setParentless(true);
    RestangularProvider.setDefaultHttpFields({
      withCredentials: true
    });
    RestangularProvider.setDefaultHeaders({
      'Content-Type': 'application/json'
    });
    RestangularProvider.setFullResponse(false);
    RestangularProvider.setResponseExtractor(function(response) {
      return response;
    });

    $locationProvider.html5Mode(true);

    // Parts
    $routeProvider.when('/part/list', {
      templateUrl: 'views/part/PartList.html',
      controller: 'PartListCtrl',
      resolve: {
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        critDimsByPartTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getCriticalDimensions();
        }],
        critDimEnumVals: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getCriticalDimensionsEnumsVals();
        }]
      }
    });
    $routeProvider.when('/part/createByPartTypeId/:typeId', {
      templateUrl: 'views/part/PartForm.html',
      controller: 'PartFormCtrl',
      resolve: {
        part: function () {
          return null;
        },
        partType: ['$log', '$route', 'restService', function($log, $route, restService) {
          var typeId = $route.current.pathParams.typeId;
          return restService.findPartType(typeId);
        }],
        manufacturers: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getManufacturers();
        }],
        services: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getServices();
        }]
      }
    });
    // TODO: it seems the entry below is obsolete and dont used anymore
    /*
    $routeProvider.when('/part/:id/form', {
      templateUrl: 'views/part/PartForm.html',
      controller: 'PartFormCtrl',
      resolve: {
        part: ['$route', 'restService', function ($route, restService) {
          var partId = $route.current.pathParams.id;
          return restService.findPart(partId);
        }],
        partType: function () {
          return null;
        },
        manufacturers: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getManufacturers();
        }],
        services: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getServices();
        }]
      }
    });
    */
    $routeProvider.when('/part/:id/interchange/search', {
      templateUrl: 'views/part/interchange/PartInterchangeSearch.html',
      controller: 'PartInterchangeSearchCtrl',
      resolve: {
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        critDimsByPartTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getCriticalDimensions();
        }],
        critDimEnumVals: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getCriticalDimensionsEnumsVals();
        }],
        services: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getServices();
        }]
      }
    });
    $routeProvider.when('/kitcomponent/:id/search/part', {
      templateUrl: '../views/part/KitComponentSearchPart.html',
      controller: 'KitComponentSearchPartCtrl',
      resolve: {
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        existingMappings: ['$route', 'restService', function($route, restService) {
          return restService.listKitComponentsByKitId($route.current.pathParams.id, 'part.manufacturerPartNumber', 'asc');
        }]
      }
    });
    $routeProvider.when('/kitcomponent/:id/search/kit', {
      templateUrl: '../views/part/KitComponentSearchKit.html',
      controller: 'KitComponentSearchKitCtrl',
      resolve: {
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        existingMappings: ['$route', 'restService', function($route, restService) {
          return restService.listKitComponentsByPartId($route.current.pathParams.id, 'kit.manufacturerPartNumber', 'asc');
        }]
      }
    });
    $routeProvider.when('/part/:id/bom/search', {
      templateUrl: 'views/part/bom/PartBomSearch.html',
      controller: 'PartBomSearchCtrl',
      resolve: {
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        boms: ['$log', '$route', 'BOM', function($log, $route, BOM) {
          return BOM.listByParentPartId($route.current.pathParams.id);
        }],
        services: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getServices();
        }]
      }
    });
    $routeProvider.when('/part/:id/parentbom/search', {
      templateUrl: 'views/part/bom/ParentBomSearch.html',
      controller: 'ParentBomSearchCtrl',
      resolve: {
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        parents:['$route', 'BOM', function ($route, BOM) {
          var partId = $route.current.pathParams.id;
          return BOM.listParentsOfPartBom(partId);
        }],
        services: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getServices();
        }]
      }
    });
    $routeProvider.when('/part/:id/turbo/search', {
      templateUrl: 'views/part/turbo/TurboSearch.html',
      controller: 'TurboSearchCtrl',
      resolve: {
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        turbos: ['$route', 'restService', function($route, restService) {
          return restService.listTurbosLinkedToGasketKit($route.current.pathParams.id);
        }]
      }
    });
    $routeProvider.when('/part/:id/application/search', {
      templateUrl: 'views/part/application/PartApplicationSearch.html',
      controller: 'PartApplicationSearchCtrl',
      resolve: {
        services: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getServices();
        }]
      }
    });
    $routeProvider.when('/part/:id/bom/:childId/alt/:altHeaderId', {
      templateUrl: 'views/part/bom/BomAlternateSearch.html',
      controller: 'BomAlternateSearchCtrl',
      resolve: {
        altHeaderId: ['$route', function($route) {
          return parseInt($route.current.pathParams.altHeaderId);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        selectedBom: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.childId);
        }],
        bom: ['$route', 'BOM', function($route, BOM) {
          return BOM.listByParentPartId($route.current.pathParams.id);
        }],
        altBom: ['$route', 'restService', function($route, restService) {
          return restService.getBomAlternatives($route.current.pathParams.id, $route.current.pathParams.childId);
        }]
      }
    });
    $routeProvider.when('/part/:id/gasketkit/search', {
      templateUrl: 'views/part/gasketkit/PartGasketKitSearch.html',
      controller: 'PartGasketKitSearchCtrl',
      resolve: {
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }]
      }
    });
    $routeProvider.when('/part/:id/ancestors', {
      templateUrl: 'views/part/PartAncestors.html',
      controller: 'PartAncestorsCtrl',
      resolve: {
        part: ['$route', 'restService', function ($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }]
      }
    });
    $routeProvider.when('/part/:id', {
      templateUrl: 'views/part/view/PartDetail.html',
      controller: 'PartDetailCtrl',
      resolve: {
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
        manufacturers: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getManufacturers();
        }]
      }
    });

    // Link standard/oversize parts.
    $routeProvider.when('/part/:id/oversize/add', {
      templateUrl: 'views/part/AddStandardOversize.html',
      controller: 'AddStandardOversizeCtrl',
      resolve: {
        type: function() {
          return 'oversize';
        },
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        existing: ['$route', 'restService', function($route, restService) {
          return restService.findOversizeParts($route.current.pathParams.id);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }]
      }
    });
    $routeProvider.when('/part/:id/standard/add', {
      templateUrl: 'views/part/AddStandardOversize.html',
      controller: 'AddStandardOversizeCtrl',
      resolve: {
        type: function() {
          return 'standard';
        },
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.id);
        }],
        existing: ['$route', 'restService', function($route, restService) {
          return restService.findStandardParts($route.current.pathParams.id);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }]
      }
    });

    // Part Types
    $routeProvider.when('/parttype/list', {
      templateUrl: 'views/parttype/list.html',
      controller: 'PartTypeListCtrl',
      resolve: {
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }]
      }
    });
    $routeProvider.when('/parttype/:id', {
      templateUrl: 'views/parttype/edit.html',
      controller: 'PartTypeEditCtrl',
      resolve: {
        partType: ['$route', 'restService', function($route, restService) {
          return restService.findPartType($route.current.pathParams.id);
        }]
      }
    });

    // Part Sales Notes
    $routeProvider.when('/part/:id/sales_notes', {
      templateUrl: 'views/part/sales_note/SalesNoteListByPart.html',
      controller: 'SalesNoteListCtrl',
      resolve: {
        primaryPartId: ['$route', 'restService', function($route, restService) {
          return restService.findPrimaryPartIdForThePart($route.current.pathParams.id);
        }]
      }
    });
    $routeProvider.when('/part/:id/sales_note/create', {
      templateUrl: 'views/part/sales_note/SalesNoteCreate.html',
      controller: 'SalesNoteCreateCtrl',
      resolve: {
        services: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getServices();
        }]
      }
    });

    $routeProvider.when('/part/:partId/sales_note/:salesNoteId', {
      templateUrl: 'views/part/sales_note/SalesNoteDetail.html',
      controller: 'SalesNoteDetailCtrl',
      resolve: {
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.partId);
        }],
        salesNote: ['$route', 'restService', function($route, restService) {
          return restService.findSalesNote($route.current.pathParams.salesNoteId);
        }]
      }
    });

    $routeProvider.when('/part/:partId/sales_note/:salesNoteId/related_part/search', {
      templateUrl: 'views/part/sales_note/SalesNoteAddRelatedPart.html',
      controller: 'SalesNoteAddRelatedPartCtrl',
      resolve: {
        part: ['$route', 'restService', function($route, restService) {
          return restService.findPart($route.current.pathParams.partId);
        }],
        salesNote: ['$route', 'restService', function($route, restService) {
          return restService.findSalesNote($route.current.pathParams.salesNoteId);
        }],
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }],
      }
    });

    $routeProvider.when('/application/carfueltype/list', {
      templateUrl: 'views/application/carfueltype/list.html',
    });
    $routeProvider.when('/application/carfueltype/form', {
      templateUrl: 'views/application/carfueltype/entity.html',
      controller: 'CarFuelTypeFormCtrl'
    });
    $routeProvider.when('/application/carmake/list', {
      templateUrl: 'views/application/carmake/list.html',
    });
    $routeProvider.when('/application/carmake/form', {
      templateUrl: 'views/application/carmake/entity.html',
      controller: 'CarMakeFormCtrl'
    });

    $routeProvider.when('/application/carmodel/list', {
      templateUrl: 'views/application/carmodel/list.html',
    });
    $routeProvider.when('/application/carmodel/form', {
      templateUrl: 'views/application/carmodel/entity.html',
      controller: 'CarModelFormCtrl',
      resolve: {
        carModel: function() {
          return null;
        },
        carMakes: ['restService', function (restService) {
          return restService.findAllCarMakesOrderedByName();
        }]
      }
    });
    $routeProvider.when('/application/carmodel/:id/form', {
      templateUrl: 'views/application/carmodel/entity.html',
      controller: 'CarModelFormCtrl',
      resolve: {
        carModel: ['$route', 'restService', function($route, restService) {
          return restService.findCarmodel($route.current.pathParams.id);
        }],
        carMakes: ['restService', function (restService) {
          return restService.findAllCarMakesOrderedByName();
        }]
      }
    });
    $routeProvider.when('/application/carmodelengineyear/list', {
      templateUrl: 'views/application/carmodelengineyear/list.html',
    });
    $routeProvider.when('/application/carmodelengineyear/form', {
      templateUrl: 'views/application/carmodelengineyear/entity.html',
      controller: 'CarModelEngineYearFormCtrl',
      resolve: {
        carEngines: ['restService', function(restService) {
          return restService.findAllCarEnginesOrderedByName();
        }],
        carMakes: ['restService', function(restService) {
          return restService.findAllCarMakesOrderedByName();
        }],
        carModelEngineYear: function() {
          return null;
        }
      }
    });
    $routeProvider.when('/application/carmodelengineyear/:id/form', {
      templateUrl: 'views/application/carmodelengineyear/entity.html',
      controller: 'CarModelEngineYearFormCtrl',
      resolve: {
        carEngines: ['restService', function(restService) {
          return restService.findAllCarEnginesOrderedByName();
        }],
        carMakes: ['restService', function(restService) {
          return restService.findAllCarMakesOrderedByName();
        }],
        carModelEngineYear: ['$route', 'restService', function($route, restService) {
          return restService.findCarmodelengineyear($route.current.pathParams.id);
        }]
      }
    });
    $routeProvider.when('/application/carmodelengineyear/:id', {
      templateUrl: 'views/application/carmodelengineyear/view.html',
      controller: 'CarmodelengineyearViewCtrl'
    });

    $routeProvider.when('/application/carengine/list', {
      templateUrl: 'views/application/carengine/list.html',
    });
    $routeProvider.when('/application/carengine/form', {
      templateUrl: 'views/application/carengine/entity.html',
      controller: 'CarEngineFormCtrl',
      resolve: {
        carEngine: function() {
          return null;
        },
        carFuelTypes: ['restService', function(restService) {
          return restService.findAllCarFuelTypesOrderedByName();
        }]
      }
    });
    $routeProvider.when('/application/carengine/:id/form', {
      templateUrl: 'views/application/carengine/entity.html',
      controller: 'CarEngineFormCtrl',
      resolve: {
        carEngine: ['$route', 'restService', function($route, restService) {
          return restService.findCarengine($route.current.pathParams.id);
        }],
        carFuelTypes: ['restService', function(restService) {
          return restService.findAllCarFuelTypesOrderedByName();
        }]
      }
    });

    // List All Sales Notes
    $routeProvider.when('/other/salesNotes', {
      templateUrl: 'views/other/SalesNoteListAll.html',
      controller: 'SalesNoteListCtrl',
      resolve: {
        primaryPartId: function() { return null; }
      }
    });

    // Turbo Models
    $routeProvider.when('/other/turboModels', {
      templateUrl: 'views/other/TurboModels.html',
      controller: 'TurboModelsCtrl'
    });

    // Applications and Turbos
    $routeProvider.when('/other/appsturbos', {
      templateUrl: 'views/other/appsturbos/main.html',
      controller: 'AppsTurbosCtrl',
      resolve: {
        partTypes: ['cachedDictionaries', function(cachedDictionaries) {
          return cachedDictionaries.getPartTypes();
        }]
      }
    });

    // Changelog Sources Names
    $routeProvider.when('/changelog/source/name/list', {
      templateUrl: 'views/chlogsrc/name/list.html',
      controller: 'ChangelogSourcesNamesListCtrl'
    });

    // Changelog sources
    $routeProvider.when('/changelog/source/list', {
      templateUrl: 'views/chlogsrc/list.html',
      controller: 'ChangelogSourcesListCtrl',
      resolve: {
        'sourcesNames': ['restService', function(restService) {
          return restService.getAllChangelogSourceNames();
        }]
      }
    });
    $routeProvider.when('/changelog/source/create', {
      templateUrl: 'views/chlogsrc/form.html',
      controller: 'ChangelogSourcesFormCtrl',
      resolve: {
        begin: ['restService', function(restService) {
          return restService.changelogSourceBeginEdit(); // needs to clear session attribute on the server side
        }],
        'sourcesNames': ['restService', function(restService) {
          return restService.getAllChangelogSourceNames();
        }],
        source: [function() {
          return null;
        }]
      }
    });
    $routeProvider.when('/changelog/source/:id/form', {
      templateUrl: 'views/chlogsrc/form.html',
      controller: 'ChangelogSourcesFormCtrl',
      resolve: {
        begin: ['$route', 'restService', function($route, restService) {
          return restService.changelogSourceBeginEdit($route.current.pathParams.id); // needs to clear session attribute on the server side
        }],
        'sourcesNames': ['restService', function(restService) {
          return restService.getAllChangelogSourceNames();
        }],
        source: ['$route', 'restService', function($route, restService) {
          return restService.findChangelogSourceById($route.current.pathParams.id);
        }]
      }
    });
    $routeProvider.when('/changelog/source/:id', {
      templateUrl: 'views/chlogsrc/view.html',
      controller: 'ChangelogSourcesViewCtrl',
      resolve: {
        source: ['$route', 'restService', function($route, restService) {
          return restService.findChangelogSourceById($route.current.pathParams.id);
        }]
      }
     });

    // MAS90
    $routeProvider.when('/mas90/sync/status', {
      templateUrl: 'views/mas90/sync/status.html',
      controller: 'Mas90SyncCtrl',
      resolve: {
        status: ['restService', function(restService) {
          return restService.statusMas90Sync();
        }]
      }
    });

    // Indexing.
    $routeProvider.when('/search/indexing/status', {
      templateUrl: 'views/indexing/status.html',
      controller: 'IndexingCtrl as ctrl',
      resolve: {
        status: ['restService', function(restService) {
          return restService.getIndexingStatus();
        }]
      }
    });

    // BOM rebuild and indexing.
    $routeProvider.when('/bom/rebuild/status', {
      templateUrl: 'views/bom/status.html',
      controller: 'BomRebuildCtrl as ctrl',
      resolve: {
        status: ['restService', function(restService) {
          return restService.getBomRebuildingStatus();
        }]
      }
    });

    // Critical dimensions
    $routeProvider.when('/criticaldimension/enums', {
      templateUrl: 'views/criticaldimension/enums.html',
      controller: 'CriticalDimensionEnumsCtrl',
      resolve: {
        critDimEnums: ['restService', function(restService) {
          return restService.getAllCritDimEnums();
        }]
      }
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
      controller: 'UsersCtrl',
      resolve: {
        authProviders: ['restService', function(restService) {
          return restService.getAllAuthProviders('id', 'asc', 0, 1000);
        }]
      }
    });
    $routeProvider.when('/security/user/:id', {
      templateUrl: 'views/security/user/view.html',
      controller: 'UserCtrl',
      resolve: {
        authProviders: ['restService', function(restService) {
          return restService.getAllAuthProviders('id', 'asc', 0, 1000);
        }]
      }
    });

    // Chagelog.
    $routeProvider.when('/changelog/list', {
      templateUrl: 'views/changelog/list.html',
      controller: 'ChangelogListCtrl',
      resolve: {
        users: ['restService', function(restService) {
          return restService.findAllUsers();
        }]
      }
    });

    // Manufacturers.
    $routeProvider.when('/manufacturer/list', {
      templateUrl: 'views/manufacturer/list.html',
      controller: 'ManufacturerListCtrl',
      resolve: {
        manufacturerTypes: ['restService', function(restService) {
          return restService.listManufacturerTypes();
        }]
      }
    });

    // Services.
    $routeProvider.when('/service/list', {
      templateUrl: 'views/service/list.html',
      controller: 'ServiceListCtrl'
    });

    // Authentication providers.
    $routeProvider.when('/security/auth_providers', {
      templateUrl: 'views/security/auth_providers.html',
      controller: 'AuthProvidersCtrl'
    });
    $routeProvider.when('/security/auth_providers/create', {
      templateUrl: 'views/security/auth_provider_create.html',
      controller: 'AuthProviderFormCtrl'
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
      redirectTo: '/login'
    });

  }])
  .run(['$rootScope', '$log', function($rootScope, $log) {
    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
      $log.error('Encountered internal error: ');
      $log.error(error);
      alert('Error: ' + angular.toJson(error, 2));
    });

  }]);
