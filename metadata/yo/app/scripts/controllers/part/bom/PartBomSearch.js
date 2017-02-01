"use strict";

angular.module("ngMetaCrudApp")

.constant("BOM_RESULT_STATUS", {
  OK: "OK",
  ASSERTION_ERROR: "ASSERTION_ERROR",
  FOUND_BOM_RECURSION: "FOUND_BOM_RECURSION"
})

.controller("PartBomSearchCtrl", ["$log", "$scope", "$location", "$routeParams", "$uibModal",
  "BOM", "restService", "Restangular", "dialogs", "gToast", "User", "partTypes", "part",
  function($log, $scope, $location, $routeParams, $uibModal, BOM, restService,
    Restangular, dialogs, gToast, User, partTypes, part) {
    $scope.partTypes = partTypes;
    $scope.restService = restService;
    $scope.partId = $routeParams.id;

    $scope.pickedPart = null;
    $scope.showPickedPart = false;

    // The part whose bom we're editing
    $scope.part = part;

    // Load the part's BOM
    $scope.bom = BOM.listByParentPartId($scope.partId).then(
      function(bom) {
        $scope.bom = bom;
      },
      restService.error
    );

    $scope.bomItem = {
      parentPartId: $scope.partId,
      childPartId: null,
      quantity: 1
    };

    $scope.save = function() {
      if (User.hasRole("ROLE_CHLOGSRC_SKIP")) {
        restService.createBom($scope.bomItem, null, null, null).then(
          function(bomResult) {
            if (bomResult.status == BOM_RESULT_STATUS.OK) {
              // Success
              gToast.open("BOM item added.");
              $location.path("/part/" + $scope.partId);
            } else if (bomResult.status == BOM_RESULT_STATUS.ASSERTION_ERROR) {
              dialogs.error("Validation error", bomResult.message);
            } else if (bomResult.status == BOM_RESULT_STATUS.FOUND_BOM_RECURSION) {
              dialogs.error("Validation error", bomResult.message);
            } else {
              dialogs.error("Internal error", "Server returned unknown status of the operation: " + bomResult.status);
            }
          },
          function(response) {
            dialogs.error("Could not add BOM Item", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
          }
        );
      } else {
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
            }
          }
        });
      }
    };

    $scope.pickBomItemPart = function(bomItemPartId, allowed) {
      if (allowed) {
        $scope.pickedPart = restService.findPart(bomItemPartId).then(
          function(pickedPart) {
            $scope.pickedPart = pickedPart;
            $scope.bomItem.childPartId = $scope.pickedPart.id;
          },
          function(errorResponse) {
            restService.error("Could not pick part.", errorResponse);
            $log.log("Could not pick part", errorResponse);
          }
        );
      } else {
        dialogs.error("Validation error", "Child part must have the same manufacturer as the Parent part.");
      }
    };

  }
]);
