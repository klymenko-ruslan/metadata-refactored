
"use strict";

angular.module("ngMetaCrudApp")
  .directive("interchangeableParts", ["$log", "restService", "ngTableParams", function($log, restService, ngTableParams) {
    return {
      scope: {
        interchangeId: "=",
        parentPartId: "=",
      },
      templateUrl: '/views/component/interchangeable_parts.html',
      restrict: 'E',
      controller: function($scope) {
        $log.log("scope: " + angular.toJson($scope));
        $log.log("$scope.interchangeId: " + $scope.interchangeId);
        $log.log("interchangeId: " + interchangeId);
        restService.findInterchange($scope.interchangeId).then(
          function(interchange) {
            // Remove the parent part.
            $log.log("interchange: " + interchange);
            var idx = _.findIndex(interchange.parts, function(part) {
              return part.id == $scope.parentPartId;
            });
            if (idx > -1) {
              interchange.parts.splice(idx, 1);
            }
            $scope.interchangeablePartsTableParams = new ngTableParams({
              page: 1,
              count: 10
            }, {
              getData: function($defer, params) {
                $defer.resolve(interchange.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                params.total(interchange.parts.length);
              }
            });
          },
          restService.error
        );
      }
    };
  }]);
