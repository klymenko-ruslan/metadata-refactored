'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartListCtrl', function ($scope, $location, $rootScope) {
        $scope.$on("PartTable.click", function (event, part) {
            console.log(part);
            $location.path("/part/" + part.type + "/" + part.id);
        });
  });
