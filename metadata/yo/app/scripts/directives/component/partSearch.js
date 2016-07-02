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
      controller: ["$parse", "$sce", "$log", "$q", "$scope", "ngTableParams", function($parse, $sce, $log, $q,
        $scope, ngTableParams) {

        $scope.critDimEnumValsMap = _.indexBy($scope.critDimEnumVals, "id");
        // Filter
        $scope.searchPartType = null;
        $scope.searchManufacturer = null;
        $scope.searchName = null;
        $scope.search = {};
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
            $log.log("showCriticalDimensions is TRUE. critDimCols: " + angular.toJson($scope.critDims));
            var critDimCols = [];
            if ($scope.critDims) {
              _.each($scope.critDims, function (d) {
                var col = {
                  title: d.name,
                  getter: $parse("_source." + d.jsonName),
                  sortable: d.idxName
                };
                $log.log("col: " + angular.toJson(d));
                critDimCols.push(col);
              });
            }
            $log.log("critDimCols: " + angular.toJson(critDimCols));
            $scope.columns = Array.prototype.concat($scope.fixedCols, critDimCols, $scope.actionsCol);
          } else {
            $log.log("showCriticalDimensions is FALSE");
            $scope.columns = Array.prototype.concat($scope.fixedCols, $scope.actionsCol);
          }
        };

        $scope.initColumns();

        $scope.$watch("[showCriticalDimensions]", function(newVal, oldVal) {
          $scope.initColumns();
        });

        // Latest Results
        $scope.searchResults = null;
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
            var searchPartTypeId = $scope.searchPartType ? $scope.searchPartType.id : null;
            var searchManufacturerId = $scope.searchManufacturer ? $scope.searchManufacturer.id : null;
            restService.filterParts(searchPartTypeId, searchManufacturerId, $scope.searchName, $scope.search, $scope.searchCritDims, sortProperty, sortOrder, offset, limit).then(
              function(filtered) {
                $scope.searchResults = filtered;
                // Update the total and slice the result
                $defer.resolve($scope.searchResults.hits.hits);
                params.total($scope.searchResults.hits.total);
              },
              function(errorResponse) {
                $log.log("Couldn't search for parts.");
                $defer.reject();
              }
            );
          }
        });

        $scope.clearPartNumber = function() {
          $scope.search.partNumber = null;
        };

        $scope.clearPartNumber();

        // Critical dimensions for the current choose $scope.searchPartType.
        $scope.critDims = null;

        $scope.$watch("[search, searchManufacturer, searchName, searchCritDims]", function(newVal, oldVal) {
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
          var pt = newVal[0];
          if (angular.isObject(pt)) {
            $scope.critDims = $scope.critDimsByPartTypes[pt.id];
          } else {
            $scope.critDims = null;
            $scope.showCriticalDimensions = false;
          }
          $scope.searchCritDims = {}; // re-init
          $scope.partTableParams.reload();
        }, true);

      }]
    };
  }]);
