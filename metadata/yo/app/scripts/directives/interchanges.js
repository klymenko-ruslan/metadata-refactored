'use strict';

angular.module('ngMetaCrudApp')
.directive('interchanges', [function() {
  return {
    transclude: false,
    template: '<ul class="list-inline" data-ng-if="!interchange.alone">' +
              '  <li data-ng-repeat="i in interchange.parts">' +
              '    <a href="/part/{{i.partId}}">{{i.partNumber}}</a><span data-ng-if="!$last">, </span>' +
              '</li>' +
              '</ul>',
    restrict: 'E',
    scope: {
      'interchange': '='
    }
  };
}]);
