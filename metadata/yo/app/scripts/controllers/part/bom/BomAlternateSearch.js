'use strict';

angular.module('ngMetaCrudApp')
    .controller('BomAlternateSearchCtrl', function ($log, $scope, $location, $routeParams, restService, Restangular, $dialogs, gToast) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

        $scope.pickedPart = null;
        $scope.showPickedPart = false;

        // The part whose bom we're editing
        $scope.part = restService.findPart($scope.partId)
            .then(function (parentPart) {
                $scope.part = parentPart;

                $scope.bomItem = _.find(parentPart.bom, function(bomItem) {
                  return bomItem;
                });

//                $scope.bomItem.parent = {
//                    id: parentPart.id,
//                    version: parentPart.version
//                };

            }, function (errorResponse) {
                alert("Could not get part details", errorResponse);
            });

        $scope.save = function () {

          var altItem = {
            header: $scope.header,
            part: $scope.pickedPart
          };

          Restangular.setParentless(false);
          Restangular.one('bom', $scope.bomItem.id).all('alt').post(altItem).then(
            function () {
              // Success
              gToast.open("BOM alternate added.");
              $location.path("/part/" + $scope.partType + "/" + $scope.partId);
            },
              function (response) {
                $dialogs.error("Could not add BOM alternate", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
            });
        }

        $scope.pickPart = function (partId) {
            $scope.pickedPart = restService.findPart(partId).then(
                function (pickedPart) {
                    $scope.pickedPart = pickedPart;
                },
                function (errorResponse) {
                    alert("Could not pick part.");
                    $log.log("Could not pick part", errorResponse);
                });
        }
    });