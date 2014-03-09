'use strict';

angular.module('ngMetaCrudApp')
  .directive('bom', function ($log) {
    return {
      templateUrl: '/views/component/bom.html',
      restrict: 'E',
      link: function postLink(scope, element, attrs) {
//        element.text('this is the bom directive');
      },
      controller: function($scope, ngTableParams, gToast, Restangular, $dialogs) {


        $scope.bomTableParams = new ngTableParams(
            {
              page: 1,
              count: 10
            },
            {
              getData: function ($defer, params) {
                if (!angular.isObject($scope.part)) {
                  $defer.reject();
                  return;
                };

                // Update the total and slice the result
                $defer.resolve($scope.part.bom.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                params.total($scope.part.bom.length);
              }
            });

        // Temp storage for quantities
        $scope.modifyValues = {};

        $scope.isModifying = function(index, bomItem) {
          return angular.isDefined($scope.modifyValues[bomItem.id]);
        }

        $scope.modifyStart = function(index, bomItem) {
          $scope.modifyValues[bomItem.id] = bomItem.quantity;
        };

        $scope.modifyCancel = function(index, bomItem) {
          delete $scope.modifyValues[bomItem.id];
        };

        $scope.modifySave = function(index, bomItem) {
          var quantity = $scope.modifyValues[bomItem.id];
          Restangular.restangularizeElement($scope.part, bomItem, 'bom').put({quantity: quantity}).then(
              function() {
                bomItem.quantity = quantity;
                delete $scope.modifyValues[bomItem.id];
              },
              function() {
              }
          );
        };

        $scope.remove = function(index, bomItem) {
          $log.log("Remove bom item, part: ", $scope.part);

          $dialogs.confirm(
                  "Remove BOM Item?",
                  "Remove child part from this bill of materials?").result.then(
              function() {
                // Yes
                Restangular.one('bom', bomItem.id).remove().then(
                    function() {
                      // Success

                      // Remove the BOM item from the local part and reload the table
                      $scope.part.bom.splice(index, 1);
                      $scope.bomTableParams.reload();

                      gToast.open("Child part removed from BOM.");
                    },
                    function(response) {
                      // Error
                      $dialogs.error(
                          "Could not remove BOM Item",
                          "Here's the error: <pre>" + response.status +"</pre>");
                    });
              },
              function() {
                // No
              });
        };


        // The BOM item whose alternates we're showing
        $scope.altBomItem = null;

        $scope.showAlternates = function(bomItem) {
          $scope.altBomItem = bomItem;
        };

        $scope.hideAlternates = function() {
          $scope.altBomItem = null;
        }

      }
    };
  });
