'use strict';

angular.module('ngMetaCrudApp')
  .directive('turboTypes', ['$log', 'toastr', 'NgTableParams',
    function($log, toastr, NgTableParams) {
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
                $scope.part.turboTypes = turboTypes;
                $scope.turboTypesTableParams.settings({dataset: $scope.part.turboTypes});
              });
            };

            $scope.removeTurboType = function(turboTypeToRemove) {
              dialogs.confirm('Remove Turbo Type?',
                'Do you want to remove this turbo type from the part?').result.then(
                  function() {
                    // Yes
                    restService.removeTurboType($scope.part.id, turboTypeToRemove.id).then(
                      function(turboTypes) {
                        $scope.part.turboTypes = turboTypes;
                        $scope.turboTypesTableParams.settings({dataset: $scope.part.turboTypes});
                        toastr.success('The turbo type [' +
                          turboTypeToRemove.id + '] - ' +
                          turboTypeToRemove.manufacturer.name + ' - ' +
                          turboTypeToRemove.name +
                          ' has been successfully removed.');
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
    }
  ]);
