'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartTypeListCtrl', ['$scope', 'NgTableParams', 'partTypes',
    function ($scope, NgTableParams, partTypes) {
      $scope.partTypesTableParams = new NgTableParams({
        'page': 1,
        'count': 10,
        'sorting': {
          'id': 'asc'
        }
      }, {
        'dataset': partTypes
      });
    }
  ]);
