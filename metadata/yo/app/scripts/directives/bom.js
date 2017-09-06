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
          $scope.highlightedBom = null;

          $scope.selectedItems = {
            allBomChecked: false,
            bom: {}
          };

          // BOM of the part.
          $scope.bom =  null;
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
                _initBoms(bom);
              },
              function failure(response) {
                restService.error('Loading of BOMs failed.', response);
              }
            );
          });

          function _initBoms(bom) {
            $scope.selectedItems.allBomChecked = false;
            $scope.selectedItems.bom = {};
            // As BOM has complex primary key which is not suitable for processing in UI
            // we assign artificial "_rowid" property.
            var rowid = 1;
            _.each(bom, function(b) {
              b._rowid = rowid++;
            });
            $scope.bom = bom;
            $scope.bomTableParams = new NgTableParams({
              page: 1,
              count: 10
            }, {
              getData: utils.localPagination($scope.bom, 'partNumber')
            });
          };

          $scope.$watch('selectedItems.allBomChecked', function(val) {
            if ($scope.bom) {
              _.each($scope.bom, function(b) {
                $scope.selectedItems.bom[b._rowid] = val;
              });
            }
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

          $scope.onRemoveSelectedBoms = function() {
            dialogs.confirm('Delete BOM item(s)?',
              'Do you really want to remove selected part(s) from the BOM? ' +
              'Corresponding alternative BOM will be removed too.').result
              .then(
                function yes() {
                  var children = _.chain($scope.selectedItems.bom).reduce(function(memo, v, k) {
                    if (v === true) {
                      var b = _.chain($scope.bom).find(function(e) { return e._rowid == k; }).value();
                      if (!b) {
                        throw "Internal error. Part not found, _rowid: " + k;
                      }
                      memo.push(b.partId);
                    }
                    return memo;
                  }, []).value();
                  $log.log('To removed: ' + angular.toJson(children));
                }
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
                    $scope.highlightedBom = null;
                    toastr.success('Child part removed from BOM.');
                  },
                  function failure(response) {
                    restService.error('Removing of the BOM failed.', response);
                  }
                );
              }
            );
          };

          $scope.hasAnyAltBomGroup = function() {
            return $scope.altBoms && $scope.altBoms.length > 0 && $scope.altBoms[0].altHeaderId;
          };

          $scope.newAltGroup = function() {
            restService.createAltBomGroup($scope.parentPartId, $scope.highlightedBom.partId).then(
              function success(partGroups) {
                _updateAltBoms(partGroups);
                 toastr.success('The alternative BOM group has been successfully created.');
              },
              function failure(response) {
                restService.error('Creation of a new BOM alternate group failed.', response);
              }
            );
          };

          $scope.removeAltGroup = function(altHeaderId) {
            dialogs.confirm('Confirmation', 'Are you sure? Do you really want to remove this group ' +
              'of alternative BOM [' + altHeaderId + ']?').result.then(
              function yes() {
                restService.deleteAltBomGroup($scope.parentPartId, $scope.highlightedBom.partId, altHeaderId).then(
                  function success(partGroups) {
                    _updateAltBoms(partGroups);
                    toastr.success('The alternative BOM group [" + altHeaderId + "] has been successfully removed.');
                  },
                  function failure(response) {
                    restService.error('Removing of the alternative BOM group [" + altHeaderId + "] failed.', response);
                  }
                );
              },
              function no() {
                // ignore
              }
            );
          };

          $scope.openAddAlternativeView = function(altHeaderId) {
            $location.path('/part/' + $scope.parentPartId + '/bom/' + $scope.highlightedBom.partId + '/alt/' + altHeaderId);
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
                $scope.highlightedBom = b;
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
