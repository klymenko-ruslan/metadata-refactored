'use strict';

angular.module('ngMetaCrudApp')
  .directive('bom', function ($log, Restangular) {
    return {
      scope: {
          parentPartId: "="
      },
      templateUrl: '/views/component/bom.html',
      restrict: 'E',
      link: function postLink(/*$scope, element, attrs */) {
      },
      controller: function(dialogs, $scope, BOM, ngTableParams, gToast, Restangular, restService) {
        $scope.restService = restService;

        $scope.$watch("parentPartId", function(parentPartId) {
            if (parentPartId === undefined) {
                return;
            }

            $log.log("Loading BOM for parent part", parentPartId);
            BOM.listByParentPartId(parentPartId).then(function(bom) {
                $scope.bom = bom;
                $scope.bomTableParams.reload();
            }, restService.error);
        });

        $scope.bomTableParams = new ngTableParams({
          page: 1,
          count: 10
        }, {
          getData: function ($defer, params) {
              
                if (!angular.isObject($scope.bom)) {
                  $defer.reject();
                  return;
                }

                $scope.bom = _.sortBy($scope.bom, 'id');

                // Update the total and slice the result
                $defer.resolve($scope.bom.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                params.total($scope.bom.length);
              }
        });

        // Temp storage for quantities
        $scope.modifyValues = {};

        $scope.isModifying = function(index, bomItem) {
          return angular.isDefined($scope.modifyValues[bomItem.id]);
        };

        $scope.modifyStart = function(index, bomItem) {
          $scope.modifyValues[bomItem.id] = bomItem.quantity;
        };

        $scope.modifyCancel = function(index, bomItem) {
          delete $scope.modifyValues[bomItem.id];
        };

        $scope.modifySave = function(index, bomItem) {
          var quantity = $scope.modifyValues[bomItem.id];
          Restangular.one('bom').post(bomItem.id, null, {quantity: quantity}).then(
              function() {
                bomItem.quantity = quantity;
                delete $scope.modifyValues[bomItem.id];
              },
              function() {
          }
          );
        };

        $scope.remove = function(index, bomItem) {
          $log.log('Remove bom item, part: ', $scope.parentPart);

          dialogs.confirm(
                  'Remove BOM Item?',
                  'Remove child part from this bill of materials?').result.then(
              function() {
                // Yes
                Restangular.one('bom', bomItem.id).remove().then(
                    function() {
                      // Success

                      // Remove the BOM item from the local part and reload the table
                      $scope.bom.splice(index, 1);
                      $scope.bomTableParams.reload();

                      // Clear the alt bom item
                      $scope.altBomItem = null;

                      gToast.open("Child part removed from BOM.");
                    },
                    restService.error);
              });
        };

        $scope.removeAlternate = function(index, altItem) {
          dialogs.confirm(
              "Remove alternate item?",
              "This will remove the alternate part from this BOM item.").result.then(
                  function() {
                    Restangular.setParentless(false);
                    Restangular.one('bom', $scope.altBomItem.id).one('alt', altItem.id).remove().then(
                        function() {
                          $scope.altBomItem.alternatives.splice(index, 1);
                          gToast.open("BOM alternate removed.");
                        },
                        restService.error
                    );
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
