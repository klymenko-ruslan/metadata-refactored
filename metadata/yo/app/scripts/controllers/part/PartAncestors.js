'use strict';

angular.module('ngMetaCrudApp')
.controller('PartAncestorsCtrl', ['$log', '$routeParams', '$location',
    '$scope', 'restService', 'NgTableParams', 'part', 'partTypes',
  function($log, $routeParams, $location, $scope, restService, NgTableParams,
    part, partTypes)
  {

    $scope.partId = $routeParams.id;
    $scope.part = part;
    $scope.partTypes = partTypes;

    // Filter
    $scope.fltrPart = {
      partType: null,
      inactive: null,
      manufacturer: null,
      turboModel: null,
      turboType: null,
      name: null,
      interchangeParts: null,
      relationDistance: null,
      relationType: null,
      partNumber: null,
      year: null,
      make: null,
      model: null,
      engine: null,
      fuelType: null,
    };

    $scope.fltrGroupState = {
      basicAttrsOpened: true,
      turboAttrsOpened: false,
      appAttrsOpened: false
    };

    $scope.searchResults = null;

    $scope.clearFilter = function() {

      $scope.fltrPart.partType = null;
      $scope.fltrPart.inactive = null;
      $scope.fltrPart.manufacturer = null;
      $scope.fltrPart.turboModel = null;
      $scope.fltrPart.turboType = null;
      $scope.fltrPart.name = null;
      $scope.fltrPart.interchangeParts = null;
      $scope.relationDistance = null;
      $scope.relationType = null;
      $scope.fltrPart.partNumber = null;

      $scope.fltrPart.year = null;
      $scope.fltrPart.make = null;
      $scope.fltrPart.model = null;
      $scope.fltrPart.engine = null;
      $scope.fltrPart.fuelType = null;

      $scope.fltrGroupState.turboAttrsOpened = false;
      $scope.fltrGroupState.appAttrsOpened = false;

      $scope.$broadcast('angucomplete-alt:clearInput', 'fltrTurboModel');
      $scope.$broadcast('angucomplete-alt:clearInput', 'fltrTurboType');

    };

    $scope.onPressedEnter = function() {
      if ($scope.searchResults.hits.total === 1) {
        var rec = $scope.searchResults.hits.hits[0]._source;
        var partId = rec.id;
        $location.path('/part/' + partId);
      }
    };

    $scope.onCmeyYearChanged = function(val) {
      if (val !== $scope.fltrPart.year) {
        $scope.fltrPart.year = val;
        $scope.partTableParams.reload();
      }
    };

    $scope.onCmeyYearSelected = function($item) {
      if ($item !== undefined) {
        $scope.onCmeyYearChanged($item.title);
      }
    };

    $scope.onCmeyMakeChanged = function(val) {
      if (val !== $scope.fltrPart.make) {
        $scope.fltrPart.make = val;
        $scope.partTableParams.reload();
      }
    };

    $scope.onCmeyMakeSelected = function($item) {
      if ($item !== undefined) {
        $scope.onCmeyMakeChanged($item.title);
      }
    };

    $scope.onCmeyModelChanged = function(val) {
      if (val !== $scope.fltrPart.model) {
        $scope.fltrPart.model = val;
        $scope.partTableParams.reload();
      }
    };

    $scope.onCmeyModelSelected = function($item) {
      if ($item !== undefined) {
        $scope.onCmeyModelChanged($item.title);
      }
    };

    $scope.onCmeyEngineChanged = function(val) {
      if (val !== $scope.fltrPart.engine) {
        $scope.fltrPart.engine = val;
        $scope.partTableParams.reload();
      }
    };

    $scope.onCmeyEngineSelected = function($item) {
      if ($item !== undefined) {
        $scope.onCmeyEngineChanged($item.title);
      }
    };

    $scope.onCmeyFuelTypeChanged = function(val) {
      if (val !== $scope.fltrPart.fuelType) {
        $scope.fltrPart.fuelType = val;
        $scope.partTableParams.reload();
      }
    };

    $scope.onCmeyFuelTypeSelected = function($item) {
      if ($item !== undefined) {
        $scope.onCmeyFuelTypeChanged($item.title);
      }
    };

    $scope.onTurboModelChanged = function(val) {
      if (val !== $scope.fltrPart.turboModel) {
        $scope.fltrPart.turboModel = val;
        $scope.partTableParams.reload();
      }
    };

    $scope.onTurboModelSelected = function($item) {
      if ($item !== undefined) {
        $scope.onTurboModelChanged($item.title);
      }
    };

    $scope.onTurboTypeChanged = function(val) {
      if (val !== $scope.fltrPart.turboType) {
        $scope.fltrPart.turboType = val;
        $scope.partTableParams.reload();
      }
    };

    $scope.onTurboTypeSelected = function($item) {
      if ($item !== undefined) {
        $scope.onTurboTypeChanged($item.title);
      }
    };

    $scope.$watch('[fltrPart.partNumber, fltrPart.inactive, fltrPart.manufacturer, ' +
      'fltrPart.name, fltrPart.relationType, fltrPart.relationDistance, ' +
      'fltrPart.partType, fltrPart.interchangeParts]', function(newVal, oldVal)
    {
      // Debounce
      if (angular.equals(newVal, oldVal, true)) {
        return;
      }
      $scope.partTableParams.reload();
    }, true);

    $scope.partTableParams = new NgTableParams({
        page: 1,
        count: 25
      }, {
        getData: function (params) {
          // Update the pagination info
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          var sortProperty, sortOrder;
          for (sortProperty in params.sorting()) {
              break;
          }
          if (sortProperty) {
            sortOrder = params.sorting()[sortProperty];
          }
          // Values in the filter for 'Turbo Model' and 'Turbo Type'
          // have sens only when current part type is 'Turbo'.
          var turboModelName = null;
          var turboTypeName = null;
          var searchPartTypeId = $scope.fltrPart.partType ? $scope.fltrPart.partType.id : null;
          if (searchPartTypeId === 1) { // 1 is ID of Turbo
            turboModelName = $scope.fltrPart.turboModel;
            turboTypeName = $scope.fltrPart.turboType;
          }
          var searchRelationType = $scope.fltrPart.relationType ? $scope.fltrPart.relationType.id : null;

          return restService.filterAncestors($scope.partId, searchPartTypeId,
            $scope.fltrPart.manufacturer, $scope.fltrPart.name,
            $scope.fltrPart.interchangeParts, $scope.fltrPart.partNumber,
            $scope.fltrPart.relationDistance, searchRelationType,
            $scope.fltrPart.inactive, turboModelName, turboTypeName,
            $scope.fltrPart.year, $scope.fltrPart.make, $scope.fltrPart.model,
            $scope.fltrPart.engine, $scope.fltrPart.fuelType,
            sortProperty, sortOrder, offset, limit).then(
            function(result) { // The 'filtered' is a JSON returned by ElasticSearch.
              $scope.searchResults = result;
              // Update values for UI combobox -- 'State'.
              $scope.stateItems = [];
              _.each($scope.searchResults.aggregations.State, function(b) {
                if (b.key === 0) {
                  $scope.stateItems.push({
                    name: 'Active',
                    val: false,
                    // jscs:disable requireCamelCaseOrUpperCaseIdentifiers
                    count: b.count
                    // jscs:enable requireCamelCaseOrUpperCaseIdentifiers
                  });
                } else if (b.key === 1) {
                  $scope.stateItems.push({
                    name: 'Inactive',
                    val: true,
                    // jscs:disable requireCamelCaseOrUpperCaseIdentifiers
                    count: b.count
                    // jscs:enable requireCamelCaseOrUpperCaseIdentifiers
                  });
                }
              });

              if ($scope.defManufacturerName) {
                var manufacturerBucket = _.find($scope.searchResults.aggregations.Manufacturer.buckets,
                  function(r) { return r.key === $scope.defManufacturerName; }
                );
                if (angular.isObject(manufacturerBucket)) {
                  $scope.fltrPart.manufacturer = manufacturerBucket.key;
                }
                $scope.defManufacturerName = null;
              }

              // Update the total and slice the result
              params.total($scope.searchResults.ancestors.total);
              return $scope.searchResults.ancestors.recs;
            },
            function(errorResponse) {
              $log.log('Parts search failed: ' + errorResponse);
            }
          );
        }
      }
    );

    $scope.clearFilter();
    //$scope.partTableParams.reload();

  }]);
