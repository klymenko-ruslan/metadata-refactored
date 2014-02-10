'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartBomSearchCtrl', function ($log, $scope, $location, $routeParams, restService) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

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
            $log.log("BOM Item: " + JSON.stringify($scope.bomItem));

            // Add the new BOM item to the part and save it
            if (!angular.isArray($scope.part.bom)) {
                $scope.part.bom = [];
            }
            $scope.part.bom.push($scope.bomItem);

            console.log("BOM: ", $scope.part.bom);


            $scope.part.put().then(function () {
                console.log("Saved.");
                $location.path("/part/" + $scope.partType + "/" + $scope.partId + "/form");
            }, function (errorResponse) {
                alert("Could not save part", errorResponse);
            });
        }

        $scope.onPick = function (action, pickedPartId, pickedPartType) {
            $scope.pickedPart = restService.findPart(pickedPartId).then(
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
