'use strict';

angular.module('ngMetaCrudApp')
    .controller('TurboModelPickerCtrl', function ($log, $scope, restService, ngTableParams, Restangular) {

        // List of turbo types and models (set in $watch)
        $scope.turboTypes = null;
        $scope.turboModels = null;

        /**
         * Keep the turbo types updated with the part's manufacturer
         */
        var turboTypesPromise = null;
        $scope.$watch('part.manufacturer.id', function (newMfrId, oldMfrId) {
//        $log.log("TurboModelPicker.$watch part.manufacturer.id", newMfrId, oldMfrId);

            if (!angular.isObject($scope.part)) return;    // NOP if the part is null
            if ($scope.manufacturerId == newMfrId) return; // NOP if we're already displaying this manufacturer's turbo types

            // Update the manufacturer ID for the turbo types list
            $scope.manufacturerId = newMfrId;

            // TODO: Cancel any previous fetch we were doing

            $log.log("Fetching new turbo types for manufacturer", $scope.manufacturerId);

            // Fetch the new types
            $scope.turboTypes = null;
            $scope.turboModels = null;

            // Use the turbo type and model from the part if the manufacturer is the same
            var partTurboTypeMfrId = $scope.$eval('part.turboModel.turboType.manufacturer.id');
            if ($scope.manufacturerId == partTurboTypeMfrId) {
                $scope.turboTypeId = $scope.$eval('part.turboModel.turboType.id');
                $scope.turboModelId = $scope.$eval('part.turboModel.id');
//          $log.log("TurboModelPicker using part values type/model", $scope.turboTypeId, $scope.turboModelId);
            } else {
                $scope.turboTypeId = null;
                $scope.turboModelId = null;
            }
//        $log.log("TurboModelPicker.turboTypeId", $scope.turboTypeId);

            // Clear and fetch the turbo types
            $scope.turboTypes = null;

            Restangular.setParentless(false);
            turboTypesPromise = Restangular.all("other/turboType").one('manufacturer', $scope.manufacturerId)
                .getList()
                .then(function (response) {

                    $scope.turboTypes = response;
                }, function (errorResponse) {

                    alert("Could not fetch turbo types");
                });
        });

        // Fetch the new turbo models when the turbo type changes
        var turboModelsPromise = null;
        $scope.$watch('turboTypeId', function (newTurboTypeId, oldTurboTypeId) {
//        $log.log("TurboModelPicker.$watch turboTypeId", newTurboTypeId, oldTurboTypeId);

            // Clear and fetch the turbo models
            $scope.turboModels = null;

            // Fetch the turbo models if we have a type
            if ($scope.turboTypeId != null) {
                turboModelsPromise = Restangular.all("other/turboModel")
                    .getList({"turboTypeId": $scope.turboTypeId})
                    .then(function (response) {

                        $scope.turboModels = response;
                    }, function (errorResponse) {

                        alert("Could not fetch turbo models");
                    });
            }
        });

        // Watch for changes in the part's model/type (i.e. reverting) and propagate them to the picker
        $scope.$watch('{modelId:part.turboModel.id, typeId: part.turboModel.turboType.id}', function (newValue, oldValue) {
//        $log.log("TurboModelPicker.$watch (part.turboModel.id, part.turboModel.turboType.id}", newValue);
            $scope.turboModelId = newValue.modelId;
            $scope.turboTypeId = newValue.typeId;
        }, true);


        // Reset the turbo TYPE filter when the list changes
        $scope.$watch('turboTypes', function () {
            $scope.turboTypeFilter = "";
        });

        // Reset the turbo MODEL filter when the list changes
        $scope.$watch('turboModels', function () {
            $scope.turboModelFilter = "";
        });


        $scope.setPartTurboModel = function () {
            var turboModel = _.find($scope.turboModels, function (turboModel) {
                return $scope.turboModelId == turboModel.id;
            });

            $scope.part.turboModel = Restangular.copy(turboModel)
//        $log.log('setPartTurboModel', $scope.part.turboModel);
        };
    });
