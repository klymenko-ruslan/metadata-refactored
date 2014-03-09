'use strict';

angular.module('ngMetaCrudApp')
    .directive('searchFacets', function ($log, Facets) {
        return {
            scope: {
              search: '=',
              results: '='
            },
            restrict: 'E',
            controller:  function ($scope) {
              $scope.Facets = Facets;

              $scope.isSelected = function (facet) {
                return angular.isDefined($scope.search.facets[facet.name]);
              };

              $scope.select = function(facet, term) {
                $scope.search.facets[facet.name] = term;
              };
            },
            template:
                '<div>' +
                '  <div ng-repeat="facet in Facets" ng-show="results.facets[facet.name].terms">' +
                '    <h2>{{facet.name}}</h2>' +
                '    <select ng-model="search.facets[facet.name]" ng-options="t.term as t.term + \' (\' + t.count + \')\' for t in results.facets[facet.name].terms | orderBy: \'term\'">' +
                '      <option value=""></option>' +
                '    </select>' +
                '  </div>' +
                '</div>'
        };
    });
