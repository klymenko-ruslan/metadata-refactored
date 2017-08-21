'use strict';

angular.module('ngMetaCrudApp')

.controller('AddStandardOversizeCtrl', ['$log', '$scope', '$location', 'NgTableParams',
  'restService', 'dialogs', 'toastr', 'utils', 'partTypes', 'part', 'type', 'existing',
  function($log, $scope, $location, NgTableParams, restService,
    dialogs, toastr, utils, partTypes, part, type, existing) {

    $scope.partTypes = partTypes;
    $scope.restService = restService;
    $scope.partId = part.id;
    $scope.part = part;
    $scope.type = type;

    var pickedParts = [];
    var pickedPartIds = {};
    var existingPartIds = null;

    function updateExistingPartIds() {
      existingPartIds = {};
      _.each(existing, function(p) {
        existingPartIds[p.id] = true;
      });
    }

    updateExistingPartIds();

    $scope.existingTableParams = new NgTableParams({
      page: 1,
      count: 5
    }, {
        counts: [5, 10, 15],
      getData: utils.localPagination(existing, 'manufacturerPartNumber')
    });

    $scope.pickedPartsTableParams = new NgTableParams(
      {
        page: 1,
        count: 5,
        sorting: {}
      },
      {
        counts: [5, 10, 15],
        getData: utils.localPagination(pickedParts)
      }
    );

    $scope.pick = function(pickedPart) {
      pickedParts.push(pickedPart);
      pickedPart.extra = {
        qty: 1
      };
      pickedPartIds[pickedPart.id] = true;
      $scope.pickedPartsTableParams.reload();
    };

    $scope.unpick = function(partId) {
      var idx = _.findIndex(pickedParts, function(p) {
        return p.id === partId;
      });
      var p = pickedParts[idx];
      delete p.extra;
      pickedParts.splice(idx, 1);
      delete pickedPartIds[partId];
      $scope.pickedPartsTableParams.reload();
    };

    $scope.unpickAll = function() {
      _.each(pickedParts, function(pp) {
        delete pickedPartIds[pp.id];
      });
      pickedParts.splice(0, pickedParts.length);
      $scope.pickedPartsTableParams.reload();
    };

    $scope.isBttnSaveDisabled = function() {
      return pickedParts.length === 0;
    };

    $scope.isBttnUnpickAllDisabled = function() {
      return pickedParts.length === 0;
    };

    $scope.isBttnPickDisabled = function(p) {
      return p === undefined || $scope.part.manufacturer.id !== p.manufacturer.id ||
        $scope.part.partType.id !== p.partType.id ||
        $scope.part.id === p.id || pickedPartIds[p.id] || existingPartIds[p.id];
    };

    $scope.save = function() {
      var partIds = _.map(pickedParts, function(p) {
        return p.id;
      });

      restService.createStandardOversizePart($scope.type, $scope.partId, partIds).then(
        function success(response) {
          existing.splice(0, existing.length);
          _.each(response.parts, function(p) {
            existing.push(p);
          });
          updateExistingPartIds();
          $scope.existingTableParams.reload();
          _.each(pickedParts, function(p) {
            delete pickedPartIds[p.id];
          });
          pickedParts.splice(0, pickedParts.length);
          $scope.pickedPartsTableParams.reload();
          toastr.success('The part(s) has been successfully added.');
        },
        function failure(error) {
          restService.error('Could not create the standard/oversize part.', error);
        }
      );
    };

    $scope.remove = function(partId) {
      var idx = _.findIndex(existing, function(p) {
        return p.id === partId;
      });
      //var p = existing[idx];
      var standardPartId, oversizePartId, title, message, toast;
      if ($scope.type === 'standard') {
        standardPartId = partId;
        oversizePartId = $scope.partId;
        title = 'Delete Standard Part?';
        message = 'Do you want to delete this standard part?';
        toast = 'The standard part has been deleted.';
      } else if ($scope.type === 'oversize') {
        standardPartId = $scope.partId;
        oversizePartId = partId;
        title = 'Delete Oversize Part?';
        message = 'Do you want to delete this oversize part?';
        toast = 'The oversize part has been deleted.';
      } else {
        throw 'Unknown type: ' + $scope.type;
      }
      dialogs.confirm(title, message).result.then(
        function() {
          // Yes
          restService.deleteStandardOversizePart(standardPartId, oversizePartId).then(
            function() {
              existing.splice(idx, 1);
              $scope.existingTableParams.reload();
              updateExistingPartIds();
              toastr.success(toast);
            },
            restService.error
          );
        }
      );
    };

  }
]);
