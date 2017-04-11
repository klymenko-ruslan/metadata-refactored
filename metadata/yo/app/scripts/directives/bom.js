"use strict";

angular.module("ngMetaCrudApp")
  .directive("bom", ["$log", "$q", "Restangular", function($log, $q, Restangular) {
    return {
      scope: {
        parentPartId: "="
      },
      templateUrl: "/views/component/bom.html",
      restrict: "E",
      link: function postLink( /*$scope, element, attrs */ ) {},
      controller: ["dialogs", "$scope", "$parse", "BOM", "ngTableParams", "toastr",
        "Restangular", "utils", "restService",
        function(dialogs, $scope, $parse, BOM, ngTableParams, toastr,
          Restangular, utils, restService) {
          $scope.restService = restService;

          $scope.bomTableParams = new ngTableParams({
            page: 1,
            count: 10
          }, {
            data: []
          });

          $scope.$watch("parentPartId", function(parentPartId) {
            if (parentPartId === undefined) {
              return;
            }
            $q.all([restService.getInterchangesOfThePartBoms(parentPartId), BOM.listByParentPartId(parentPartId)]).then(
              function success(retVals) {
                $scope.interchangesOfThePartBoms = retVals[0];
                $scope.bom = retVals[1];
                $scope.bomTableParams = new ngTableParams({
                  page: 1,
                  count: 10
                }, {
                  getData: utils.localPagination($scope.bom, "child.manufacturerPartNumber")
                });
              },
              function failure(response) {
                restService.error("Loading of BOMs failed.", response);
              }
            );
          });


          // Temp storage for quantities
          $scope.modifyValues = {};

          $scope.isModifying = function(bomItem) {
            return angular.isDefined($scope.modifyValues[bomItem.id]);
          };

          $scope.modifyStart = function(bomItem) {
            $scope.modifyValues[bomItem.id] = bomItem.quantity;
          };

          $scope.modifyCancel = function(bomItem) {
            delete $scope.modifyValues[bomItem.id];
          };

          $scope.modifySave = function(bomItem) {
            var quantity = $scope.modifyValues[bomItem.id];
            Restangular.one("bom").post(bomItem.id, null, {
              quantity: quantity
            }).then(
              function() {
                bomItem.quantity = quantity;
                delete $scope.modifyValues[bomItem.id];
              },
              function() {}
            );
          };

          $scope.remove = function(bomItem) {
            dialogs.confirm(
              "Remove BOM Item?",
              "Remove child part from this bill of materials?").result.then(
              function() {
                // Yes
                BOM.removeBOM(bomItem.id).then(
                  function success() {
                    // Success
                    // Remove the BOM item from the local part and reload the table
                    var idxToRemove = _.findIndex($scope.bom, function(e) {
                      return e.id === bomItem.id;
                    });
                    $scope.bom.splice(idxToRemove, 1);
                    $scope.bomTableParams.reload();
                    // Clear the alt bom item
                    $scope.altBomItem = null;
                    toastr.success("Child part removed from BOM.");
                  },
                  function failure(response) {
                    restService.error("Removing of the BOM failed.", response);
                  }
                );
              }
            );
          };

          $scope.removeAlternate = function(index, altItem) {
            dialogs.confirm(
              "Remove alternate item?",
              "This will remove the alternate part from this BOM item.").result.then(
              function() {
                Restangular.setParentless(false);
                Restangular.one("bom", $scope.altBomItem.id).one("alt", altItem.id).remove().then(
                  function success() {
                    $scope.altBomItem.alternatives.splice(index, 1);
                    toastr.success("BOM alternate removed.");
                  },
                  function failure(response) {
                    restService.error("Removing of the Alternate failed.", response);
                  }
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
          };

        }
      ]
    };
  }]);
