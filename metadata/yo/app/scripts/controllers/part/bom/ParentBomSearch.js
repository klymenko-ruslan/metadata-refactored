"use strict";

angular.module("ngMetaCrudApp").controller("ParentBomSearchCtrl", [
  "$log", "$scope", "ngTableParams", "dialogs", "gToast", "restService", "BOM", "utils", "part", "partTypes", "parents",
  function ($log, $scope, ngTableParams, dialogs, gToast, restService, BOM, utils, part, partTypes, parents) {

    $scope.part = part;
    $scope.partTypes = partTypes;
    $scope.restService = restService;

    var pickedParts = [];
    var pickedPartIds = {
    };

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

    $scope.bttnSaveDisabled = function() {
      return pickedParts.length === 0 || restService.status.bomRebuilding;
    };

    $scope.bttnPickDisabled = function(p) {
      return p === undefined || $scope.part.manufacturer.id != p.manufacturer.id ||
        $scope.part.id == p.id || pickedPartIds[p.id];
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
          $scope.bomTableParams.reload();
          _.each(pickedParts, function(p) {
            delete pickedPartIds[p.id];
          });
          pickedParts.splice(0, pickedParts.length);
          $scope.pickedPartsTableParams.reload();
          gToast.open("The part has been successfully added to " + response.added +
            " parents to their BOM lists. Failures: " + response.failed);
        },
        function failure(error) {
          restService.error("Can't add the part to parent BOM's.", error);
        }
      );
    };

    $scope.pick = function(part) {
      part.extra = {
        qty: 1,
        resolution: "ADD"
      };
      pickedParts.push(part);
      pickedPartIds[part.id] = true;
      $scope.pickedPartsTableParams.reload();
    };

    $scope.unpick = function(idx) {
      var p = pickedParts[idx];
      delete p.extra;
      pickedParts.splice(idx, 1);
      delete pickedPartIds[p.id];
      $scope.pickedPartsTableParams.reload();
    };

    $scope.removeBOM = function(idx) {
      var bomItem = parents[idx];
      dialogs.confirm(
        "Remove BOM Item?",
        "Remove this child part from the bill of materials of the parent part?").result.then(
        function() {
          // Yes
          BOM.removeBOM(bomItem.id).then(
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

}]);
