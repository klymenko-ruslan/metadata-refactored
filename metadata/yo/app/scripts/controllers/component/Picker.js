'use strict';

angular.module('ngMetaCrudApp')
    .controller('PickerCtrl', function ($log, $scope, Restangular, $parse) {

      // Fetch the items if we got a path
      if ($scope.path != null) {
        Restangular.all($scope.path).getList().then(function (items) {
          $scope.items = items;
        }, function(response) {
          console.error("Failed to load items from " + scope.path);
        });
      }

      $scope.selection = {};


      // Set the value
      $scope.onSelect = function() {
        if (angular.isDefined($scope.selection.id)) {
          var selectedItem = _.find($scope.items, function(item) {
            return $scope.selection.id == item.id;
          });

          $parse($scope.ngModel).assign($scope.$parent, selectedItem);
        }
      }
    });
