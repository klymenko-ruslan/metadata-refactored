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
      controller: ['dialogs', '$scope', '$log', '$location', '$q', '$parse', 'BOM', 'NgTableParams', 'toastr',
          'restService', function(dialogs, $scope, $log, $location, $q, $parse, BOM, NgTableParams, toastr, restService)
        {
          // The BOM item whose alternates we're showing.
          $scope.highlightedBom = null;

          $scope.selectedItems = {
            bom: {
              allBomChecked: false,
              parts: {}
            },
            altbom: {}
          };

          // BOM of the part.
          $scope.bom = [];
          // List of groups of alternatives.
          $scope.altBoms = null;
          // Temp storage for quantities
          $scope.modifyValues = {};

          // Find a BOM item by its "_rowid".
          function _rowid2b(rowid) {
            return _.chain($scope.bom).find(
              function(e) { return e._rowid === parseInt(rowid); }).value();
          }

          // Return a list of selected BOM items.
          function _findSelectedBomItems() {
            return _.chain($scope.selectedItems.bom.parts)
              .reduce(function(memo, v, k) {
                if (v === true) {
                  var b = _rowid2b(k);
                  if (!b) {
                    throw 'Internal error. Part not found, _rowid: ' + k;
                  }
                  memo.push(b);
                }
                return memo;
              }, []).value();
          }

          // Return a list of IDs of selected Alt BOM  in a group.
          function _findSelectedAltBomItems(altHeaderId) {
            var g = $scope.selectedItems.altbom[altHeaderId.toString()];
            if (!g) {
              return [];
            }
            return _.chain(g.parts)
              .reduce(function(memo, v, k) {
                if (v === true) {
                  memo.push(parseInt(k));
                }
                return memo;
              }, []).value();
          }

          function _initBoms(bom) {
            $scope.selectedItems.bom.allBomChecked = false;
            $scope.selectedItems.bom.parts = {};
            // As BOM has a complex primary key which is not suitable
            // for processing in UI, we are assigning artificial "_rowid"
            // unique property.
            var rowid = 1;
            _.each(bom, function(b) {
              b._rowid = rowid++;
            });
            $scope.bom.splice(0, $scope.bom.length);
            $scope.bom.push.apply($scope.bom, bom);
            //$scope.bomTableParams.reload();
            $scope.bomTableParams.settings({
              dataset: $scope.bom
            });
          }

          $scope.bomTableParams = new NgTableParams({
            page: 1,
            count: 10,
            sorting: {'partNumber': 'asc'}
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

          $scope.$watch('selectedItems.bom.allBomChecked', function(val) {
            if ($scope.bom) {
              _.each($scope.bom, function(b) {
                $scope.selectedItems.bom.parts[b._rowid] = val;
              });
            }
          });

          $scope.isModifying = function(b) {
            return angular.isDefined($scope.modifyValues[b.part.partId]);
          };

          $scope.modifyStart = function(b) {
            $scope.modifyValues[b.part.partId] = b.qty;
          };

          $scope.modifyCancel = function(b) {
            delete $scope.modifyValues[b.part.partId];
          };

          $scope.modifySave = function(b) {
            var quantity = $scope.modifyValues[b.part.partId];
            restService.updateBom($scope.parentPartId, b.part.partId, quantity).then(
              function() {
                b.qty = quantity;
                delete $scope.modifyValues[b.part.partId];
              },
              function() {}
            );
          };

          $scope.isThereAnySelectedBomItem = function() {
            if ($scope.selectedItems.bom && $scope.selectedItems.bom.parts) {
              for (var k in $scope.selectedItems.bom.parts) {
                if ($scope.selectedItems.bom.parts.hasOwnProperty(k) &&
                  $scope.selectedItems.bom.parts[k])
                {
                  return true;
                }
              }
            }
            return false;
          };

          $scope.onRemoveSelectedBoms = function() {
            dialogs.confirm('Delete BOM item(s)?',
              'Do you really want to remove selected part(s) from the BOM? ' +
              'Corresponding alternative BOM will be removed too.').result
              .then(
                function yes() {
                  var children = _findSelectedBomItems();
                  var ids = _.map(children, function(b) { return b.part.partId; });
                  restService.removeBomItems($scope.parentPartId, ids).then(
                    function success(updatedBom) {
                      _initBoms(updatedBom);
                      toastr.success('Child part(s) removed from the BOM.');
                    },
                    function failure(response) {
                      restService.error('Removing of the BOM item(s) failed.', response);
                    }
                  );
                }
              );
          };

          $scope.hasAnyAltBomGroup = function() {
            return $scope.altBoms && $scope.altBoms.length > 0 && $scope.altBoms[0].altHeaderId;
          };

          function _findAltBomGroup(altHeaderId) {
            if ($scope.altBoms) {
              return _.find($scope.altBoms, function(grp) {
                return grp.altHeaderId === altHeaderId;
              });
            }
            return undefined;
          }

          $scope.onCheckAllAltBomItems = function(altHeaderId) {
            var k = altHeaderId.toString();
            var checked = $scope.selectedItems.altbom[k].allAltBomChecked;
            var grp = _findAltBomGroup(altHeaderId);
            if (grp) {
              _.each(grp.ngTableParams.data, function(p) {
                $scope.selectedItems.altbom[k].parts[p.partId] = checked;
              });
            }
          };

          $scope.newAltGroup = function() {
            restService.createAltBomGroup($scope.parentPartId, $scope.highlightedBom.part.partId).then(
              function success(partGroups) {
                _updateAltBoms(partGroups);
                 toastr.success('The alternative BOM group has been successfully created.');
              },
              function failure(response) {
                restService.error('Creation of a new BOM alternate group failed.', response);
              }
            );
          };

          $scope.isThereAnySelectedAltBomItem = function(altHeaderId) {
            var g = $scope.selectedItems.altbom[altHeaderId.toString()];
            if (g) {
              for (var k in g.parts) {
                if (g.parts.hasOwnProperty(k) && g.parts[k]) {
                  return true;
                }
              }
            }
            return false;
          };

          $scope.onRemoveSelectedAltBoms = function(altHeaderId) {
            dialogs.confirm('Delete item(s) in the alternate BOM?',
              'Do you really want to remove selected part(s) from the alternate BOM? ').result.then(
                function yes() {
                  var ids = _findSelectedAltBomItems(altHeaderId);
                  restService.removeAltBomItems($scope.parentPartId, $scope.highlightedBom.part.partId, altHeaderId, ids).then(
                    function success(partGroups) {
                      _updateAltBoms(partGroups);
                      toastr.success('Child part(s) removed from the alternative BOM.');
                    },
                    function failure(response) {
                      restService.error('Removing of the item(s) from alternative BOM failed.', response);
                    }
                  );
                }
              );
          };

          $scope.removeAltGroup = function(altHeaderId) {
            dialogs.confirm('Confirmation', 'Are you sure? Do you really want to remove this group ' +
              'of alternative BOM [' + altHeaderId + ']?').result.then(
              function yes() {
                restService.deleteAltBomGroup($scope.parentPartId, $scope.highlightedBom.part.partId, altHeaderId).then(
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
            $location.path('/part/' + $scope.parentPartId + '/bom/' + $scope.highlightedBom.part.partId + '/alt/' + altHeaderId);
          };

          function _updateAltBoms(partGroups) {
            $scope.selectedItems.altbom = {};
            var result = [];
            _.chain(partGroups)
              //.filter(function(pg) { return pg && pg.id; }) // TODO: hack
              .each(function(pg) {
                result.push({
                  altHeaderId: pg.id,
                  ngTableParams: new NgTableParams(
                    {
                      page: 1,
                      count: 10,
                      sorting: {'partNumber': 'asc'}
                    }, {
                      dataset: pg.parts
                    }
                  )
                });
                $scope.selectedItems.altbom[pg.id.toString()] = {allAltBomChecked: false, parts:{}};
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
            if ($scope.highlightedBom && b._rowid === $scope.highlightedBom._rowid) {
              // debounce
              return;
            }
            restService.getBomAlternatives($scope.parentPartId, b.part.partId).then(
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
