"use strict";

angular.module("ngMetaCrudApp").controller("CarMakeFormCtrl", [
  "restService", "$scope", "$location", "$log", "$routeParams", "gToast",
  function(restService, $scope, $location, $log, $routeParams, gToast) {

    $scope.$on("form:created", function(event, data) {
      if (data.name === "carmakeForm") {
        $scope.carmakeForm = data.controller;
      }
    });

    $scope.save = function() {
      $scope.$broadcast("carmakeform:save", function(promise) {
        promise.then(
          function(carMake) {
            $log.log("Carmake has been successfully created: " + carMake.id);
            gToast.open("Carmake [" + carMake.id + "] has been successfully created.");
            $location.path('/application/carmake/list');
          },
          function (errorResponse) {
            restService.error("Could not create carmake.", response);
          }
        );
      });
    };

  }
]);
