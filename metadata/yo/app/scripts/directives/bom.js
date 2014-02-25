'use strict';

angular.module('ngMetaCrudApp')
  .directive('bom', function ($log) {
    return {
      template: '<div class="well">' +
                '  <div class="row">' +
                '  <h3 style="display: inline-block;">Bill of Materials</h3>' +
                '  <a authorize="ROLE_BOM" class="btn btn-info pull-right" style="vertical-align: middle;" ng-href="#/part/{{part.partType.typeName}}/{{partId}}/bom/search">' +
                '    <i class="fa fa-cogs"></i>' +
                '    Add BOM Item' +
                '  </a>' +
                '  </div>' +
                '  <table data-ng-table="bomTableParams" class="table table-bordered table-striped table-hover table-responsive">' +
                '    <tr data-ng-repeat="bomItem in $data">' +
                '      <td title="\'Type\'">{{bomItem.child.partType.name}}</td>' +
                '      <td title="\'Name\'">{{bomItem.child.name}}</td>' +
                '      <td title="\'Manufacturer\'">{{bomItem.child.manufacturer.name}}</td>' +
                '      <td title="\'Manufacturer Part Number\'">{{bomItem.child.manufacturerPartNumber}}</td>' +
                '      <td title="\'Quantity\'">' +
                '        <span ng-hide="isModifying($index, bomItem)">{{bomItem.quantity}}</span>' +
                '        <input ng-model="modifyValues[bomItem.id]" type="text" ng-show="isModifying($index, bomItem)"/>' +
                '      </td>' +
                '      <td title="\'Actions\'">' +

                '        <button ng-click="modifyStart($index, bomItem)"' +
                '                authorize="ROLE_BOM" class="btn btn-warning btn-xs"' +
                '                ng-hide="isModifying($index, bomItem)">' +
                '          <i class="fa fa-cog"></i> Modify' +
                '        </button>' +

                '        <button ng-click="modifySave($index, bomItem)"' +
                '                authorize="ROLE_BOM" class="btn btn-success btn-xs"' +
                '                ng-show="isModifying($index, bomItem)">' +
                '          <i class="fa fa-save"></i> Save' +
                '        </button>' +

                '        <button ng-click="modifyCancel($index, bomItem)"' +
                '                authorize="ROLE_BOM" class="btn btn-warning btn-xs"' +
                '                ng-show="isModifying($index, bomItem)">' +
                '          <i class="fa fa-minus-circle"></i> Cancel' +
                '        </button>' +

                '        <a ng-href="#/part/{{bomItem.child.partType.typeName}}/{{bomItem.child.id}}"' +
                '           authorize="ROLE_READ" class="btn btn-primary btn-xs"' +
                '           ng-hide="isModifying($index, bomItem)">' +
                '          <i class="fa fa-eye"></i> View Part' +
                '        </a>' +

                '        <button ng-click="remove($index, bomItem)"' +
                '                authorize="ROLE_BOM" class="btn btn-danger btn-xs"' +
                '                ng-hide="isModifying($index, bomItem)">' +
                '          <i class="fa fa-trash-o"></i> Remove' +
                '        </button>' +

                '      </td>' +
                '    </tr>' +
                '  </table>' +

                '</div>',
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

      }
    };
  });
