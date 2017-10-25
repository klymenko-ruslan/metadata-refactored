'use strict';

angular.module('ngMetaCrudApp')
  .directive('turboTypes', ['$log', 'NgTableParams', function($log, NgTableParams) {
    return {
      restrict: 'E',
      scope: {
        part: '='
      },
      templateUrl: '/views/component/turbo_types.html',
      controller: ['$scope', '$parse', 'dialogs', 'toastr', 'restService',
        function($scope, $parse, dialogs, toastr, restService) {
          $scope.turboTypesTableParams = new NgTableParams({
            page: 1,
            count: 10,
            sorting: {'manufacturer.name': 'asc'}
          }, {
            dataset: $scope.part.turboTypes
          });

          // Turbo Types
          $scope.addTurboType = function() {
            dialogs.create(
              '/views/part/dialog/AddTurboType.html',
              'AddTurboTypeDialogCtrl',
              {partId: $scope.part.id}
            ).result.then(function(turboTypes) {
$log.log('After add turbo type: ' + angular.toJson(turboTypes, 2));
              $scope.part.turboTypes = turboTypes;
              $scope.turboTypesTableParams.settings({dataset: $scope.part.turboTypes})
            });
          };

          $scope.removeTurboType = function(turboTypeToRemove) {
            dialogs.confirm('Remove Turbo Type?',
              'Do you want to remove this turbo type from the part?').result.then(
                function() {
                  // Yes
                  restService.removeTurboType($scope.part.id, turboTypeToRemove.id).then(
                    function(turboTypes) {
$log.log('After remove turbo type: ' + angular.toJson(turboTypes, 2));
                      $scope.part.turboTypes = turboTypes;
                      $scope.turboTypesTableParams.settings({dataset: $scope.part.turboTypes})
                    },
                    function(response) {
                      // Error
                      restService.error('Could not delete turbo type.', response);
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
