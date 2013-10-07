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
            $scope.part.turboModel = {
                id: $scope.turboModelId,
                turboType: {
                    id: $scope.turboTypeId,
                    manufacturer: {
                        id: $scope.part.manufacturer.id
                    }
                }
            };
        };

        $scope.$on("revert", function() {
            $scope.turboTypeId = $scope.getPartTurboTypeId();
            $scope.turboModelId = $scope.getPartTurboModelId();

            if (angular.isDefined($scope.part.manufacturer))
                restService.listTurboTypesForManufacturerId($scope.part.manufacturer.id)
                    .then(function(turboTypes) {
                        $scope.turboTypes = turboTypes;
                    });
        });

        $scope.$watch('part.manufacturer.id', function (manufacturerId) {
            if (angular.isUndefined($scope.part)) return;

            // Clear the current turbo model if the manufacturer changes
            if (manufacturerId != $scope.getPartModelManufacturer()) {
                $scope.part.turboModel = null;
            }

            $scope.turboTypeId = $scope.getPartTurboTypeId();
            $scope.turboModelId =$scope.getPartTurboModelId();
            $scope.turboTypes = [];
            $scope.turboModels = [];

            if (angular.isNumber(manufacturerId))
                restService.listTurboTypesForManufacturerId(manufacturerId)
                    .then(function(turboTypes) {
                        $scope.turboTypes = turboTypes;
                    });
        });

        $scope.$watch('turboTypeId', function(newTurboTypeId, oldTurboTypeId) {
            if (!angular.isNumber(newTurboTypeId)) return;

            $scope.turboModels = [];

            if (angular.isNumber(newTurboTypeId))
                restService.listTurboModelsForTurboTypeId(newTurboTypeId)
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
