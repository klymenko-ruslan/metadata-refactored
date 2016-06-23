"use strict";

angular.module("ngMetaCrudApp")
  .directive("picker", ["$log", function($log) {
    return {
      scope: {
        "itemType": "@?",
        "items": "=?",
        "ngModel": "@",
        "required": "@"
      },
      restrict: "E",
      templateUrl: "views/component/Picker.html",
      controller: ["$scope", "$parse", "restService", function($scope, $parse,
        restService) {
        var getter = $parse($scope.ngModel);
        var setter = getter.assign;
        // Fetch the items if we got a path
        // $log.log("itemType: " + $scope.itemType);
        var rest = null;
        if ($scope.itemType == "type/part") {
          rest = restService.listPartTypes;
        } else if ($scope.itemType == "other/manufacturer") {
          rest = restService.listManufacturers;
        }
        if (rest !== null) {
          rest().then(function(items) {
            $scope.items = items;
          }, function(response) {
            console.error("Failed to load items from " + $scope.itemType);
          });
        }
        /**
         * Current selection
         * @type {{}}
         */
        $scope.selection = {};
        // Setup the initial selection
        var initialValue = getter($scope.$parent);
        if (angular.isObject(initialValue)) {
          $scope.selection.id = initialValue.id;
        }
        /**
         * Watches for external changes.
         */
        $scope.$watch(
          function() {
            return getter($scope.$parent);
          },
          function(newExternalValue, oldExternalValue) {
            if (newExternalValue === oldExternalValue) return;
            if (angular.isObject(newExternalValue)) {
              $scope.selection.id = newExternalValue.id;
            } else {
              $scope.selection.id = null;
            }
          }
        );
        /**
         * Watches for and propagates selection changes.
         */
        $scope.$watch("selection.id",
          function(newSelectedValue, oldSelectedValue) {
            if (newSelectedValue === oldSelectedValue) return;
            if ($scope.items === null) return;
            if (newSelectedValue === null) {
              setter($scope.$parent, null);
            } else {
              var selectedItem = _.find($scope.items, function(item) {
                return $scope.selection.id == item.id;
              });
              setter($scope.$parent, selectedItem);
            }
          }
        );
      }]
    };
  }]);
