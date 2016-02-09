"use strict";

angular.module("ngMetaCrudApp")

  .controller("mergeInterchangablesCtrl", ["$scope", "$uibModalInstance", "data", function($scope, $uibModalInstance, data) {
    $scope.mergeChoice = data["mergeChoice"];
    $scope.cancel = function() {
      $uibModalInstance.dismiss("Canceled");
    };
    $scope.doIt = function() {
      $uibModalInstance.close($scope.mergeChoice);
    };
  }])

  .controller("PartInterchangeSearchCtrl", ["$log", "$scope", "$location", "$routeParams", "restService",
      "Restangular", "gToast", "dialogs", function($log, $scope, $location, $routeParams,
        restService, Restangular, gToast, dialogs) {
    //$scope.restService = restService;
    $scope.partId = $routeParams.id;
    // The part whose interchange we're editing
    $scope.promise = restService.findPart($scope.partId).then(function(part) {
      $scope.part = part;
    });
    $scope.pick = function(pickedPartId) {
      // Lookup the picked part
      $scope.pickedPartPromise = restService.findPart(pickedPartId).then(
        function(pickedPart) {
          var partType = $scope.part.partType.name;
          var pickedPartType = pickedPart.partType.name;
          // Check part type and add the picked part
          if (partType !== pickedPartType) {
            dialogs.confirm("Confirm Interchange Part Type",
              "This part and picked one have different types.\n" +
              "Are you sure you want to make the picked " +
                pickedPartType + " interchangeable with this " + partType + "?")
              .result.then(function() {
                $scope.pickedPart = pickedPart;
              });
          } else {
            $scope.pickedPart = pickedPart;
          }
        },
        function(response) {
          dialogs.error("Could not load part details.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
        });
    };

    $scope.save = function() {
      var partAlreadyHasInterchange = angular.isObject($scope.part.interchange);
      if (partAlreadyHasInterchange) {
        if (partAlreadyHasInterchange && !$scope.pickedPart.interchange.alone) {
          // Join the other part's interchange group
          var mergeDialog = dialogs.create("mergeInterchangablesDlg.html", "mergeInterchangablesCtrl", {mergeChoice: 3}, {
            size: 'lg',
            keyboard: true,
            backdrop: false
          });
          mergeDialog.result.then(
            function(mergeChoice) {
              //alert("mergeChoice: " + mergeChoice);
              restService.updatePartInterchange($scope.partId, $scope.pickedPart.interchange.id, mergeChoice).then(
                function() {
                  gToast.open("Interchangeable part group changed.");
                  $location.path("/part/" + $scope.partId);
                },
                function(response) {
                  dialogs.error("Could not change interchangeable part group.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                }
              );
            },
            function() {
              $log.log("Cancelled.");
            }
          );
        } else {
          // Add part to this interchange group
          restService.updatePartInterchange($scope.pickedPart.id, $scope.part.interchange.id).then(
            function() {
              gToast.open("Added picked part to interchange.");
              $location.path("/part/" + $scope.partId);
            },
            restService.error);
        }
      } else if ($scope.pickedPart.interchange) {
        // This case equals to mergeCoice=2 --

        // Add this part to the picked part's interchange
        restService.updatePartInterchange($scope.part.id, $scope.pickedPart.interchange.id, 2).then(
          function() {
            gToast.open("Added part to picked part's interchanges.");
            $location.path("/part/" + $scope.partId);
          },
          restService.error);
      } else {
        // Create
        restService.createPartInterchange($scope.part.id, $scope.pickedPart.id).then(
          function() {
            gToast.open("Interchangeable part group changed added.");
            $location.path("/part/" + $scope.partId);
          },
          function(response) {
            dialogs.error("Could not add interchangeable part.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
          }
        );
      }
    };

    $scope.canSave = function() {
      // No Picked Part
      if ($scope.pickedPart == null) {
        return false;
      }
      if ($scope.part.id === $scope.pickedPart.id) {
        return false;
      }
      // Same interchange
      if ($scope.part.interchange && $scope.pickedPart.interchange && $scope.part.interchange.id === $scope.pickedPart.interchange.id) {
        return false;
      }
      return true;
    };

    $scope.canClear = function() {
      return $scope.part && $scope.part.interchange;
    };

    $scope.removeInterchange = function() {
      dialogs.confirm(
          "Remove from interchangeable part group?",
          "Other parts in the group will not be modified.")
        .result.then(
          function() {
            restService.deletePartInterchange($scope.partId, $scope.part.interchangeId).then(
//            Restangular.setParentless(false);
//            Restangular.one('interchange', $scope.part.interchangeId).one('part', $scope.partId).remove().then(
              function() {
                // Success
                gToast.open("Part removed from interchange.");
                $location.path("/part/" + $scope.partId);
              },
              function(response) {
                dialogs.error("Could not remove part from interchange", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
              });
          });
    };
  }]);
