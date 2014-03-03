'use strict';

angular.module('ngMetaCrudApp')
    .directive('picker', function ($log, Restangular) {
        return {
            scope: {
                "path": '@?',
                "items": '=?',
                "ngModel": '@',
                "required": '@'
            },
            restrict: 'E',
            template:
                '<div class="picker row">' +
                '  <div class="col-xs-11">' +
                '    <select ng-model="selection.id"' +
                '    ng-options="item.id as item.name for item in items | orderBy: \'name\'"' +
                '    ng-disabled="items == null"' +
                '    placeholder="Choose..." class="form-control input-sm"></select>' +
                '  </div>' +
                '  <div class="col-xs-1">' +
                '    <button ng-click="selection.id=\'0\'" ng-disabled="!selection.id"' +
                '    class="btn btn-link btn-sm">' +
                '      <i class="fa fa-times"></i>' +
                '    </button>' +
                '  </div>' +
                '</div>',
            controller: function ($log, $scope, Restangular, $parse) {

              var getter = $parse($scope.ngModel);
              var setter = getter.assign;

              // Fetch the items if we got a path
              if ($scope.path != null) {
                Restangular.all($scope.path).getList().then(function (items) {
                  $scope.items = items;
                }, function (response) {
                  console.error("Failed to load items from " + $scope.path);
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
//        $log.log($scope.ngModel, "Picker Initial Value", initialValue);
                $scope.selection.id = initialValue.id;
              }

              /**********\
               * Watches
               **********/

              /**
               * Watches for external changes.
               */
              $scope.$watch(
                  function () {
                    return getter($scope.$parent);
                  },
                  function (newExternalValue, oldExternalValue) {
                    if (newExternalValue === oldExternalValue) return;

//            $log.log("Picker getter", $scope.ngModel, newExternalValue, oldExternalValue);

                    if (angular.isObject(newExternalValue)) {
                      $scope.selection.id = newExternalValue.id;
                    } else {
                      $scope.selection.id = null;
                    }
                  });

              /**
               * Watches for and propagates selection changes.
               */
              $scope.$watch('selection.id', function (newSelectedValue, oldSelectedValue) {
                if (newSelectedValue === oldSelectedValue) return;

                if ($scope.items == null) return;

//        $log.log("Picker selection.id", $scope.ngModel,  newSelectedValue, oldSelectedValue);

                if (newSelectedValue == null) {
                  setter($scope.$parent, null);
                } else {
                  var selectedItem = _.find($scope.items, function (item) {
                    return $scope.selection.id == item.id;
                  });

                  setter($scope.$parent, selectedItem);
                }
              });

            },
            link: function (scope, element, attrs) {
//              scope.ngModelAttr = attrs.ngModel;
            }
        }
    });
