"use strict";

angular.module("ngMetaCrudApp")

.constant("BOM_RESULT_STATUS", {
  OK: "OK",
  ASSERTION_ERROR: "ASSERTION_ERROR",
  FOUND_BOM_RECURSION: "FOUND_BOM_RECURSION"
})

.controller("PartBomSearchCtrl", ["$log", "$scope", "$location", "$routeParams", "$uibModal",
  "BOM", "restService", "Restangular", "dialogs", "gToast", "partTypes", "part",
  function($log, $scope, $location, $routeParams, $uibModal, BOM, restService,
    Restangular, dialogs, gToast, partTypes, part) {
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
          "begin": function() {
            return restService.chanlelogSourceBeginEdit(); // needs to clear session attribute on the server side
          }
        }
      });

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
