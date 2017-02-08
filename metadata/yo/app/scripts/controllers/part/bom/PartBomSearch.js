"use strict";

angular.module("ngMetaCrudApp")

.constant("BOM_RESULT_STATUS", {
  OK: "OK",
  ASSERTION_ERROR: "ASSERTION_ERROR",
  FOUND_BOM_RECURSION: "FOUND_BOM_RECURSION"
})

.controller("PartBomSearchCtrl", ["$log", "$scope", "$location", "ngTableParams", "$routeParams", "$uibModal",
  "User", "restService", "Restangular", "dialogs", "gToast", "utils", "partTypes", "part", "boms",
  function($log, $scope, $location, ngTableParams, $routeParams, $uibModal, User, restService,
    Restangular, dialogs, gToast, utils, partTypes, part, boms) {

    $scope.partTypes = partTypes;
    $scope.restService = restService;
    $scope.partId = $routeParams.id;

    var pickedParts = [];
    var pickedPartIds = {};

    // The part whose bom we're editing
    $scope.part = part;

    $scope.boms = boms; // boms of the current part

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

    $scope.isBttnUnpickAllDisabled = function() {
      return pickedParts.length === 0;
    };
//part.manufacturer.id != $part._source.manufacturer.id || part.id == $part._source.id || $part._source.id == pickedPart.id
    $scope.isBttnPickDisabled = function(p) {
      return false; // TODO
      return p === undefined || $scope.part.manufacturer.id != p.manufacturer.id ||
        $scope.part.id == p.id || pickedPartIds[p.id] || parentPartsIds[p.id] ||
        restService.status.bomRebuilding;
    };

    $scope.save = function() {
      var authorized = User.hasRole("ROLE_CHLOGSRC_READ") && User.hasRole("ROLE_CHLOGSRCNAME_READ");
      if (authorized) {
        $uibModal.open({
          templateUrl: "/views/chlogsrc/LinkDlg.html",
          animation: false,
          size: "lg",
          controller: "ChlogSrcLinkDlgCtrl",
          backdrop: 'static',
          keyboard: false,
          resolve: {
            "partId": function () {
              return $scope.partId;
            },
            "bomItem": function () {
              return $scope.bomItem;
            },
            "sourcesNames": restService.getAllChangelogSourceNames(),
            "lastPicked": restService.getLastPickedChangelogSources,
            "begin": function() {
              return restService.changelogSourceBeginEdit(); // needs to clear session attribute on the server side
            },
            "cancelUrl": function() {
              return "/part/" + $scope.partId + "/bom/search";
            }
          }
        });
      } else {
        dialogs.error("Not authorized", "To complete this operation you must have at least following roles: " +
          "ROLE_CHLOGSRC_READ, ROLE_CHLOGSRCNAME_READ.");
      }
    };

  }
]);
