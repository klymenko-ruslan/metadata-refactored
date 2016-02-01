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
      controller: function($log, $q, $scope, ngTableParams) {
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
            $scope.search.count = params.count();
            $scope.search.page = params.page();
            $scope.search.sorting = params.sorting();
            // $log.log("sorting: " + angular.toJson($scope.search.sorting));
            var offset = params.count() * (params.page() - 1);
            var limit = params.count();
            for (var sortProperty in $scope.search.sorting) break;
            if (sortProperty) {
              var sortOrder = $scope.search.sorting[sortProperty];
            }
            // $log.log("sortProperty: " + sortProperty + ", sortOrder: " + sortOrder);
            // $log.log("aggregations: " + angular.toJson($scope.search.aggregations));
            restService.filterParts($scope.search.partNumber, $scope.search.aggregations["Part Type"],
                $scope.search.aggregations["Manufacturer"], $scope.search.aggregations["Kit Type"],
                $scope.search.aggregations["Gasket Type"], $scope.search.aggregations["Seal Type"],
                $scope.search.aggregations["Coolant Type"], $scope.search.aggregations["Turbo Type"],
                $scope.search.aggregations["Turbo Model"],
                sortProperty, sortOrder, offset, limit).then(
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

        // Query Parameters
        $scope.search = {
          partNumber: "",
          aggregations: {},
          sort: {}
        };

        $scope.clear = function() {
          $scope.search = {
            partNumber: "",
            aggregations: {},
            sort: {}
          };
        };

        // Handle updating search results
        $scope.$watch("[search.partNumber, search.aggregations]", function(newVal, oldVal) {
          // Debounce
          if (angular.equals(newVal, oldVal, true)) {
            return;
          }
          $scope.partTableParams.reload();
        }, true);
      }
    };
  }]).directive("partSearchActions", ["$log", function($log) {
    return {
      restrict: "A",
      require: "^partSearch",
      link: function postLink(scope, element, attrs, controller) {
        scope.partId = scope.part._id;
        scope.partType = scope.part._source.partType.name;
        controller.transcludeActionsFn(scope, function(clone) {
          element.append(clone);
        });
      }
    };
  }]);
