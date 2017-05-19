'use strict';

angular.module('ngMetaCrudApp')

  .constant('GASKET_RESULT_STATUS', {
    OK: 'OK',
    ASSERTION_ERROR: 'ASSERTION_ERROR'
  })

  .controller('PartGasketKitSearchCtrl', ['$log', '$scope', '$location', '$routeParams', 'restService',
    'dialogs', 'toastr', 'GASKET_RESULT_STATUS', 'partTypes', 'part',
    function($log, $scope, $location, $routeParams, restService,
      dialogs, toastr, GASKET_RESULT_STATUS, partTypes, part) {
      $scope.partTypes = partTypes;
      $scope.restService = restService;
      $scope.partId = $routeParams.id;

      $scope.pickedPart = null;
      $scope.showPickedPart = false;

      // The part whose bom we're editing
      $scope.part = part;

      $scope.save = function() {
        restService.setGasketKitForPart($scope.partId, $scope.pickedPart.id).then(
          function(result) {
            if (result.status === GASKET_RESULT_STATUS.OK) {
              // Success
              toastr.success('The gasket kit set to the turbo.');
              $location.path('/part/' + $scope.partId);
            } else if (result.status === GASKET_RESULT_STATUS.ASSERTION_ERROR) {
              dialogs.error('Validation error', result.message);
            } else {
              dialogs.error('Internal error', 'Server returned unknown status of the operation: ' + result.status);
            }
          },
          function(response) {
            dialogs.error('Could not set Gasket Kit.', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
          }
        );
      };

      $scope.pickPart = function(pickedId) {
        $scope.pickedPart = restService.findPart(pickedId).then(
          function(pickedPart) {
            $scope.pickedPart = pickedPart;
          },
          function(errorResponse) {
            restService.error('Could not pick part.', errorResponse);
            $log.log('Could not pick part', errorResponse);
          }
        );
      };

    }
  ]);
