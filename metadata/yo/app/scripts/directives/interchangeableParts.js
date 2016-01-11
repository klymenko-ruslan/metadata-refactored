"use strict";

angular.module("ngMetaCrudApp")
  .directive("interchangeableParts2", ["$log", "restService", "ngTableParams", function($log, restService, ngTableParams) {
    return {
      restrict: 'E',
      scope: {
        part: "=",
        interchId: "=",
        parentPartId: "="
      },
      templateUrl: '/views/component/interchangeable_parts.html',
      controller: function($scope) {
        $log.log("scope: " + angular.toJson($scope));
        $log.log("$scope.part: " + angular.toJson($scope.part));
        $log.log("$scope.interchId: " + $scope.interchId);
        $log.log("$scope.parentPartId: " + $scope.parentPartId);
        restService.findInterchange($scope.interchId).then(
          function(interchange) {
            // Remove the parent part.
            $log.log("Loaded interchange: " + angular.toJson(interchange.parts));
            var idx = _.findIndex(interchange.parts, function(part) {
              return part.id == $scope.parentPartId;
            });
            if (idx > -1) {
              $log.log("Found idx: " + idx);
              interchange.parts.splice(idx, 1);
              $log.log("Sliced interchange: " + angular.toJson(interchange.parts));
            }
            $scope.interchangeablePartsTableParams = new ngTableParams({
              page: 1,
              count: 10
            }, {
              getData: function($defer, params) {
                $log.log("params1: " + angular.toJson(params));
                params.total(interchange.parts.length);
                $log.log("params2: " + angular.toJson(params));
                $log.log("parts: " + angular.toJson(interchange.parts));
                var recs = interchange.parts.slice((params.page() - 1) * params.count(), params.page() * params.count());
                $log.log("recs: " + angular.toJson(recs));
                $defer.resolve(recs);
              }
            });
          },
          function (error) {
            restService.error("Can't load interchangeable parts.", error);
          }
        );
      }
    };
  }]);
