'use strict';

angular.module('ngMetaCrudApp').controller('CarFuelTypeFormCtrl', ['restService', '$q', '$scope', '$location', '$log',
  '$routeParams', 'toastr', function(restService, $q, $scope, $location, $log, $routeParams, toastr) {

    $scope.carfueltype = {};

    $scope.save = function() {
      restService.createCarfueltype($scope.carfueltype).then(
        function(carfueltypeId) {
          $log.log('Carfueltype has been successfully created: ' + carfueltypeId);
          toastr.success('Carfueltype "' + $scope.carfueltype.name + '" has been successfully created.');
          $location.path('/application/carfueltype/list');
        },
        function (errorResponse) {
          restService.error('Could not create carfueltype.', response);
        }
      );
    };

  }
]).directive('uniqueCarfueltypeName', ['$log', '$q', 'restService', function($log, $q, restService) {
  // Validator for uniqueness of the carfueltype name.
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.nonUniqueName = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        restService.findCarfueltypeByName(viewValue).then(
          function(foundCarfueltype) {
            if (foundCarfueltype === undefined) {
              def.resolve();
            } else {
              def.reject();
            }
          },
          function (errorResponse) {
            $log.log('Couldn\'t validate name of the carfueltype: ' + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
