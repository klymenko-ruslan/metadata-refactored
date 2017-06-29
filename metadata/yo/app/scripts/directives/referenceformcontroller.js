'use strict';

angular.module('ngMetaCrudApp').directive('referenceFormController', ['$rootScope', function($rootScope) {
  /**
   * The directive was copy-pasted (and modified) from:
   *   * http://davidhavl.com/devnotes/2014/01/angularjs-access-form-controller-from-outside/
   * Thank you David Havl for your perfect work :)
   */
  return {
    restrict: 'A',
    require: '^form', // require FormController
    link: function (scope, element, attrs, ctrls) {
      // broadcast existence of new FormController
      $rootScope.$broadcast('form:created', {name: attrs.name, controller: ctrls});
    }
  };
}]);
