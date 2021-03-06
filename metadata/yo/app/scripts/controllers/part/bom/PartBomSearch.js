'use strict';

angular.module('ngMetaCrudApp')

.controller('PartBomSearchCtrl', ['$log', '$scope', '$location', 'NgTableParams', '$routeParams', '$uibModal',
  'User', 'BOM', 'restService', 'dialogs', 'toastr', 'partTypes', 'part', 'boms',
  'services', 'LinkSource',
  function($log, $scope, $location, NgTableParams, $routeParams, $uibModal, User, BOM, restService,
    dialogs, toastr, partTypes, part, boms, services, LinkSource) {

    $scope.partTypes = partTypes;
    $scope.restService = restService;
    $scope.partId = $routeParams.id;
    $scope.part = part; // The part whose bom we're editing

    $scope.requiredSource = LinkSource.isSourceRequiredForBOM(services);

    var pickedParts = [];
    var pickedPartIds = {};
    var existingBomPartIds = null;

    function updateExistingBomPartIds() {
      existingBomPartIds = {};
      _.each(boms, function(b) {
        existingBomPartIds[b.part.partId] = true;
      });
    }

    updateExistingBomPartIds();

    $scope.bomTableParams = new NgTableParams({
      page: 1,
      count: 5,
      sorting: {'child.manufacturerPartNumber': 'asc'}
    }, {
      counts: [5, 10, 15],
      dataset: boms
    });

    $scope.pickedPartsTableParams = new NgTableParams(
      {
        page: 1,
        count: 5,
        sorting: {}
      },
      {
        counts: [5, 10, 15],
        dataset: pickedParts
      }
    );

    $scope.pick = function(pickedPart) {
      pickedPart.extra = {
        qty: 1
      };
      pickedParts.push(pickedPart);
      pickedPartIds[pickedPart.id] = true;
      $scope.pickedPartsTableParams.settings({dataset: pickedParts});
    };

    $scope.unpick = function(partId) {
      var idx = _.findIndex(pickedParts, function(p) {
        return p.id === partId;
      });
      var p = pickedParts[idx];
      delete p.extra;
      pickedParts.splice(idx, 1);
      delete pickedPartIds[partId];
      $scope.pickedPartsTableParams.settings({dataset: pickedParts});
    };

    $scope.unpickAll = function() {
      _.each(pickedParts, function(pp) {
        delete pickedPartIds[pp.id];
      });
      pickedParts.splice(0, pickedParts.length);
      $scope.pickedPartsTableParams.settings({dataset: pickedParts});
    };

    $scope.isBttnSaveDisabled = function() {
      return pickedParts.length === 0;
    };

    $scope.isBttnUnpickAllDisabled = function() {
      return pickedParts.length === 0;
    };

    $scope.isBttnPickDisabled = function(p) {
      return p === undefined || $scope.part.manufacturer.id !== p.manufacturer.id ||
        $scope.partId === p.id || pickedPartIds[p.id] || existingBomPartIds[p.id];
    };

    /**
     * This is a callback that invoked when an user presses key 'Enter'
     * while focus is in an input field 'Part Number'
     * of a 'part search filter'.
     *
     * See also a view PartBomSearch.html and
     * an attribute 'on-press-enter-callback' in a declaration of a directive
     * 'part-search'.
     */
    $scope.cbPickOnEnter = function(searchResults) {
      // When a result of a search is a single part
      // and this part is suitable to be picked then
      // we pick its.
      if (searchResults && searchResults.hits.total === 1) {
        var rec = searchResults.hits.hits[0]._source;
        if (!$scope.isBttnPickDisabled(rec)) {
          $scope.pick(rec);
        }
      }
      // Return statement below
      // signals that default behaviour on the pressed 'Enter' key should
      // not be invoked.
      // See function 'onKeyUpInPartNumber' in the partSearch.js.
      return true;
    };

    function cbSave(srcIds, ratings, description, attachIds) {
      restService.createBom($scope.partId, pickedParts, srcIds, ratings, description, attachIds).then(
        function(response) {
          boms.splice(0, boms.length);
          _.each(response.boms, function(b) {
            boms.push(b);
          });
          updateExistingBomPartIds();
          $scope.bomTableParams.settings({dataset: boms});
          _.each(pickedParts, function(p) {
            delete pickedPartIds[p.id];
          });
          pickedParts.splice(0, pickedParts.length);
          $scope.pickedPartsTableParams.settings({dataset: pickedParts});
          if (response.failures.length > 0) {
            $uibModal.open({
              templateUrl: '/views/part/bom/FailedBOMsDlg.html',
              animation: false,
              size: 'lg',
              controller: 'FailedBOMsDlgCtrl',
              resolve: {
                message: function() {
                  return 'Following parts failed while adding to the BOM of the part [' + $scope.partId + '] - ' +
                    $scope.part.manufacturerPartNumber + ':';
                },
                failures: function() {
                  return response.failures;
                }
              }
            });
          } else {
            toastr.success('The BOMs have been successfully added to the part.');
          }
        },
        function(response) {
          dialogs.error('Could not add BOMs', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
        });
    }

    $scope.save = function() {
      LinkSource.link(cbSave, $scope.requiredSource, '/part/' + $scope.partId + '/bom/search');
    };

    $scope.removeBom = function(childPartId) {
      dialogs.confirm(
        'Remove BOM Item?',
        'Remove this child part from the bill of materials of the parent part?').result.then(
        function() {
          // Yes
          BOM.removeBom($scope.partId, childPartId).then(
            function(updatedBoms) {
              boms.splice(0, boms.length);
              boms.push.apply(boms, updatedBoms);
              $scope.bomTableParams.settings({dataset: boms});
              updateExistingBomPartIds();
              toastr.success('The BOM has been successfully removed.');
            },
            function(response) {
              dialogs.error('Could delete BOM', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
            }
          );
        }
      );
    };

  }
]);
