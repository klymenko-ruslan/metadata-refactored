"use strict";

angular.module("ngMetaCrudApp")

.controller("ChangelogSourcesViewCtrl", [
    "$scope", "$log", "$location", "gToast", "ngTableParams", "restService", "source",
  function($scope, $log, $location, gToast, ngTableParams, restService, source) {

    $scope.source = source;

    $scope.onViewList = function() {
      $location.path("/changelog/source/list");
    };

    $scope.onEdit = function() {
      $location.path("/changelog/source/" + $scope.source.id + "/form");
    };

    $scope.onRemove = function() {
    // TODO
    };

  }
]);
