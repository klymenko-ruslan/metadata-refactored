'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartInterchangeSearchCtrl', function ($log, $scope, $location, $routeParams, restService, Restangular, gToast, $dialogs) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

        // The part whose interchange we're editing
        $scope.promise = restService.findPart($scope.partId).then(function (part) {
            $scope.part = part;
        });

        $scope.pick = function (pickedPartId) {
          $log.log("Picked part", pickedPartId);

          // Lookup the picked part
          $scope.iPartPromise = restService.findPart(pickedPartId).then(
              function (pickedPart) {
                $log.log("Loaded picked part", pickedPart);
                if (pickedPart.interchange && pickedPart.interchange.id) {

                  // Update
                  Restangular.setParentless(false);
                  var promise = Restangular.one('interchange', pickedPart.interchange.id).one('part', $scope.partId).put();

                } else {

                  var interchange = {
                    parts: [
                      {
                        id: $scope.partId
                      },
                      {
                        id: pickedPartId
                      }
                    ]
                  };

                  // Create
                  var promise = Restangular.all('interchange').post(interchange);
                }

                promise.then(
                    function() {
                      gToast.open("Interchangeable part added.");
                      $location.path("/part/" + $scope.partType + "/" + $scope.partId);
                    },
                    function(response) {
                      $dialogs.error("Could not add interchangeable part.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                    }
                )
              },
              function(response) {
                $dialogs.error("Could not load part details.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
              });
        }

        $scope.canClear = function () {
            return $scope.part && $scope.part.interchange;
        }

        $scope.clear = function () {
          Restangular.setParentless(false);
          Restangular.one('interchange', $scope.part.interchangeId).one('part', $scope.partId).remove().then(
              function () {
                // Success
                gToast.open("Part removed from interchange.");
                $location.path("/part/" + $scope.partType + "/" + $scope.partId);
              },
              function (response) {
                $dialogs.error("Could not remove part from interchange", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
              });
        }
    });
