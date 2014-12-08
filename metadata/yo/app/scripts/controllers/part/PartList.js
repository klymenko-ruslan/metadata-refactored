'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartListCtrl', function ($scope, $log, $modal, $dialogs, gToast, Restangular) {

      $scope.createPart = function () {
          var modalInstance = $modal.open({
          templateUrl: '/views/part/PartCreateModal.html',
          controller: 'PartCreateModalCtrl'
        });
      };

    });
