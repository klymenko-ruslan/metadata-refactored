'use strict';

angular.module('ngMetaCrudApp')
    .controller('PickerCtrl', function ($log, $scope, Restangular, $parse) {

      var getter = $parse($scope.ngModel);
      var setter = getter.assign;

      // Fetch the items if we got a path
      if ($scope.path != null) {
        Restangular.all($scope.path).getList().then(function (items) {
          $scope.items = items;
        }, function(response) {
          console.error("Failed to load items from " + scope.path);
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
        $log.log($scope.ngModel, "Picker Initial Value", initialValue);
        $scope.selection.id = initialValue.id;
      }

      /**********\
       * Watches
       **********/

      /**
       * Watches for external changes.
       */
      $scope.$watch(
          function() {
            return getter($scope.$parent);
          },
          function(newExternalValue, oldExternalValue) {
            if (newExternalValue === oldExternalValue) return;

            $log.log("getter", $scope.ngModel, newExternalValue, oldExternalValue);

            if (angular.isObject(newExternalValue)) {
              $scope.selection.id = newExternalValue.id;
            } else {
              $scope.selection.id = null;
            }
          });

      /**
       * Watches for and propagates selection changes.
       */
      $scope.$watch('selection.id', function(newSelectedValue, oldSelectedValue) {
        if (newSelectedValue === oldSelectedValue) return;

        $log.log("selection.id", $scope.ngModel,  newSelectedValue, oldSelectedValue);

        if (newSelectedValue == null) {
          setter($scope.$parent, null);
        } else {
          var selectedItem = _.find($scope.items, function(item) {
            return $scope.selection.id == item.id;
          });

          setter($scope.$parent, selectedItem);
        }
      });


      // Set the value
      $scope.onSelect = function() {
      }
    });
