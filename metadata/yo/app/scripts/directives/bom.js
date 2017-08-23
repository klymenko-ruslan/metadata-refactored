'use strict';

angular.module('ngMetaCrudApp')
  .directive('bom', function() {
    return {
      scope: {
        parentPartId: '='
      },
      templateUrl: '/views/component/bom.html',
      restrict: 'E',
      link: function postLink() {},
      controller: ['dialogs', '$scope', '$q', '$parse', 'BOM', 'NgTableParams', 'toastr', 'utils', 'restService',
        function(dialogs, $scope, $q, $parse, BOM, NgTableParams, toastr, utils, restService) {
          $scope.restService = restService;

          $scope.bomTableParams = new NgTableParams({
            page: 1,
            count: 10
          }, {
            data: []
          });

          $scope.$watch('parentPartId', function(parentPartId) {
            if (parentPartId === undefined) {
              return;
            }
            BOM.listByParentPartId(parentPartId).then(
              function success(bom) {
                $scope.bom = bom;
                $scope.bomTableParams = new NgTableParams({
                  page: 1,
                  count: 10
                }, {
                  getData: utils.localPagination($scope.bom, 'child.manufacturerPartNumber')
                });
              },
              function failure(response) {
                restService.error('Loading of BOMs failed.', response);
              }
            );
          });

          // Temp storage for quantities
          $scope.modifyValues = {};

          $scope.isModifying = function(b) {
            return angular.isDefined($scope.modifyValues[b.partId]);
          };

          $scope.modifyStart = function(b) {
            $scope.modifyValues[b.partId] = b.qty;
          };

          $scope.modifyCancel = function(b) {
            delete $scope.modifyValues[b.partId];
          };

          $scope.modifySave = function(b) {
            var quantity = $scope.modifyValues[b.partId];
            restService.updateBom($scope.parentPartId, b.partId, quantity).then(
              function() {
                b.qty = quantity;
                delete $scope.modifyValues[b.partId];
              },
              function() {}
            );
          };

          $scope.remove = function(b) {
            dialogs.confirm(
              'Remove BOM Item?',
              'Remove child part from this bill of materials?').result.then(
              function() {
                // Yes
                BOM.removeBom($scope.parentPartId, b.partId).then(
                  function success(updatedBom) {
                    // Success
                    $scope.bom.splice(0, $scope.bom.length);
                    $scope.bom.push.apply($scope.bom, updatedBom);
                    $scope.bomTableParams.reload();
                    // Clear the alt bom item
                    $scope.altBomItem = null;
                    toastr.success('Child part removed from BOM.');
                  },
                  function failure(response) {
                    restService.error('Removing of the BOM failed.', response);
                  }
                );
              }
            );
          };

          $scope.removeAlternate = function(index, altItem) {
            dialogs.confirm(
              'Remove alternate item?',
              'This will remove the alternate part from this BOM item.').result.then(
              function() {
                restService.removeBomAlternative($scope.altBomItem.id, altItem.id).then(
                  function success() {
                    $scope.altBomItem.alternatives.splice(index, 1);
                    toastr.success('BOM alternate removed.');
                  },
                  function failure(response) {
                    restService.error('Removing of the Alternate failed.', response);
                  }
                );
              });
          };


          // The BOM item whose alternates we're showing
          $scope.altBomItem = null;

          $scope.showAlternates = function(bomItem) {
            $scope.altBomItem = bomItem;
          };

          $scope.hideAlternates = function() {
            $scope.altBomItem = null;
          };

        }
      ]
    };
  });
