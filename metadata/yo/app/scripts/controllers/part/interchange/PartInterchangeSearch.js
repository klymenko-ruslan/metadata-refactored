'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartInterchangeSearchCtrl', function ($scope, $location, $routeParams, restService) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

        $scope.showPickedPart = false;

        // The part whose interchange we're editing
        $scope.part = restService.findPart($scope.partId).then(function (part) {
            $scope.part = part;
            $scope.newInterchange = part.interchange;
        });

        // The part the user picked
        $scope.pickedPart = null;

        $scope.isChanged = function () {
            return $scope.part.interchange != $scope.newInterchange;
        };

        $scope.undo = function () {
            $scope.newInterchange = $scope.part.interchange;
            $scope.pickedPart = null;
        }

        $scope.save = function () {

            // Copy over the new interchange information
            if ($scope.newInterchange == null) {
                $scope.part.interchange = null;
            } else {
                $scope.part.interchange = {};
                angular.copy($scope.newInterchange, $scope.part.interchange);
            }

            // Save the part
            if (angular.isObject($scope.pickedPart)) {
                $scope.part.interchangePartId = $scope.pickedPart.id;
            }

            $scope.part.put().then(function () {
                $scope.newInterchange = $scope.part.interchange;
                console.log("Saved.");
            });
        }

        $scope.canClear = function () {
            return $scope.newInterchange != null || $scope.pickedPart != null;
        }

        $scope.clear = function () {
            $scope.pickedPart = null;
            $scope.newInterchange = null;
        }

        $scope.onPick = function (action, pickedPartId, pickedPartType) {
            $scope.pickedPart = restService.findPart(pickedPartId);
            $scope.pickedPart.then(function (pickedPart) {
                $scope.pickedPart = pickedPart;
                $scope.newInterchange = {};
                angular.copy(pickedPart.interchange, $scope.newInterchange);
            });
        }
    });
