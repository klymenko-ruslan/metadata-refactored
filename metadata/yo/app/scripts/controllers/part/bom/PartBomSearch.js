'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartBomSearchCtrl', function ($log, $scope, $location, $routeParams, restService, Restangular, $dialogs, gToast) {
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
                alert("Could not get part details", errorResponse);
            });

        $scope.bomItem = {
            parent: {id: $scope.partId},
            child: null,
            quantity: 1
        };

        $scope.save = function () {
          Restangular.setParentless(true);
          Restangular.all('bom').post($scope.bomItem).then(
            function () {
              // Success
              gToast.open("BOM item added.");
              $location.path("/part/" + $scope.partId);
            },
              function (response) {
                $dialogs.error("Could not add BOM Item", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
            });
        }

        $scope.pickBomItemPart = function (bomItemPartId) {
            $scope.pickedPart = restService.findPart(bomItemPartId).then(
                function (pickedPart) {
                    $scope.pickedPart = pickedPart;
                    $scope.bomItem.child = {
                        id: $scope.pickedPart.id,
                        version: $scope.pickedPart.version
                    };
                },
                function (errorResponse) {
                    alert("Could not pick part.");
                    $log.log("Could not pick part", errorResponse);
                });
        }
    });
