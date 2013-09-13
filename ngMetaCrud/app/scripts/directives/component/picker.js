'use strict';

angular.module('ngMetaCrudApp')
  .directive('picker', function () {
    return {
      templateUrl: '/views/component/Picker.html',
      restrict: 'E',
      controller: 'PickerCtrl'
    };
  });
