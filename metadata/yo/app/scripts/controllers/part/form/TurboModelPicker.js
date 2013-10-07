'use strict';

angular.module('ngMetaCrudApp')
  .controller('TurboModelPickerCtrl', function ($scope, restService, ngTableParams) {

        // Turbo type info
        $scope.turboTypeId = null;

        $scope.turboModelId = null;

        // List of turbo types
        $scope.turboTypes = [];

        // List of turbo models
        $scope.turboModels = [];


        $scope.getPartModelManufacturer = function(manufacturerId) {
            if (angular.isObject($scope.part)
                && angular.isObject($scope.part.turboModel)
                && angular.isObject($scope.part.turboModel.turboType)
                && angular.isObject($scope.part.turboModel.turboType.manufacturer)) {

                return $scope.part.turboModel.turboType.manufacturer.id;
            }

            return null;
        };

        $scope.getPartTurboTypeId = function() {
            if (angular.isObject($scope.part)
                && angular.isObject($scope.part.turboModel)
                && angular.isObject($scope.part.turboModel.turboType)) {
                return $scope.part.turboModel.turboType.id;
            }

            return null;
        };

        $scope.getPartTurboModelId = function() {
            if (angular.isObject($scope.part)
                && angular.isObject($scope.part.turboModel)) {
                return $scope.part.turboModel.id;
            }

            return null;
        };

        $scope.setPartTurboModel = function() {
            if ($scope.turboModelId == null) {
                $scope.part.turboModel = null;
            } else {
                $scope.part.turboModel = {
                    id: $scope.turboModelId,
                    turboType: {
                        id: $scope.turboTypeId,
                        manufacturer: {
                            id: $scope.part.manufacturer.id
                        }
                    }
                };
            }
        };


        $scope.$watch('part.manufacturer.id', function (manufacturerId) {
            if (!angular.isNumber(manufacturerId)) return;

            // Clear the current turbo model if the manufacturer changes
            if (manufacturerId != $scope.getPartModelManufacturer()) {
                $scope.part.turboModel = null;
            }

            // Turbo Types
            $scope.turboTypeId = $scope.getPartTurboTypeId();
            console.log("turboTypeId", $scope.turboTypeId);

            $scope.turboTypes = [];

            restService.listTurboTypesForManufacturerId(manufacturerId)
                .then(function(turboTypes) {
                    $scope.turboTypes = turboTypes;
                });

            // Turbo Models
            $scope.turboModelId = $scope.getPartTurboModelId();
            console.log("turboModelId", $scope.turboModelId);

            $scope.turboModels = [];

            if (angular.isNumber($scope.turboModelId))
                restService.listTurboModelsForTurboTypeId($scope.turboModelId)
                    .then(function(turboModels) {
                        $scope.turboModels = turboModels;
                    });
        });

        $scope.$watch('turboTypeId', function(turboTypeId) {
            if (!angular.isNumber(turboTypeId)) return;

            $scope.turboModels = [];

            if (angular.isNumber(turboTypeId))
                restService.listTurboModelsForTurboTypeId(turboTypeId)
                    .then(function(turboModels) {
                        $scope.turboModels = turboModels;
                    });
        });

        $scope.$watch('turboModels', function() {
            $scope.turboModelFilter = "";
        });

        $scope.$watch('turboTypes', function() {
            $scope.turboTypeFilter = "";
        });
  });
