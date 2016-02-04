"use strict";

angular.module("ngMetaCrudApp")
  .directive("turboTypes", ["$log", "ngTableParams", function($log, ngTableParams) {
    return {
      restrict: 'E',
      scope: {
        turboTypes: "=ttypes"
      },
      templateUrl: '/views/component/turbo_types.html',
      controller: ["$scope", "$parse", function($scope, $parse) {
        $scope.turboTypesTableParams = new ngTableParams({
          page: 1,
          count: 10
        }, {
          getData: function($defer, params) {
            var sorting = params.sorting();
            var sortAsc = true;
            for (var sortProperty in sorting) break;
            if (sortProperty) {
              sortAsc = sorting[sortProperty] == "asc";
            } else {
              sortProperty = "manufacturer.name"; // asc. see above.
            }
            var sortedAsc = _.sortBy($scope.turboTypes, function(b) {
              var s = $parse(sortProperty)(b);
              if (s && _.isString(s)) {
                s = s.toLowerCase();
              }
              return s;
            });
            var sorted = sortAsc ? sortedAsc : sortedAsc.reverse();
            var page = sorted.slice((params.page() - 1) * params.count(), params.page() * params.count());
            params.total($scope.turboTypes.length);
            $defer.resolve(page);
          }
        });
      }]
    };
  }]);
