"use strict";

angular.module("ngMetaCrudApp")
  .controller("ParentBomSearchCtrl", [
    "$log", "$scope", "$location", "ngTableParams", "$uibModal", "dialogs", "gToast", "restService",
    "BOM", "utils", "part", "partTypes", "parents",
    function ($log, $scope, $location, ngTableParams, $uibModal, dialogs, gToast, restService,
              BOM, utils, part, partTypes, parents)
    {

      $scope.part = part; // primary part
      $scope.partTypes = partTypes;
      $scope.restService = restService;

      var pickedParts = [];
      var pickedPartIds = {};
      var parentPartsIds = null;

      function updateParentPartsIds() {
        parentPartsIds = {};
        _.each(parents, function(bi) {
          parentPartsIds[bi.parent.id] = true;
        });
      };

      updateParentPartsIds();

      $scope.showPart = function(partId) {
        $location.path("/part/" + partId);
      }

      $scope.bomTableParams = new ngTableParams({
        page: 1,
        count: 10
      }, {
        getData: utils.localPagination(parents, "child.manufacturerPartNumber")
      });

      $scope.pickedPartsTableParams = new ngTableParams(
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
        return pickedParts.length === 0 || restService.status.bomRebuilding;
      };

      $scope.isBttnPickDisabled = function(p) {
        return p === undefined || $scope.part.manufacturer.id != p.manufacturer.id ||
          $scope.part.id == p.id || pickedPartIds[p.id] || parentPartsIds[p.id] ||
          restService.status.bomRebuilding;
      };

      $scope.save = function() {
        var rows = _.map(pickedParts, function(p) {
          return {
            partId: p.id,
            quontity: p.extra.qty,
            resolution: p.extra.resolution
          };
        });
        BOM.addToParentsBOMs($scope.part.id, { rows: rows }).then(
          function success(response) {
            parents.splice(0, parents.length);
            _.each(response.parents, function(b) {
              parents.push(b);
            });
            updateParentPartsIds();
            $scope.bomTableParams.reload();
            _.each(pickedParts, function(p) {
              delete pickedPartIds[p.id];
            });
            pickedParts.splice(0, pickedParts.length);
            $scope.pickedPartsTableParams.reload();
            if (response.failures.length > 0) {
              $uibModal.open({
                templateUrl: "/views/part/bom/FailedBOMsDlg.html",
                animation: false,
                size: "lg",
                controller: "FailedBOMsDlgCtrl",
                resolve: {
                  primaryPart: function() {
                    return $scope.part;
                  },
                  failures: function() {
                    return response.failures;
                  }
                }
              });
            } else {
              gToast.open("The part has been successfully added to " + response.added +
                " parents to their BOM lists.");
            }
          },
          function failure(error) {
            restService.error("Can't add the part to parent BOM's.", error);
          }
        );
      };

      $scope.pick = function(pickedPart) {
        BOM.listByParentPartAndTypeIds(pickedPart.id, $scope.part.partType.id).then(
          function success(boms) {
            var resolution = null;
            if (boms.length === 0) {
              resolution = "ADD";
            }
            pickedPart.extra = {
              qty: 1,
              resolution: resolution,
              existingBoms: boms
            };
            pickedParts.push(pickedPart);
            pickedPartIds[pickedPart.id] = true;
            $scope.pickedPartsTableParams.reload();
          },
          function failure(error) {
            restService.error("Can't pick the part.", error);
          }
        );
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

      $scope.removeBOM = function(bomId) {
        var idx = _.findIndex(parents, function(b) {
          return b.id === bomId;
        });
        var bomItem = parents[idx];
        dialogs.confirm(
          "Remove BOM Item?",
          "Remove this child part from the bill of materials of the parent part?").result.then(
          function() {
            // Yes
            BOM.removeBOM(bomId).then(
              function() {
                parents.splice(idx, 1);
                $scope.bomTableParams.reload();
                gToast.open("The BOM has been successfully removed.");
              },
              restService.error
            );
          }
        );
      };

      $scope.displayExistingBOMs = function(pickedPart) {
        $uibModal.open({
          templateUrl: "/views/part/bom/ExistingBOMsDlg.html",
          animation: false,
          size: "lg",
          controller: "ExistingBOMsDlgCtrl",
          resolve: {
            existingBoms: function() {
              return pickedPart.extra.existingBoms;
            }
          }
        });
      };

    }
  ])
  .controller("ExistingBOMsDlgCtrl", ["$scope", "$log", "$location", "$uibModalInstance", "ngTableParams",
      "utils", "existingBoms",
    function($scope, $log, $location, $uibModalInstance, ngTableParams, utils, existingBoms) {

      $scope.bomTableParams = new ngTableParams({
        page: 1,
        count: 10
      }, {
        getData: utils.localPagination(existingBoms, "child.manufacturerPartNumber")
      });

      $scope.onClose = function() {
        $uibModalInstance.close();
      };

      $scope.showPart = function(partId) {
        $scope.onClose();
        $location.path("/part/" + partId);
      }

  }]).controller("FailedBOMsDlgCtrl", ["$scope", "$log", "$location", "$uibModalInstance", "ngTableParams",
      "utils", "primaryPart", "failures",
    function($scope, $log, $location, $uibModalInstance, ngTableParams, utils, primaryPart, failures) {

      $scope.primaryPart = primaryPart;

      $scope.failuresTableParams = new ngTableParams({
        page: 1,
        count: 10
      }, {
        getData: utils.localPagination(failures, "manufacturerPartNumber")
      });

      $scope.onClose = function() {
        $uibModalInstance.close();
      };

      $scope.showPart = function(partId) {
        $scope.onClose();
        $location.path("/part/" + partId);
      }

  }]);
