'use strict';

angular.module('ngMetaCrudApp')
.controller('KitComponentSearchCtrl', ['$log', '$scope', '$location',
  '$routeParams', 'NgTableParams', 'restService', 'dialogs', 'toastr',
  'partTypes', 'existingMappings',
  function ($log, $scope, $location, $routeParams, NgTableParams, restService,
    dialogs, toastr, partTypes, existingMappings)
  {

    $scope.partId = parseInt($routeParams.id);
    $scope.partType = 'Kit';

    $scope.pickedPart = null;
    $scope.showPickedPart = false;

    $scope.partTypes = partTypes;
    $scope.existingMappings = existingMappings;

    var existed = _.object(_.map(existingMappings.recs, function(c) {
      return [c.part.partId, true];
    }));

    $scope.existingKitComponentsTableParams = new NgTableParams({
      page: 1,
      count: 5,
      sorting: {
        'part.manufacturerPartNumber': 'asc'
      }
    }, {
      dataset: existingMappings.recs
    });

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
      restService.createKitComponent($scope.pickedPart.id, $scope.partId /* Kit */, $scope.mapping.exclude).then(
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
