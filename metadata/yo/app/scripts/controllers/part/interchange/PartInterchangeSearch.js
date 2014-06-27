'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartInterchangeSearchCtrl', function ($log, $scope, $location, $routeParams, restService, Restangular, gToast, $dialogs) {
      $scope.partId = $routeParams.id;

      // The part whose interchange we're editing
      $scope.promise = restService.findPart($scope.partId).then(function (part) {
        $scope.part = part;
      });

      $scope.pick = function (pickedPartId) {
        $log.log("Picked part", pickedPartId);

        // Lookup the picked part
        $scope.pickedPartPromise = restService.findPart(pickedPartId).then(
            function (pickedPart) {
              $log.log("Loaded picked part", pickedPart);
              var partType = $scope.part.partType.name;
              var pickedPartType = pickedPart.partType.name;

              // Check part type and add the picked part
              if (partType !== pickedPartType) {
                $dialogs.confirm("Confirm Interchange Part Type",
                                 "Are you sure you want to make the picked " + pickedPartType + " interchangeable with this " + partType + "?")
                  .result.then(function() {
                    $scope.pickedPart = pickedPart;
                  });
              } else {
                $scope.pickedPart = pickedPart;
              }
            },
            function(response) {
              $dialogs.error("Could not load part details.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
            });
      }

      $scope.save = function() {

        if ($scope.part.interchange) {

          // Update
          if ($scope.pickedPart.interchange && $scope.pickedPart.interchange.parts.length > 0) {

            // Join the other part's interchange group
            $dialogs.confirm(
                    "Change interchangeable part group?",
                    "The part will no longer be interchangeable with it's current interchange parts.")
                .result.then(function() {
                  Restangular.setParentless(false);
                  Restangular.one('interchange', $scope.pickedPart.interchange.id).one('part', $scope.partId).put().then(
                      function() {
                        gToast.open("Interchangeable part group changed.");
                        $location.path("/part/" + $scope.partId);
                      },
                      function(response) {
                        $dialogs.error("Could not change interchangeable part group.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                      });
              });
          } else {

            // Add part to this interchange group
            Restangular.setParentless(false);
            Restangular.one('interchange', $scope.part.interchange.id).one('part', $scope.pickedPart.id).put().then(
                function() {
                  gToast.open("Added picked part to interchange.");
                  $location.path("/part/" + $scope.partId);
                },
                restService.error);
          }
        } else {
          if ($scope.pickedPart.interchange) {

            // Add this part to the picked part's interchange
            Restangular.setParentless(false);
            Restangular.one('interchange', $scope.pickedPart.interchange.id).one('part', $scope.part.id).put().then(
                function() {
                  gToast.open("Added part to picked part's interchanges.");
                  $location.path("/part/" + $scope.partId);
                },
                restService.error);
          } else {

            // Create
            var interchange = {
              parts: [
                {id: $scope.part.id},
                {id: $scope.pickedPart.id}
              ]
            };

            Restangular.all('interchange').post(interchange).then(
                function() {
                  gToast.open("Interchangeable part group changed added.");
                  $location.path("/part/" + $scope.partId);
                },
                function(response) {
                  $dialogs.error("Could not add interchangeable part.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                });
          }
        }
      }

      $scope.canSave = function() {

        // No Picked Part
        if ($scope.pickedPart == null) {
          return false;
        }

        if ($scope.part.id === $scope.pickedPart.id) {
          return false;
        }

        // Same interchange
        if ($scope.part.interchange && $scope.pickedPart.interchange
            && $scope.part.interchange.id === $scope.pickedPart.interchange.id) {
          return false;
        }

        return true;
      }

      $scope.canClear = function () {
        return $scope.part && $scope.part.interchange;
      }

      $scope.removeInterchange = function () {
        $log.log("clear");
        $dialogs.confirm(
                "Remove from interchangeable part group?",
                "Other parts in the group will not be modified.")
            .result.then(
                function() {
                  Restangular.setParentless(false);
                  Restangular.one('interchange', $scope.part.interchangeId).one('part', $scope.partId).remove().then(
                      function () {
                        // Success
                        gToast.open("Part removed from interchange.");
                        $location.path("/part/" + $scope.partId);
                      },
                      function (response) {
                        $dialogs.error("Could not remove part from interchange", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                      });
                });
      }
    });
