"use strict";

angular.module("ngMetaCrudApp").controller("ParentBomSearchCtrl", [
  "$log", "$scope", "ngTableParams", "dialogs", "restService", "utils", "part", "partTypes", "boms",
  function ($log, $scope, ngTableParams, dialogs, restService, utils, part, partTypes, boms) {

    $scope.part = part;
    $scope.partTypes = partTypes;

    var pickedParts = [];
    var pickedPartIds = {
    };

    $scope.bomTableParams = new ngTableParams({
      page: 1,
      count: 10
    }, {
      getData: utils.localPagination(boms, "child.manufacturerPartNumber")
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

    $scope.removeBom = function(idx) {
      var bomItem = boms[idx];
      dialogs.confirm(
        "Remove BOM Item?",
        "Remove child part from this bill of materials?").result.then(
        function() {
          // Yes
          Restangular.one("bom", bomItem.id).remove().then(
            function() {
              boms.splice(idx, 1);
              $scope.bomTableParams.reload();
              gToast.open("Child part removed from BOM.");
            },
            restService.error
          );
        }
      );
    };

}]);
