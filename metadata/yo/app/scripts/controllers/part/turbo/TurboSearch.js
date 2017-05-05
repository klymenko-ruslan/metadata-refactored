'use strict';

angular.module('ngMetaCrudApp')
  .controller('TurboSearchCtrl', [
    '$log', '$scope', '$location', 'NgTableParams', '$uibModal', 'dialogs', 'toastr', 'restService',
    'BOM', 'utils', 'part', 'partTypes', 'turbos',
    function ($log, $scope, $location, NgTableParams, $uibModal, dialogs, toastr, restService,
              BOM, utils, part, partTypes, turbos)
    {

      $scope.part = part; // primary part
      $scope.partTypes = partTypes;
      $scope.restService = restService;

      $scope.searchPartType = part.partType.id;

      var pickedParts = [];
      var pickedPartIds = {};

      $scope.showPart = function(partId) {
        $location.path('/part/' + partId);
      }

      $scope.linkedTurbosTableParams = null;

      function _updateLinkedTurbosTableParams(turbos) {
        $scope.linkedTurbosTableParams = new NgTableParams({
          page: 1,
          count: 10
        }, {
          getData: utils.localPagination(turbos, 'id')
        });
      };

      _updateLinkedTurbosTableParams(turbos);

      $scope.pickedPartsTableParams = new NgTableParams(
        {
          page: 1,
          count: 10,
          sorting: {}
        },
        {
          getData: utils.localPagination(pickedParts)
        }
      );

      $scope.isBttnSaveDisabled = function() {
        return pickedParts.length === 0;
      };

      $scope.isBttnPickDisabled = function(p) {
        return p === undefined || $scope.part.manufacturer.id != p.manufacturer.id ||
          p.partType.id != 1 || $scope.part.id == p.id || pickedPartIds[p.id];
      };

      $scope.isBttnUnpickAllDisabled = function() {
        return pickedParts.length === 0;
      };

      $scope.save = function() {
        var turboIds = _.map(pickedParts, function(p) { return p.id; });
        restService.linkTurbosToGasketKit($scope.part.id, turboIds).then(
          function success(result) {
            var failures = _.filter(result.rows, function(row) { return !row.success; });
            if (failures.length > 0) {
              $uibModal.open({
                templateUrl: '/views/part/gasketkit/FailedTurbosDlg.html',
                animation: false,
                size: 'lg',
                controller: 'FailedTurbosDlgCtrl',
                resolve: {
                  part: function() {
                    return $scope.part;
                  },
                  failures: function() {
                    return failures;
                  }
                }
              });
            } else {
              toastr.success('The Turbo(s) have been successfully linked to this GasketKit.');
            }
            _updateLinkedTurbosTableParams(result.turbos);
            $scope.unpickAll();
          },
          function failure(result) {
            restService.error('Can\'t add the Turbo(s) to the Gasket Kit.', error);
          }
        );

      };

      $scope.pick = function(pickedPart) {
        pickedParts.push(pickedPart);
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

    }
  ]).controller('FailedTurbosDlgCtrl', ['$scope', '$log', '$location', '$uibModalInstance', 'NgTableParams',
      'utils', 'part', 'failures',
    function($scope, $log, $location, $uibModalInstance, NgTableParams, utils, part, failures) {

      $scope.part = part;

      $scope.failuresTableParams = new NgTableParams({
        page: 1,
        count: 10
      }, {
        getData: utils.localPagination(failures, 'manufacturerPartNumber')
      });

      $scope.onClose = function() {
        $uibModalInstance.close();
      };

      $scope.showPart = function(partId) {
        $scope.onClose();
        $location.path('/part/' + partId);
      };

  }]);
