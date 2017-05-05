'use strict';

angular.module('ngMetaCrudApp').directive('ngHtmlCompile', function($compile) {
  /**
   * The directive was copy-pasted from:
   *  * https://github.com/francisbouvier/ng_html_compile
   * Thank you Francis Bouvier for your perfect work :)
   */
  return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(attrs.ngHtmlCompile, function(newValue, oldValue) {
          element.html(newValue);
          $compile(element.contents())(scope);
        });
      }
  }
});
