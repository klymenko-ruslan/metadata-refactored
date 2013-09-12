'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartListCtrl', function ($scope, $location) {
        $scope.$on("PartTable.click", function (part) {
            $location.path()
        });
  });
