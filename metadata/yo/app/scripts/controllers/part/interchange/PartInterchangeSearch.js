"use strict";

angular.module("ngMetaCrudApp")

  .constant("MERGE_OPTIONS", {
    "PICKED_ALONE_TO_PART": 1,  // Add picked part to interchange group of this part and remove picked part from its existing interchange
    "PART_ALONE_TO_PICKED": 2,  // Add this part to interchange group of the picked part and remove this part from its existing interchange
    "PICKED_ALL_TO_PART": 3     // Add the picked part and all its existing interchange parts to interchange group of this part
  })

  .controller("mergeInterchangeablesCtrl", ["$scope", "$uibModalInstance", "data", "MERGE_OPTIONS", function($scope, $uibModalInstance, data, MERGE_OPTIONS) {
    $scope.mergeOptions = MERGE_OPTIONS;
    $scope.mergeChoice = data["mergeChoice"];
    $scope.cancel = function() {
      $uibModalInstance.dismiss("Canceled");
    };
    $scope.doIt = function() {
      $uibModalInstance.close($scope.mergeChoice);
    };
  }])

  .controller("PartInterchangeSearchCtrl", ["$log", "$scope", "$location", "$routeParams", "restService",
      "Restangular", "gToast", "dialogs", "MERGE_OPTIONS", function($log, $scope, $location, $routeParams,
        restService, Restangular, gToast, dialogs, MERGE_OPTIONS) {
    //$scope.restService = restService;
    $scope.partId = $routeParams.id;
    // The part whose interchange we're editing
    $scope.promise = restService.findPart($scope.partId).then(function(part) {
      $scope.part = part;
    });

    $scope.go = function(path) {
      $location.path(path);
    };

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
      if ($scope.part.interchange) {
        if (!$scope.pickedPart.interchange || $scope.pickedPart.interchange.alone) {
          // Add part to this interchange group
          restService.updatePartInterchange($scope.partId, $scope.pickedPart.id, MERGE_OPTIONS.PICKED_ALONE_TO_PART).then(
            function() {
              gToast.open("Added picked part to interchange.");
              $location.path("/part/" + $scope.partId);
            },
            restService.error);
        } else {
          // In this case there are several possibilities how interchangeables can be merged.
          // See ticket #484.
          var mergeDialog = dialogs.create("/views/dialog/MergeInterchangeablesDlg.html", "mergeInterchangeablesCtrl",
            {
              "mergeChoice": MERGE_OPTIONS.PICKED_ALL_TO_PART
            }, {
              "size": "lg",
              "keyboard": true,
              "backdrop": false
            }
          );
          mergeDialog.result.then(
            function(mergeChoice) {
              restService.updatePartInterchange($scope.partId, $scope.pickedPart.id, mergeChoice).then(
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
        }
      } else if ($scope.pickedPart.interchange) {
        // Add this part to the picked part's interchange
        restService.updatePartInterchange($scope.partId, $scope.pickedPart.id, MERGE_OPTIONS.PART_ALONE_TO_PICKED).then(
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
