'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartBomSearchCtrl', function ($scope, $location, $routeParams, restService) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

        $scope.pickedPart = null;
        $scope.showPickedPart = false;

        // The part whose bom we're editing
        $scope.part = restService.findPart($scope.partId, {fields: 'bom,bom.parent,bom.child'})
            .then(function(part) {
                $scope.part = part;

                $scope.bomItem.parent = {
                    id: part.id
                };



                console.log("Part: ",$scope.part);
                console.log("BOM: ",$scope.part.bom);
        });

        $scope.bomItem = {
            parent: {id:$scope.partId},
            child: null,
            quantity: 1
        };

        $scope.save = function() {
            console.log("BOM Item: " + JSON.stringify($scope.bomItem));

            // Add the new BOM item to the part and save it
            if (!angular.isArray($scope.part.bom)) {
                $scope.part.bom = [];
            }
            $scope.part.bom.push($scope.bomItem);

            console.log("BOM: ",$scope.part.bom);


            $scope.part.put().then(function() {
                console.log("Saved.");
                $location.path("/part/" + $scope.partType + "/" + $scope.partId + "/form");
            });
        }

        $scope.onPick = function(action, pickedPartId, pickedPartType) {
            $scope.pickedPart = restService.findPart(pickedPartId);

            $scope.bomItem.child = {id: pickedPartId};
        }
    });
