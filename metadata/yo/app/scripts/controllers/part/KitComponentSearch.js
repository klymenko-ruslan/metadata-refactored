'use strict';

angular.module('ngMetaCrudApp')
    .controller('KitComponentSearchCtrl', function ($log, $scope, $location, $routeParams, Kits, restService, Restangular, dialogs, gToast) {
        $scope.partId = $routeParams.id;
        $scope.partType = 'Kit';

        $scope.pickedPart = null;
        $scope.showPickedPart = false;

        // The base kit
        $scope.part = restService.findPart($scope.partId)
            .then(function (part) {
                $scope.part = part;
                $scope.mapping.kit = part;

            }, function (errorResponse) {
              restService.error("Could not get part details", errorResponse);
            });
        
        $scope.components = Kits.listComponents($scope.partId)
                .then(function(components) {
                    $scope.components = components;
                }, restService.error);

        $scope.mapping = {
            kit: null,
            part: null,
            exclude: false
        };

        $scope.save = function () {
          Restangular.setParentless(false);
          Restangular.one('kit', $scope.partId).all('component').post($scope.mapping).then(
            function () {
              // Success
              gToast.open("Common component mapping added.");
              $location.path("/part/" + $scope.partId);
            },
              function (response) {
                restService.error("Could not add kit mapping", response);
            });
        }

        $scope.pick = function (partId) {
            $scope.pickedPart = restService.findPart(partId).then(
                function (pickedPart) {
                    $scope.pickedPart = pickedPart;
                    $scope.mapping.part = pickedPart;
                },
                function (errorResponse) {
                  restService.error("Could not pick part", errorResponse);
                });
        }
    });
