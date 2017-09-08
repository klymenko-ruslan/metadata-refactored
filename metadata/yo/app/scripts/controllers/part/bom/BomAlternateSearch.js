'use strict';

angular.module('ngMetaCrudApp')
.controller('BomAlternateSearchCtrl', ['$log', '$scope', '$location', '$routeParams', 'NgTableParams', 'utils',
  'restService', 'dialogs', 'toastr', 'partTypes', 'part', 'selectedBom', 'bom', 'altBom', 'altHeaderId',
  function ($log, $scope, $location, $routeParams, NgTableParams, utils, restService, dialogs, toastr, partTypes,
    part, selectedBom, bom, altBom, altHeaderId)
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

    $scope.altHeaderId = altHeaderId;

    var alternatives = _.chain(altBom)
      .filter(function(pg) {return pg.id === altHeaderId;})
      .map(function(pg) {return pg.parts;})
      // Return first item from array (the array must have 0 or 1 element.
      .reduce(function(memo, parts) {return memo.concat(parts);})
      .value();

    var alternativesIdx = _.reduce(alternatives, function(memo, val) { memo[val.partId] = true; return memo; }, {});

    $scope.altBomTableParams = new NgTableParams({
        page: 1,
        count: 10
      }, {
        getData: utils.localPagination(alternatives, 'partNumber')
      }
    );

    $scope.save = function () {
      restService.createBomAlternative($scope.partId, $scope.selectedBom.id, $scope.altHeaderId, $scope.pickedPart.id).then(
        function success() {
          toastr.success('BOM alternate added.');
          $location.path('/part/' + $scope.partId);
        },
        function failure(response) {
          dialogs.error('Could not add BOM alternate', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
        }
      );
    };

    $scope.isPickBttnDisabled = function(partId) {
      if (partId === undefined) {
        return false;
      }
      return $scope.pickedPart && $scope.pickedPart.id === partId || alternativesIdx[partId.toString()];
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
