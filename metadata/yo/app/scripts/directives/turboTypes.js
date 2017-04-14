"use strict";

angular.module("ngMetaCrudApp")
  .directive("turboTypes", ["$log", "utils", "NgTableParams", function($log, utils, NgTableParams) {
    return {
      restrict: 'E',
      scope: {
        part: "="
      },
      templateUrl: "/views/component/turbo_types.html",
      controller: ["$scope", "$parse", "dialogs", "toastr", "restService",
        function($scope, $parse, dialogs, toastr, restService) {
          $scope.turboTypesTableParams = new NgTableParams({
            page: 1,
            count: 10
          }, {
            getData: utils.localPagination($scope.part.turboTypes, "manufacturer.name")
          });

          // Turbo Types
          $scope.addTurboType = function() {
            dialogs.create(
              "/views/part/dialog/AddTurboType.html",
              "AddTurboTypeDialogCtrl",
              {partId: $scope.part.id}
            ).result.then(function(turboType) {
              $scope.part.turboTypes.push(turboType);
              $scope.turboTypesTableParams.reload();
            });
          }

          $scope.removeTurboType = function(turboTypeToRemove) {
            dialogs.confirm("Remove Turbo Type?",
              "Do you want to remove this turbo type from the part?").result.then(
                function() {
                  // Yes
                  restService.removeTurboType($scope.part.id, turboTypeToRemove.id).then(
                    function() {
                      // Success
                      toastr.success("Turbo type removed.");
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
          }

        }
      ]
    };
  }]);
