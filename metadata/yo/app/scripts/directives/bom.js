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
      controller: ['dialogs', '$scope', '$log', '$location', '$q', '$parse', 'BOM', 'NgTableParams', 'toastr', 'utils',
          'restService', function(dialogs, $scope, $log, $location, $q, $parse, BOM, NgTableParams, toastr, utils, restService)
        {
          // The BOM item whose alternates we're showing.
          $scope.selectedBom = null;
          // List of groups of alternatives.
          $scope.altBoms = null;
          // Temp storage for quantities
          $scope.modifyValues = {};

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
                  getData: utils.localPagination($scope.bom, 'partNumber')
                });
              },
              function failure(response) {
                restService.error('Loading of BOMs failed.', response);
              }
            );
          });

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
                    $scope.selectedBom = null;
                    toastr.success('Child part removed from BOM.');
                  },
                  function failure(response) {
                    restService.error('Removing of the BOM failed.', response);
                  }
                );
              }
            );
          };

          $scope.newAltGroup = function() {
            alert('Uder development.');
            /*
            restService.createAltBomGroup($scope.parentPartId, $selectedBom.partId).then(
              function success(partGroups) {
                _updateAltBoms(partGroups);
              },
              function failure(response) {
                restService.error('Creation of a new BOM alternate group failed.', response);
              }
            );
            */
          };

          $scope.removeAltGroup = function(altHeaderId) {
            alert('Under development: ' + altHeaderId);
          };

          $scope.openAddAlternativeView = function(altHeaderId) {
            $location.path('/part/' + $scope.parentPartId + '/bom/' + $scope.selectedBom.partId + '/alt/' + altHeaderId);
          };

          function _updateAltBoms(partGroups) {
            var result = [];
            _.each(partGroups, function(pg) {
              result.push({
                altHeaderId: pg.id,
                ngTableParams: new NgTableParams(
                  {
                    page: 1,
                    count: 10
                  }, {
                    getData: utils.localPagination(pg.parts, 'partNumber')
                  }
                )
              });
            });
            $scope.altBoms = _.sortBy(result, 'altHeaderId');
          }

          $scope.removeAlternate = function(headerId, partId) {
            dialogs.confirm(
              'Remove alternate item?',
              'This will remove the alternate part from this BOM item.').result.then(
              function() {
                restService.removeBomAlternative(headerId, partId).then(
                  function success(partGroups) {
                    _updateAltBoms(partGroups);
                    toastr.success('BOM alternative has been successfully removed.');
                  },
                  function failure(response) {
                    restService.error('Removing of the Alternate failed.', response);
                  }
                );
              });
          };

          $scope.showAlternates = function(b) {
            restService.getBomAlternatives($scope.parentPartId, b.partId).then(
              function success(partGroups) {
                _updateAltBoms(partGroups);
                $scope.selectedBom = b;
              },
              function failure(response) {
                $scope.altBomItem = null;
                restService.error('Can\'t retrive a list of alternative BOMs.', response);
              }
            );
          };

        }
      ]
    };
  });
