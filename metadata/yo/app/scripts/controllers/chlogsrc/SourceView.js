"use strict";

angular.module("ngMetaCrudApp")

.controller("ChangelogSourcesViewCtrl", [
    "$scope", "$log", "$location", "gToast", "ngTableParams", "utils", "restService", "source",
  function($scope, $log, $location, gToast, ngTableParams, utils, restService, source) {

    $scope.source = source;

    $scope.attachmentsTableParams = new ngTableParams(
      {
        page: 1,
        count: 10,
        sorting: {}
      },
      {
        getData: utils.localPagination($scope.source.attachments)
      }
    );

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
