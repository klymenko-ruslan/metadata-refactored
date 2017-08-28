'use strict';

angular.module('ngMetaCrudApp')
  .directive('partSearch', ['$log', 'restService', function($log, restService) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/views/component/PartSearch.html',
      transclude: true,
      link: function(scope, element, attrs) {
        if (attrs.fltrInitManufacturer != undefined && attrs.fltrInitManufacturer != null && attrs.fltrInitManufacturer != "") {
          scope.fltrPart.manufacturer = attrs.fltrInitManufacturer;
        }
      },
      controller: ['$transclude', '$parse', '$sce', '$log', '$q', '$location',
                   '$scope', 'NgTableParams', 'utils',
        function($transclude, $parse, $sce, $log, $q, $location, $scope, NgTableParams, utils) {

        $scope.critDimEnumValsMap = _.indexBy($scope.critDimEnumVals, 'id');

        // Filter
        $scope.fltrPart = {
          partType: null,
          inactive: null,
          manufacturer: null,
          turboModel: null,
          turboType: null,
          name: null,
          partNumber: null,

          year: null,
          make: null,
          model: null,
          engine: null,
          fuelType: null,

          critDims: null
        };

        $scope.fltrGroupState = {
          basicAttrsOpened: true,
          turboAttrsOpened: false,
          appAttrsOpened: false,
          critDimsOpened: false
        };

        $scope.columns = null;

        $scope.stateItems = [];

        // Critical dimensions for the current choose $scope.fltrPart.partType.
        $scope.critDims = null;

        $scope.showCriticalDimensions = false;

        $scope.actions = utils.transclude2html($transclude);

        $scope.fixedCols = [
          {
            title: 'Part Type',
            getter: $parse('_source.partType.name'),
            sortable: 'partType.name.lower_case_sort'
          },
          {
            title: 'Manufacturer',
            getter: $parse('_source.manufacturer.name'),
            sortable: 'manufacturer.name.lower_case_sort'
          },
          {
            title: 'Mfr Part #',
            cssClass: ['text-nowrap'],
            getter: $parse('_source.manufacturerPartNumber'),
            sortable: 'manufacturerPartNumber.lower_case_sort'
          },
          {
            title: 'Name',
            getter: $parse('_source.name'),
            sortable: 'name.lower_case_sort'
          }
        ];

        $scope.actionsCol = [
          {
            title: 'Action',
            cssClass: ['actions', 'text-center'],
            getter: function(/*part*/) {
              return $scope.actions;
            }
          }
        ];

        $scope.isCritDimsAvailable = function() {
          return angular.isObject($scope.critDimsByPartTypes) && !jQuery.isEmptyObject($scope.critDimsByPartTypes);
        };

        $scope.isCritDimsForCurrentPartTypeAvailable = function() {
          return angular.isObject($scope.fltrPart.partType) &&
                angular.isObject($scope.critDims) && $scope.critDims.length;
        };

        $scope.initColumns = function() {
          if ($scope.showCriticalDimensions) {
            var critDimCols = [];
            if ($scope.isCritDimsAvailable()) {
              _.each($scope.critDims, function (d) {
                var gttr = null;
                var srtbl = null;
                if (d.dataType === 'ENUMERATION') {
                  gttr = $parse('_source.' + d.idxName + 'Label');
                  srtbl = d.idxName + 'Label.lower_case_sort';
                } else {
                  gttr = $parse('_source.' + d.idxName);
                  srtbl = d.idxName;
                }
                var col = {
                  title: d.name,
                  getter: gttr,
                  sortable: srtbl
                };
                critDimCols.push(col);
              });
            }
            $scope.columns = Array.prototype.concat($scope.fixedCols, critDimCols, $scope.actionsCol);
          } else {
            $scope.columns = Array.prototype.concat($scope.fixedCols, $scope.actionsCol);
          }
        };

        $scope.initColumns();

        $scope.$watch('[showCriticalDimensions]', function(/*newVal, oldVal*/) {
          $scope.initColumns();
        });

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

        // Latest Results
        $scope.searchResults = {
          hits: {
            total: 0,
            hits: []
          }
        };
        // Part Table
        $scope.partTableParams = new NgTableParams({
          page: 1,
          count: 10,
          sorting: {}
        }, {
          getData: function(params) {
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

            return restService.filterParts(searchPartTypeId, $scope.fltrPart.manufacturer, $scope.fltrPart.name,
              $scope.fltrPart.partNumber, $scope.fltrPart.inactive, turboModelName, turboTypeName,
              $scope.fltrPart.year, $scope.fltrPart.make, $scope.fltrPart.model,
              $scope.fltrPart.engine, $scope.fltrPart.fuelType,
              $scope.fltrPart.critDims, sortProperty, sortOrder, offset, limit).then(
              function(filtered) { // The 'filtered' is a JSON returned by ElasticSearch.
                $scope.searchResults = filtered;
                // Update values for UI combobox -- 'State'.
                $scope.stateItems = [];
                _.each(filtered.aggregations.State.buckets, function(b) {
                  if (b.key === 0) {
                    $scope.stateItems.push({
                      name: 'Active',
                      val: false,
                      // jscs:disable requireCamelCaseOrUpperCaseIdentifiers
                      count: b.doc_count
                      // jscs:enable requireCamelCaseOrUpperCaseIdentifiers
                    });
                  } else if (b.key === 1) {
                    $scope.stateItems.push({
                      name: 'Inactive',
                      val: true,
                      // jscs:disable requireCamelCaseOrUpperCaseIdentifiers
                      count: b.doc_count
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
                params.total($scope.searchResults.hits.total);
                return $scope.searchResults.hits.hits;
              },
              function(errorResponse) {
                $log.log('Parts search failed: ' + errorResponse);
              }
            );
          }
        });

        $scope.clearFilter = function() {

          $scope.fltrPart.partType = null;
          $scope.fltrPart.inactive = null;
          $scope.fltrPart.manufacturer = null;
          $scope.fltrPart.turboModel = null;
          $scope.fltrPart.turboType = null;
          $scope.fltrPart.name = null;
          $scope.fltrPart.partNumber = null;
          $scope.fltrPart.critDims = null;

          $scope.fltrPart.year = null;
          $scope.fltrPart.make = null;
          $scope.fltrPart.model = null;
          $scope.fltrPart.engine = null;
          $scope.fltrPart.fuelType = null;

          $scope.fltrGroupState.turboAttrsOpened = false;
          $scope.fltrGroupState.appAttrsOpened = false;
          $scope.fltrGroupState.critDimsOpened = false;

          $scope.$broadcast('angucomplete-alt:clearInput', 'fltrTurboModel');
          $scope.$broadcast('angucomplete-alt:clearInput', 'fltrTurboType');

        };

        $scope.clearFilter();

        $scope.$watch('[fltrPart.partNumber, fltrPart.inactive, fltrPart.manufacturer, ' +
          'fltrPart.name, fltrPart.critDims]', function(newVal, oldVal)
        {
          // Debounce
          if (angular.equals(newVal, oldVal, true)) {
            return;
          }
          $scope.partTableParams.reload();
        }, true);

        // Watch a change of $scope.fltrPart.partType
        // and initialize $scope.critDims by critical dimensions which are
        // corresponding the part type.
        $scope.$watch('[fltrPart.partType]', function(newVal, oldVal) {

          // Debounce
          if (angular.equals(newVal, oldVal, true)) {
            return;
          }

          if ($scope.isCritDimsAvailable()) {

            var pt = newVal[0];

            if (angular.isObject(pt)) {
              $scope.critDims = $scope.critDimsByPartTypes[pt.id];
              if (pt.id !== 1) {  // Not a Turbo
                $scope.fltrPart.turboModel = null;
                $scope.fltrPart.turboType = null;
                $scope.fltrGroupState.turboAttrsOpened = false;
              } else { // Turbo
                $scope.fltrGroupState.turboAttrsOpened = true;
              }
            } else {
              $scope.critDims = null;
            }

            $scope.showCriticalDimensions = false;
            $scope.fltrPart.critDims = null; // re-init

            $scope.fltrGroupState.critDimsOpened = angular.isObject($scope.critDims) && $scope.critDims.length > 0;

          }

          $scope.partTableParams.reload();

        }, true);

        $scope.onPressedEnter = function() {
          if ($scope.searchResults.hits.total === 1) {
            var rec = $scope.searchResults.hits.hits[0]._source;
            var partId = rec.id;
            $location.path('/part/' + partId);
          }
        };

      }]
    };
  }]);
