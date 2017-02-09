"use strict";

angular.module("ngMetaCrudApp")

.controller("PartBomSearchCtrl", ["$log", "$scope", "$location", "ngTableParams", "$routeParams", "$uibModal",
  "User", "BOM", "restService", "Restangular", "dialogs", "gToast", "utils", "partTypes", "part", "boms",
  function($log, $scope, $location, ngTableParams, $routeParams, $uibModal, User, BOM, restService,
    Restangular, dialogs, gToast, utils, partTypes, part, boms) {

    $scope.partTypes = partTypes;
    $scope.restService = restService;
    $scope.partId = $routeParams.id;
    $scope.part = part; // The part whose bom we're editing

    var pickedParts = [];
    var pickedPartIds = {};
    var existingBomPartIds = null;

    function updateExistingBomPartIds() {
      existingBomPartIds = {};
      _.each(boms, function(bi) {
        existingBomPartIds[bi.child.id] = true;
      });
    };

    updateExistingBomPartIds();

    $scope.bomTableParams = new ngTableParams({
      page: 1,
      count: 5
    }, {
        counts: [5, 10, 15],
      getData: utils.localPagination(boms, "child.manufacturerPartNumber")
    });

    $scope.pickedPartsTableParams = new ngTableParams(
      {
        page: 1,
        count: 5,
        sorting: {}
      },
      {
        counts: [5, 10, 15],
        getData: utils.localPagination(pickedParts)
      }
    );

    $scope.pick = function(pickedPart) {
      pickedParts.push(pickedPart);
      pickedPart.extra = {
        qty: 1
      };
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

    $scope.isBttnSaveDisabled = function() {
      return pickedParts.length === 0 || restService.status.bomRebuilding;
    };

    $scope.isBttnUnpickAllDisabled = function() {
      return pickedParts.length === 0;
    };

    $scope.isBttnPickDisabled = function(p) {
      return p === undefined || $scope.part.manufacturer.id != p.manufacturer.id ||
        $scope.part.id == p.id || pickedPartIds[p.id] || existingBomPartIds[p.id] ||
        restService.status.bomRebuilding;
    };

    function cbSave(srcIds, ratings, description) {
      restService.createBom($scope.partId, pickedParts, srcIds, ratings, description).then(
        function(response) {
          boms.splice(0, boms.length);
          _.each(response.boms, function(b) {
            boms.push(b);
          });
          updateExistingBomPartIds();
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
                message: function() {
                  return "Following parts failed while adding to the BOM of the part [" + $scope.partId + "] - " +
                    $scope.part.manufacturerPartNumber + ":";
                },
                failures: function() {
                  return response.failures;
                }
              }
            });
          } else {
            gToast.open("The BOMs have been successfully added to the part.");
          }
        },
        function(response) {
          dialogs.error("Could not add BOMs", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
        });
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
            "cbSave": function () {
              return cbSave;
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

    $scope.removeBOM = function(bomId) {
      var idx = _.findIndex(boms, function(b) {
        return b.id === bomId;
      });
      var bomItem = boms[idx];
      dialogs.confirm(
        "Remove BOM Item?",
        "Remove this child part from the bill of materials of the parent part?").result.then(
        function() {
          // Yes
          BOM.removeBOM(bomId).then(
            function() {
              boms.splice(idx, 1);
              $scope.bomTableParams.reload();
              updateExistingBomPartIds();
              gToast.open("The BOM has been successfully removed.");
            },
            restService.error
          );
        }
      );
    };

  }
]);
