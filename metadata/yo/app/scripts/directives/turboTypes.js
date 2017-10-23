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
            ).result.then(function(turboType) {
              // TODO: reload new listfrom server
              $scope.part.turboTypes.push(turboType);
              $scope.turboTypesTableParams.settings({dataset: $scope.part.turboTypes})
            });
          };

          $scope.removeTurboType = function(turboTypeToRemove) {
            dialogs.confirm('Remove Turbo Type?',
              'Do you want to remove this turbo type from the part?').result.then(
                function() {
                  // Yes
                  restService.removeTurboType($scope.part.id, turboTypeToRemove.id).then(
                    function() {
                      // Success
                      // TODO: reload new listfrom server
                      toastr.success('Turbo type removed.');
                      var idx = _.indexOf($scope.part.turboTypes, turboTypeToRemove);
                      $scope.part.turboTypes.splice(idx, 1);
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
