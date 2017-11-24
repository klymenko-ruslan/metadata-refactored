'use strict';

angular.module('ngMetaCrudApp')
.controller('PartAncestorsCtrl', ['$log', '$routeParams', '$scope',
    'restService', 'NgTableParams', 'part',
  function($log, $routeParams, $scope, restService, NgTableParams, part) {

    $scope.partId = $routeParams.id;
    $scope.part = part;

    $scope.ancestorsTableParams = new NgTableParams({
        page: 1,
        count: 25
      }, {
        getData: function (params) {
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          return restService.loadAncestors($scope.partId, offset, limit).then(
            function success(page) {
              params.total(page.total);
              return page.recs;
            },
            function failure() {
              restService.error('Couldn\'t load ancestors.', response);
            }
          );
        }
      }
    );

  }]);
