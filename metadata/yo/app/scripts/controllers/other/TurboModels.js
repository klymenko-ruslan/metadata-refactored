"use strict";

angular.module("ngMetaCrudApp")
  .controller("TurboModelsCtrl", ["$scope", "dialogs", "$log", "gToast", "restService",
  function($scope, dialogs, $log, gToast, restService) {
    $scope.selection = {
      manufacturer: null,
      turboType: null,
      turboModel: null
    };

    $scope.turboTypes = null;
    $scope.turboModels = null;

    $scope.createTurboType = function() {

      var data = {
        title: "Create Turbo Type",
        respondButton: "Create"
      };

      dialogs.create("/views/dialog/NameDialog.html", "NameDialogCtrl", data).result.then(function(name) {
        restService.createTurboType($scope.selection.manufacturer.id, name).then(
          function() {
            gToast.open("Turbo type created.");
            $scope.loadTurboTypes($scope.selection.manufacturer.id);
          },
          function(response) {
            restService.error("Could not create turbo type.", response);
          }
        );
      });

    };

    $scope.renameTurboType = function(turboType) {

      var data = {
        title: "Rename Turbo Type",
        respondButton: "Rename",
        name: turboType.name
      };

      dialogs.create("/views/dialog/NameDialog.html", "NameDialogCtrl", data).result.then(function(name) {
        turboType.name = name;
        restService.renameTurboType(turboType).then(
          function() {
            gToast.open("Turbo type renamed.");
              $scope.selection.turboType = null;
            $scope.loadTurboTypes($scope.selection.manufacturer.id);
          },
          function(response) {
            restService.error("Could not rename turbo type.", response);
          });
      });
    };

    $scope.deleteTurboType = function(turboType) {
        dialogs.confirm(
          "Delete Turbo Type?",
          "Do you want to delete this turbo type?").result.then(
          function() {
            // Yes
            restService.deleteTurboType(turboType.id).then(
              function() {
                // Success
                gToast.open("Turbo type deleted.");
                $scope.selection.turboType = null;
                $scope.loadTurboTypes($scope.selection.manufacturer.id);
              },
              function() {
                // Error
                dialogs.error(
                  "Could not delete turbo type.",
                  "Turbo type must not be used for any parts or turbo models. Check server log for details.");
              });
          },
          function() {
            // No
          });
    };

    $scope.createTurboModel = function() {

      var data = {
        title: "Create Turbo Model",
        respondButton: "Create"
      };

      dialogs.create("/views/dialog/NameDialog.html", "NameDialogCtrl", data).result.then(function(name) {
        restService.createTurboModel($scope.selection.turboType.id, name).then(
          function() {
            gToast.open("Turbo model created.");
            $scope.loadTurboModels($scope.selection.turboType.id);
          },
          function(response) {
            restService.error("Could not create turbo model.", response);
          }
        );
      });
    };

    $scope.renameTurboModel = function(turboModel) {

      var data = {
        title: "Rename Turbo Model",
        respondButton: "Rename",
        name: turboModel.name
      };

      dialogs.create("/views/dialog/NameDialog.html", "NameDialogCtrl", data).result.then(function(name) {
        turboModel.name = name;
        restService.renameTurboModel(turboModel).then(
          function() {
            gToast.open("Turbo model renamed.");
            $scope.selection.turboModel = null;
            $scope.loadTurboModels($scope.selection.turboType.id);
          },
          function(response) {
            restService.error("Could not rename turbo model.", response);
          });
      });
    };

    $scope.deleteTurboModel = function(turboModel) {
        dialogs.confirm(
          "Delete Turbo Model?",
          "Do you want to delete this turbo model?").result.then(
          function() {
            // Yes
            restService.deleteTurboModel(turboModel.id).then(
              function() {
                // Success
                gToast.open("Turbo model deleted.");
                $scope.selection.turboModel = null;
                $scope.loadTurboModels($scope.selection.turboType.id);
              },
              function(response) {
                // Error
                dialogs.error(
                  "Could not delete turbo model.",
                  "Turbo model must not be used for any parts. Check server log for details.");
              });
          },
          function() {
            // No
          });

    };

    $scope.loadTurboTypes = function(newMfrId) {
      $scope.turboTypes = null;
      $scope.turboModels = null;
      $scope.selection.turboType = null;
      $scope.selection.turboModel = null;

      if (angular.isDefined(newMfrId)) {
        $log.log("Fetching new turbo types for manufacturer", newMfrId);
        $scope.turboTypes = restService.listTurboTypesForManufacturerId(newMfrId).$object;
      }
    };

    $scope.loadTurboModels = function(newTurboTypeId) {
      $scope.turboModels = null;
      $scope.selection.turboModel = null;

      if (angular.isDefined(newTurboTypeId)) {
        $log.log("Fetching new turbo models for type", newTurboTypeId);
        $scope.turboModels = restService.listTurboModelsForTurboTypeId(newTurboTypeId).$object;
      }
    };

    $scope.$watch("selection.manufacturer.id", $scope.loadTurboTypes);

    $scope.$watch("selection.turboType.id", $scope.loadTurboModels);
  }]);
