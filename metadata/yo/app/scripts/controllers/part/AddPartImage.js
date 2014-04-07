'use strict';

angular.module('ngMetaCrudApp')
  .controller('AddPartImageCtrl', function ($log, $scope, $modalInstance, data, gToast, Restangular, restService) {
      var file;

      // Data to be uploaded
      var formData = new FormData();

      $scope.cancel = function() {
        $modalInstance.dismiss('cancelled');
      }

      $scope.changed = function(files) {
        file = files[0];
        formData.append('file', files[0]);
      };

      $scope.upload = function() {
        Restangular.setParentless(false);
        Restangular.one('part', data.part.id).all('image').post(file, {}, {'Content-Type': 'application/octet-stream'}).then(
            function(response) {
              // Success
              data.callback(response);
              gToast.open("Added image.");
              $modalInstance.close('uploaded');
            },
            function(response) {
              // Error
              restService.error("Could not upload image.", response);
            }
        );
      }
  });
