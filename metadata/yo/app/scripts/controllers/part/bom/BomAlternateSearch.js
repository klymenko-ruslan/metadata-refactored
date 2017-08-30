'use strict';

angular.module('ngMetaCrudApp')
.controller('BomAlternateSearchCtrl', ['$log', '$scope', '$location', '$routeParams', 'NgTableParams', 'utils',
  'restService', 'dialogs', 'toastr', 'partTypes', 'part', 'selectedBom', 'bom', 'altBom',
  function ($log, $scope, $location, $routeParams, NgTableParams, utils, restService, dialogs, toastr, partTypes,
    part, selectedBom, bom, altBom)
  {
    $scope.partTypes = partTypes;
    $scope.part = part;
    $scope.selectedBom = selectedBom;
    $scope.partId = $routeParams.id;
    $scope.bomItemId = $routeParams.bomId;
    $scope.headerId = null;

    $scope.pickedPart = null;

    $scope.bomTableParams = new NgTableParams({
        page: 1,
        count: 10
      }, {
        getData: utils.localPagination(bom, 'partNumber')
      }
    );

    var alternatives = [];
    _.each(altBom, function(pg) {
      var grpId = pg.id;
      var parts = pg.parts;
      _.each(parts, function(p) {
        p['altHeaderId'] = grpId;
        alternatives.push(p);
      });
    });
    
    if (alternatives.length) {
      $scope.headerId = alternatives[0]['altHeaderId'];
    }

    $scope.altBomTableParams = new NgTableParams({
        page: 1,
        count: 10
      }, {
        getData: utils.localPagination(alternatives, 'partNumber')
      }
    );

    $scope.save = function () {
      restService.createBomAlternative($scope.partId, $scope.selectedBom.id, $scope.headerId, $scope.pickedPart.id).then(
        function success() {
          toastr.success('BOM alternate added.');
          $location.path('/part/' + $scope.partId);
        },
        function failure(response) {
          dialogs.error('Could not add BOM alternate', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
        }
      );
    };

    $scope.pickPart = function (partId) {
      $scope.pickedPart = restService.findPart(partId).then(
        function (pickedPart) {
          $scope.pickedPart = pickedPart;
        },
        function (errorResponse) {
          $log.log('Could not pick part', errorResponse);
          restService.error('Could not pick part.', errorResponse);
        }
      );
    };

  }

]);
