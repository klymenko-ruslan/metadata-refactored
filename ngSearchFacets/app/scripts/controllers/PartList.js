'use strict';

angular.module('ngSearchFacetsApp')
  .controller('PartListCtrl', function ($scope, ngTableParams) {
        $scope.tableParams = new ngTableParams({
            count:20,
            page:1,
            total:100,
            counts: []
        });
  });
