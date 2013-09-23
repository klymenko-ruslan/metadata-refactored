'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartListCtrl', function ($scope, $location, $rootScope) {
        $scope.onAction = function(action, partId, partType) {
            switch (action) {
                case "Details":
                    $location.path('/part/' + partType  + '/' +  partId);
                    return;
                case "Edit":
                    $location.path('/part/' + partType  + '/' +  partId + '/form');
                    return;
            }
        };
  });
