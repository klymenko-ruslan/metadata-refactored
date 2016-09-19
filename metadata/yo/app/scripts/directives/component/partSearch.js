"use strict";

angular.module("ngMetaCrudApp")
  .directive("partSearch", ["$log", "restService", function($log, restService) {
    return {
      restrict: "E",
      scope: {
        partTypes: "=",
        searchPartType: "="
        //selectedPartType: "="
      },
      replace: true,
      templateUrl: "/views/component/PartSearch.html",
      transclude: true,
      controller: ["$transclude", "$parse", "$sce", "$log", "$q", "$location",
                   "$scope", "ngTableParams", "utils",
        function($transclude, $parse, $sce, $log, $q, $location, $scope, ngTableParams, utils) {
        $scope.critDimEnumValsMap = _.indexBy($scope.critDimEnumVals, "id");
        // Filter
        /*
        $scope.searchPartType = null;
        if (angular.isObject($scope.selectedPartType)) {
          $scope.searchPartType = selectedPartType;
        }
        */
        $scope.searchInactive = null;
        $scope.searchManufacturer = null;
        $scope.searchTurboModel = null;
        $scope.searchTurboType = null;
        $scope.searchName = null;
        $scope.searchPartNumber = null;
        $scope.searchCritDims = {};

        $scope.stateItems = [];

        $scope.actions = utils.transclude2html($transclude);
        $scope.showCriticalDimensions = false;

        $scope.fixedCols = [
          {
            title: "Part Type",
            getter: $parse("_source.partType.name"),
            sortable: "partType.name.lower_case_sort"
          },
          {
            title: "Manufacturer",
            getter: $parse("_source.manufacturer.name"),
            sortable: "manufacturer.name.lower_case_sort"
          },
          {
            title: "Mfr Part #",
            cssClass: ['text-nowrap'],
            getter: $parse("_source.manufacturerPartNumber"),
            sortable: "manufacturerPartNumber.lower_case_sort"
          },
          {
            title: "Name",
            getter: $parse("_source.name"),
            sortable: "name.lower_case_sort"
          }
        ];

        $scope.actionsCol = [
          {
            title: "Action",
            cssClass: ['actions', 'text-center'],
            getter: function(part) {
              return $scope.actions;
            }
          }
        ];

        $scope.columns = null;

        $scope.isCritDimsAvailable = function() {
          return angular.isObject($scope.critDimsByPartTypes) && !jQuery.isEmptyObject($scope.critDimsByPartTypes);
        };

        $scope.initColumns = function() {
          if ($scope.showCriticalDimensions) {
            var critDimCols = [];
            if ($scope.isCritDimsAvailable()) {
              _.each($scope.critDims, function (d) {
                var gttr = null;
                var srtbl = null;
                if (d.dataType == 'ENUMERATION') {
                  gttr = $parse("_source." + d.idxName + "Label");
                  srtbl = d.idxName + "Label.lower_case_sort";
                } else {
                  gttr = $parse("_source." + d.idxName);
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

        $scope.$watch("[showCriticalDimensions]", function(newVal, oldVal) {
          $scope.initColumns();
        });

        $scope.onTurboModelChanged = function(val) {
          if (val !== $scope.searchTurboModel) {
            $scope.searchTurboModel = val;
            $scope.partTableParams.reload();
          }
        };

        $scope.onTurboModelSelected = function($item) {
          if ($item !== undefined) {
            $scope.onTurboModelChanged($item.title);
          }
        };

        $scope.onTurboTypeChanged = function(val) {
          if (val !== $scope.searchTurboType) {
            $scope.searchTurboType = val;
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
        $scope.partTableParams = new ngTableParams({
          page: 1,
          count: 10,
          sorting: {}
        }, {
          getData: function($defer, params) {
            // Update the pagination info
            var offset = params.count() * (params.page() - 1);
            var limit = params.count();
            var sortProperty, sortOrder;
            for (sortProperty in params.sorting()) break;
            if (sortProperty) {
              sortOrder = params.sorting()[sortProperty];
            }
            // Values in the filter for 'Turbo Model' and 'Turbo Type'
            // have sens only when current part type is 'Turbo'.
            var turboModelName = null;
            var turboTypeName = null;
            var searchPartTypeId = $scope.searchPartType ? $scope.searchPartType.id : null;
            if (searchPartTypeId === 1) { // 1 is ID of Turbo
              turboModelName = $scope.searchTurboModel;
              turboTypeName = $scope.searchTurboType;
            }

            restService.filterParts(searchPartTypeId, $scope.searchManufacturer, $scope.searchName,
              $scope.searchPartNumber, $scope.searchInactive, turboModelName, turboTypeName,
              $scope.searchCritDims, sortProperty, sortOrder, offset, limit).then(
              function(filtered) { // The 'filtered' is a JSON returned by ElasticSearch.
                $scope.searchResults = filtered;
                // Update values for UI combobox -- "State".
                $scope.stateItems = [];
                _.each(filtered.aggregations.State.buckets, function(b) {
                  if (b.key === 0) {
                    $scope.stateItems.push({
                      name: "Active",
                      val: false,
                      count: b.doc_count
                    });
                  } else if (b.key == 1) {
                    $scope.stateItems.push({
                      name: "Inactive",
                      val: true,
                      count: b.doc_count
                    });
                  }
                });
                // Update the total and slice the result
                $defer.resolve($scope.searchResults.hits.hits);
                params.total($scope.searchResults.hits.total);
              },
              function(errorResponse) {
                $log.log("Parts search failed: " + errorResponse);
                $defer.reject();
              }
            );
          }
        });

        $scope.clearFilter = function() {
          $scope.searchPartNumber = null;
          $scope.searchInactive = null;
          $scope.searchPartType = null;
          $scope.searchManufacturer = null;
          $scope.searchName = null;
          $scope.$broadcast("angucomplete-alt:clearInput", "fltrTurboModel");
          $scope.$broadcast("angucomplete-alt:clearInput", "fltrTurboType");
        };

        $scope.clearFilter();

        // Critical dimensions for the current choose $scope.searchPartType.
        $scope.critDims = null;

        $scope.$watch("[searchPartNumber, searchInactive, searchManufacturer, searchName, searchCritDims, " +
          "searchTurboType]", function(newVal, oldVal)
        {
          // Debounce
          if (angular.equals(newVal, oldVal, true)) {
            return;
          }
          $scope.partTableParams.reload();
        }, true);

        // Watch a change of $scope.searchPartType
        // and initialize $scope.critDims by critical dimensions which are
        // corresponding the part type.
        $scope.$watch("[searchPartType]", function(newVal, oldVal) {
          // Debounce
          if (angular.equals(newVal, oldVal, true)) {
            return;
          }
          if ($scope.isCritDimsAvailable()) {
            var pt = newVal[0];
            if (angular.isObject(pt)) {
              $scope.critDims = $scope.critDimsByPartTypes[pt.id];
            } else {
              $scope.critDims = null;
            }
            $scope.showCriticalDimensions = false;
            $scope.searchCritDims = {}; // re-init
          }
          $scope.partTableParams.reload();
        }, true);

        $scope.onPressedEnter = function() {
          if ($scope.searchResults.hits.total === 1) {
            var rec = $scope.searchResults.hits.hits[0]._source;
            var partId = rec.id;
            $location.path("/part/" + partId);
          };
        };

      }]
    };
  }]);
