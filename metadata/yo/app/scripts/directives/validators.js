'use strict';

angular.module('ngMetaCrudApp')

.directive('asciiOnly', ['ASCII_ONLY_REGEX', function(ASCII_ONLY_REGEX) {
  // Validator for uniqueness of the part number.
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$validators.asciiOnly = function(modelValue, viewValue) {
        return ctrl.$isEmpty(modelValue) || ASCII_ONLY_REGEX.test(viewValue);
      };
    }
  };
}]);
