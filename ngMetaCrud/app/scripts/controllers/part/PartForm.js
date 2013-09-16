'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartFormCtrl', function ($scope, restService, $routeParams) {
        $scope.partId   = $routeParams.id;
        $scope.partType = $routeParams.type;
        $scope.part     = null;
        $scope.oldPart  = null;

        // Setup manufacturer picker
        $scope.manufacturer = {};
        $scope.manufacturers = restService.listManufacturers().then(function(manufacturers) {
            console.log("Loaded " + manufacturers.length + " manufacturers.");
            $scope.manufacturers = manufacturers;
        }, function(response) {
            console.error("Failed to load manufacturer(s).");
        });

        // Lookup the part or setup the create workflow
        if (angular.isDefined($scope.partId)) {
            console.log("Editing part #" + $scope.partId);

            restService.findPart($scope.partId)
                .then(function(part) {
                    console.log("Part data loaded.");

                    // Save the part
                    $scope.part = part;
//                    $scope.manufacturer.id = part.manufacturer.id;

                    // Save a copy for reverting
                    $scope.oldPart = {};
                    angular.copy($scope.part, $scope.oldPart);

                    // Make sure we're using the correct part type
                    $scope.partType = part.partType.typeName;
                }, function(response) {
                    console.error("Could not get manufacturer list from the server.");
                    // TODO: Display error
                });
        } else {
            console.log("Creating new part");
            $scope.part = {};
        }

        $scope.revert = function() {
            angular.copy($scope.oldPart, $scope.part);
            $scope.manufacturer.id = $scope.part.manufacturer.id;
        }

        $scope.save = function() {

        }

        $scope.disable = function() {

        }

        $scope.enable = function() {

        }

//        $scope.$watch("part.manufacturer.id", function() {
//            if (!angular.isObject($scope.part)
//                || !angular.isObject($scope.part.manufacturer)
//                || !angular.isNumber($scope.part.manufacturer.id)) {
//                return;
//            }
//
//            // Look for the manufacturer by ID
//            for (var manufacturerId in $scope.manufacturers) {
//                var manufacturer = $scope.manufacturers[manufacturerId];
//
//                // Set the manufacturer and stop if we've found it
//                if (angular.isObject(manufacturer) &&  $scope.part.manufacturer.id == manufacturer.id) {
//                    console.log("Manufacturer set to " + $scope.part.manufacturer.id + ": " + JSON.stringify(manufacturer));
//                    $scope.part.manufacturer = manufacturer;
//                    return;
//                }
//            }
//
//            console.error("Could not find manufacturer #" + $scope.part.manufacturer.id + " in: " + JSON.stringify($scope.manufacturers))
//        });

  });
