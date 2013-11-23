'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartListCtrl', function ($scope, $location, $log, $rootScope, $modal) {
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

      $scope.createPart = function() {
        var modalInstance = $modal.open({
          templateUrl: '/views/part/PartCreateModal.html',
          controller: 'PartCreateModalCtrl'
        });
      };
  });
