"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartAncestorsCtrl", ["$log", "$routeParams", "$scope", "restService", "Restangular", "ngTableParams",
  function($log, $routeParams, $scope, restService, Restangular, ngTableParams) {

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

    $scope.ancestorsTableParams = new ngTableParams({
        page: 1,
        count: 25
      }, {
        getData: function($defer, params) {
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          restService.loadAncestors($scope.partId, offset, limit).then(
            function(result) {
              $defer.resolve(result.recs);
              params.total(result.total);
            },
            function(errorResponse) {
              restService.error("Loading of ancestors failed.", errorResponse);
              $defer.reject();
            });
        }
      }
    );

  }]);
