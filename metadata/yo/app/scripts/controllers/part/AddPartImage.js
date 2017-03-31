"use strict";

angular.module("ngMetaCrudApp").controller("AddPartImageCtrl", [
  "$log", "$scope", "$uibModalInstance", "data", "gToast", "Restangular", "restService",
  function ($log, $scope, $uibModalInstance, data, gToast, Restangular, restService) {

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
      Restangular.setParentless(false);
      Restangular.one("part", data.part.id).all("image")
        .post(file, { "publish": $scope.publishImage }, {"Content-Type": "application/octet-stream"}).then(
          function(response) {
            // Success
            gToast.open("Added image.");
            $uibModalInstance.close(response);
          },
          function(response) {
            // Error
            restService.error("Could not upload image.", response);
          }
      );
    }
  }
]);
