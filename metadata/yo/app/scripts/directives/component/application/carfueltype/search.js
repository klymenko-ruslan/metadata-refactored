'use strict';

angular.module('ngMetaCrudApp').directive('carfueltypeSearch', ['$log', 'restService', 'toastr', function ($log, restService, toastr) {
  return {
    'restrict': 'E',
    'replace': true,
    'templateUrl': '/views/component/application/carfueltype/search.html',
    'transclude': true,
    'link': function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
      controller.transcludeActionsFn = transcludeFn;
    },
    'controller': ['$log', '$q', '$scope', 'dialogs', 'NgTableParams', function ($log, $q, $scope, dialogs, NgTableParams) {
      // Latest Results
      $scope.searchResults = null;

      // Temp storage for quantities
      $scope.modifyValues = {};

      $scope.isModifying = function(carfueltype) {
        return carfueltype.id in $scope.modifyValues;
      };

      $scope.modifyStart = function(carfueltype, form) {
        $scope._resetForm(form);
        $scope.modifyValues = {}; // close other edited form
        $scope.modifyValues[carfueltype.id] = carfueltype.name;
      };

      $scope._resetForm = function(form) {
        form.$rollbackViewValue();
        form.$setPristine();
      };

      $scope.modifyCancel = function(carfueltype, form) {
        delete $scope.modifyValues[carfueltype.id];
        $scope._resetForm(form);
      };

      $scope.modifySave = function(carfueltype, form) {
        var name = $scope.modifyValues[carfueltype.id];
        carfueltype.name = name;
        restService.updateCarfueltype(carfueltype).then(
          function() {
            // Success.
            delete $scope.modifyValues[carfueltype.id];
            $scope._resetForm(form);
            toastr.success('The car fuel type "' + name + '" has been successfully updated.');
          },
          function errorResponse(response) {
            restService.error('Car fuel type (id:' + carfueltype.id + ') "' + name + '" update failed.', response);
          }
        );
      };

      $scope.remove = function(id, name) {
        dialogs.confirm('Delete car fuel type "' + name + '".', 'Are you sure?').result.then(
          function() {
            // Yes
            restService.removeCarfueltype(id).then(
              function () {
                $scope.clear(); // reload table
                $scope.carfueltypeTableParams.reload();
                toastr.success('Car fuel type "' + name + '" has been successfully removed.');
              },
              function errorResponse(response) {
                restService.error('Car fuel type "' + name + '" remove failed.', response);
              }
            );
          }
        );
      };

      // CarFuelType Table
      $scope.carfueltypeTableParams = new NgTableParams(
        {
          'page': 1,
          'count': 10,
          'sorting': {}
        },
        {
          'getData': function (params) {
            // Update the pagination info
            $scope.search.count = params.count();
            $scope.search.page = params.page();
            $scope.search.sorting = params.sorting();
            var offset = params.count() * (params.page() - 1);
            var limit = params.count();
            for (var sortProperty in $scope.search.sorting) {
                break;
            }
            var sortOrder;
            if (sortProperty) {
              sortOrder = $scope.search.sorting[sortProperty];
            }
            return restService.filterCarFuelTypes($scope.search.carfueltype, sortProperty, sortOrder, offset, limit).then(
              function (filtered) {
                $scope.searchResults = filtered;
                // Update the total and slice the result
                params.total($scope.searchResults.hits.total);
                return $scope.searchResults.hits.hits;
              },
              function (/*errorResponse*/) {
                $log.log('Couldn\'t search for "carfueltype".');
              }
            );
          }
        }
      );
      // Query Parameters
      $scope.search = {
        'carfueltype': '',
        'aggregations': {},
        'sort': {}
      };
      $scope.clear = function() {
        $scope.search = {
          'carfueltype': '',
          'aggregations': {},
          'sort': {}
        };
      };
      // Handle updating search results
      $scope.$watch(
        '[search.carfueltype, search.aggregations]',
        function (newVal, oldVal) {
          // Debounce
          if (angular.equals(newVal, oldVal, true)) {
            return;
          }
          $scope.carfueltypeTableParams.reload();
        },
        true
      );
    }]
  };
}]
).directive('carfueltypeSearchActions', function() {
  return {
    'restrict': 'A',
    'require': '^carfueltypeSearch',
    'link': function postLink(scope, element, attrs, controller) {
      controller.transcludeActionsFn(scope, function(clone) {
        element.append(clone);
      });
    }
  };
});
