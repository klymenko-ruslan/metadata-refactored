'use strict';

angular.module('ngMetaCrudApp')
    .controller('PickerCtrl', function ($scope, Restangular) {
        $scope.$watch("ngModel.id", function() {
            if (!angular.isObject($scope.ngModel)) {
                return;
            }

            // Look for the item by ID
            for (var itemId in $scope.items) {
                var item = $scope.items[itemId];

                // If we've found the item, copy over it's details and stop
                if (angular.isObject(item) &&  item.id == $scope.ngModel.id) {
                    Restangular.copy(item, $scope.ngModel);
                    return;
                }
            }

            console.error("Could not find item #" + $scope.ngModel.id + " in: " + JSON.stringify($scope.items))
        });
    });
