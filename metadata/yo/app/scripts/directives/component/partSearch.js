"use strict";

angular.module("ngMetaCrudApp")
  .directive("partSearch", ["$log", "restService", function($log, restService) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "/views/component/PartSearch.html",
      transclude: true,
      link: function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
        controller.transcludeActionsFn = transcludeFn;
      },
      controller: ["$parse", "$sce", "$log", "$q", "$location", "$scope", "ngTableParams",
        function($parse, $sce, $log, $q, $location, $scope, ngTableParams) {

        $scope.critDimEnumValsMap = _.indexBy($scope.critDimEnumVals, "id");
        // Filter
        $scope.searchPartType = null;
        $scope.searchManufacturer = null;
        $scope.searchTurboModel = null;
        $scope.searchTurboType = null;
        $scope.searchName = null;
        $scope.searchPartNumber = null;
        $scope.searchCritDims = {};

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
              var partId = part._source.id;
              var linkView = "<a authorize=\"ROLE_READ\" " +
                "href=\"/part/" + partId + "\" " +
                "class=\"btn btn-primary btn-xs\"> " +
                "<i class=\"fa fa-eye\"></i> View</a>";
              var linkEdit = "<a authorize=\"ROLE_ALTER_PART\" " +
                "href=\"/part/" + partId + "/form\" " +
                "class=\"btn btn-primary btn-xs\"> " +
                "<i class=\"fa fa-cog\"></i> Edit</a>";
              var html = linkView + "\n" + linkEdit;
              return $sce.trustAsHtml(html);
            }
          }
        ];

        $scope.columns = null;

        $scope.initColumns = function() {
          if ($scope.showCriticalDimensions) {
            var critDimCols = [];
            if ($scope.critDims) {
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
            if ($scope.searchPartType === "Turbo") {
              turboModelName = $scope.searchTurboModel;
              turboTypeName = $scope.searchTurboType;
            }
            restService.filterParts($scope.searchPartType, $scope.searchManufacturer, $scope.searchName,
              $scope.searchPartNumber, turboModelName, turboTypeName,
              $scope.searchCritDims, sortProperty, sortOrder, offset, limit).then(
              function(filtered) { // The 'filtered' is a JSON returned by ElasticSearch.
                $scope.searchResults = filtered;
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
          $scope.searchPartType = null;
          $scope.searchManufacturer = null;
          $scope.searchName = null;
          $scope.searchTurboModel = null;
          $scope.searchTurboType = null;
        };

        $scope.clearFilter();

        // Critical dimensions for the current choose $scope.searchPartType.
        $scope.critDims = null;

        $scope.$watch("[searchPartNumber, searchManufacturer, searchName, searchCritDims, searchTurboModel, searchTurboType]", function(newVal, oldVal) {
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
          var pt = newVal[0];
          if (angular.isObject(pt)) {
            $scope.critDims = $scope.critDimsByPartTypes[pt.id];
          } else {
            $scope.critDims = null;
          }
          $scope.showCriticalDimensions = false;
          $scope.searchCritDims = {}; // re-init
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
