'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartAncestorsCtrl', ['$log', '$routeParams', '$scope', 'restService', 'NgTableParams',
    'part', 'ancestors',
  function($log, $routeParams, $scope, restService, NgTableParams, part, ancestors) {

    $scope.partId = $routeParams.id;
    $scope.part = part;

    $scope.ancestorsTableParams = new NgTableParams({
        page: 1,
        count: 25,
        sorting: {'relationDistance': 'asc'}
      }, {
        dataset: ancestors,
      }
    );

  }]);
