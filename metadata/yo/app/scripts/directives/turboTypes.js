"use strict";

angular.module("ngMetaCrudApp")
  .directive("turboTypes", ["$log", "utils", "ngTableParams", function($log, utils, ngTableParams) {
    return {
      restrict: 'E',
      scope: {
        part: "="
      },
      templateUrl: '/views/component/turbo_types.html',
      controller: ["$scope", "$parse", "dialogs", "gToast", "Restangular", "restService",
        function($scope, $parse, dialogs, gToast, Restangular, restService) {
          $scope.turboTypesTableParams = new ngTableParams({
            page: 1,
            count: 10
          }, {
            getData: utils.localPagination($scope.part.turboTypes, "manufacturer.name")
          });
          $scope.removeTurboType = function(turboTypeToRemove) {
            dialogs.confirm(
              "Remove Turbo Type?",
              "Do you want to remove this turbo type from the part?").result.then(
                function() {
                  // Yes
                  Restangular.setParentless(false);
                  Restangular.one("part", $scope.part.id).one("turboType", turboTypeToRemove.id).remove().then(
                    function() {
                      // Success
                      gToast.open("Turbo type removed.");
                      var idx = _.indexOf($scope.part.turboTypes, turboTypeToRemove);
                      $scope.part.turboTypes.splice(idx, 1);
                      $scope.turboTypesTableParams.reload();
                    },
                    function(response) {
                      // Error
                      restService.error("Could not delete turbo type.", response);
                    }
                  );
                },
                function() {
                  // No
                }
              );
          };
        }
      ]
    };
  }]);
