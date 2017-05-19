'use strict';

angular.module('ngMetaCrudApp')
.directive('cmakeForm', function() {
  return {
    restrict: 'E',
    replace: false,
    templateUrl: '/views/application/carmake/form.html',
    controller: ['restService', '$q', '$scope',
      function(restService, $q, $scope) {
        $scope.carmake = {};
        $scope.$on('carmakeform:save', function(event, callback) {
          var promise = restService.createCarmake($scope.carmake);
          callback(promise);
        });
      }
    ]
  };
})
.directive('uniqueCarmakeName', ['$log', '$q', 'restService', function($log, $q, restService) {
  // Validator for uniqueness of the carmake name.
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.nonUniqueName = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        restService.findCarmakeByName(viewValue).then(
          function(foundCarmake) {
            if (foundCarmake === undefined) {
              def.resolve();
            } else {
              def.reject();
            }
          },
          function (/*errorResponse*/) {
            $log.log('Couldn\'t validate name of the carmake: ' + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
