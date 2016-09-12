"use strict";

angular.module("ngMetaCrudApp").controller("ParentBomSearchCtrl", [
  "$log", "$scope", "ngTableParams", "dialogs", "restService", "BOM", "utils", "part", "partTypes", "parents",
  function ($log, $scope, ngTableParams, dialogs, restService, BOM, utils, part, partTypes, parents) {

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
      $log.log("TODO: save");
    };

    $scope.pick = function(part) {
      part.extra = {
        qty: 1,
        resolution: "REPLACE"
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
