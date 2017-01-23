"use strict";

angular.module("ngMetaCrudApp")

.controller("ChangelogSourcesFormCtrl", [
    "$scope", "$log", "$location", "gToast", "ngTableParams", "restService", "source",
  function($scope, $log, $location, gToast, ngTableParams, restService, source) {
    $scope.source = source;
  }
]);

