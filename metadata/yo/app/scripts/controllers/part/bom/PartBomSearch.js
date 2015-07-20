'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartBomSearchCtrl', function ($log, $scope, $location, $routeParams, restService, Restangular, dialogs, gToast) {
        $scope.restService = restService;
        $scope.partId = $routeParams.id;

        $scope.pickedPart = null;
        $scope.showPickedPart = false;

        // The part whose bom we're editing
        $scope.part = restService.findPart($scope.partId)
            .then(function (parentPart) {
                $scope.part = parentPart;

                $scope.bomItem.parent = {
                    id: parentPart.id,
                    version: parentPart.version
                };

                $log.log("Editing part: ", $scope.part);
                $log.log("BOM: ", $scope.part.bom);
            }, function (errorResponse) {
                restService.error("Could not get part details", errorResponse);
            });

        $scope.bomItem = {
            parentPartId: $scope.partId,
            childPartId: null,
            quantity: 1
        };

        $scope.save = function () {
          Restangular.all('bom').post($scope.bomItem).then(
            function () {
              // Success
              gToast.open("BOM item added.");
              $location.path("/part/" + $scope.partId);
            },
              function (response) {
                dialogs.error("Could not add BOM Item", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
            });
        }

        $scope.pickBomItemPart = function (bomItemPartId) {
            $scope.pickedPart = restService.findPart(bomItemPartId).then(
                function (pickedPart) {
                    $scope.pickedPart = pickedPart;
                    $scope.bomItem.childPartId = $scope.pickedPart.id;
                },
                function (errorResponse) {
                    restService.error("Could not pick part.", errorResponse);
                    $log.log("Could not pick part", errorResponse);
                });
        }
    });
