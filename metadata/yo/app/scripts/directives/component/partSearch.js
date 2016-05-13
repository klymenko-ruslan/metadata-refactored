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
      controller: ["$log", "$q", "$scope", "ngTableParams", function($log, $q, $scope, ngTableParams) {
        $scope.critDimEnumValsMap = _.indexBy($scope.critDimEnumVals, "id");
        // Filter
        $scope.searchPartType = null;
        $scope.searchManufacturer = null;
        $scope.search = {};
        $scope.searchCritDims = {};
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
            restService.filterParts(searchPartTypeId, searchManufacturerId, $scope.search, $scope.searchCritDims, sortProperty, sortOrder, offset, limit).then(
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

        // Critical dimensions for the current choosed $scope.searchPartType.
        $scope.critDims = null;

        $scope.$watch("[search, searchManufacturer, searchCritDims]", function(newVal, oldVal) {
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
          if (angular.equals(newVal, oldVal, true) && !angular.isObject(newVal)) {
            return;
          }
          var pt = newVal[0];
          if (angular.isObject(pt)) {
            $scope.critDims = $scope.critDimsByPartTypes[pt.id];
            $scope.searchCritDims = {}; // re-init
          }
          $scope.partTableParams.reload();
        }, true);

      }]
    };
  }])
  .directive("partSearchActions", ["$log", function($log) {
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
