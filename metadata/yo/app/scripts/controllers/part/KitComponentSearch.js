'use strict';

angular.module('ngMetaCrudApp')
.controller('KitComponentSearchCtrl', ['$log', '$scope', '$location',
  '$routeParams', 'restService', 'dialogs', 'toastr',
  'partTypes', 'components',
  function ($log, $scope, $location, $routeParams, restService, dialogs,
            toastr, partTypes, components)
  {
    $scope.partId = parseInt($routeParams.id);
    $scope.partType = 'Kit';

    $scope.pickedPart = null;
    $scope.showPickedPart = false;

    $scope.partTypes = partTypes;
    $scope.components = components;

    var existed = _.object(_.map(components, function(c) {
      return [c.part.id, true];
    }));

    $scope.cantBePicked = function(id) {
      return existed[id];
    };

    // The base kit
    $scope.part = restService.findPart($scope.partId).then(
      function (part) {
        $scope.part = part;
      }, function (errorResponse) {
        restService.error('Could not get part details', errorResponse);
      }
    );

    $scope.mapping = {
      exclude: false
    };

    $scope.save = function () {
      restService.saveKit($scope.pickedPart.id, $scope.partId /* Kit */, $scope.mapping.exclude).then(
        function success() {
          toastr.success('Common component mapping added.');
          $location.path('/part/' + $scope.partId);
        },
        function failure(response) {
            restService.error('Could not add kit mapping', response);
        }
      );
    };

    $scope.pick = function (partId) {
      $scope.pickedPart = restService.findPart(partId).then(
        function success(pickedPart) {
            $scope.pickedPart = pickedPart;
        },
        function failure(errorResponse) {
          restService.error('Could not pick part', errorResponse);
        }
      );
    };

  }]
);
