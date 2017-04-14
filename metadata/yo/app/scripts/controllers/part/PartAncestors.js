"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartAncestorsCtrl", ["$log", "$routeParams", "$scope", "restService", "Restangular", "NgTableParams",
  function($log, $routeParams, $scope, restService, Restangular, NgTableParams) {

    $scope.partId = $routeParams.id;
    $scope.part = null;

    restService.findPart($scope.partId).then(
      function (part) {
        $scope.part = part;

        // Make sure we're using the correct part type
        $scope.partType = part.partType.name;
      },
      function (errorResponse) {
        $log.log("Could not get part details", errorResponse);
        restService.error("Could not get part details", errorResponse);
      }
    );

    $scope.ancestorsTableParams = new NgTableParams({
        page: 1,
        count: 25
      }, {
        getData: function(params) {
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          return restService.loadAncestors($scope.partId, offset, limit).then(
            function(result) {
              params.total(result.total);
              return result.recs;
            },
            function(errorResponse) {
              restService.error("Loading of ancestors failed.", errorResponse);
            });
        }
      }
    );

  }]);
