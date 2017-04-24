"use strict";

angular.module("ngMetaCrudApp").controller("AddPartImageCtrl", [
  "$log", "$scope", "$uibModalInstance", "data", "toastr", "restService",
  function ($log, $scope, $uibModalInstance, data, toastr, restService) {

    var file;

    // Data to be uploaded
    var formData = new FormData();

    $scope.publishImage = true;

    $scope.cancel = function() {
      $uibModalInstance.dismiss("cancelled");
    }

    $scope.changed = function(files) {
      file = files[0];
      formData.append("file", files[0]);
    };

    $scope.upload = function() {
      restService.addProductImage(data.part.id, $scope.publishImage).then(
        function success(response) {
          toastr.success("Added image.");
          $uibModalInstance.close(response);
        },
        function failure(response) {
          restService.error("Could not upload image.", response);
        }
      );
    }
  }
]);
