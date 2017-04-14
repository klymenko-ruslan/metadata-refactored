"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartTypeListCtrl", ["$scope", "NgTableParams", "utils", "partTypes",
    function ($scope, NgTableParams, utils, partTypes) {
      $scope.partTypesTableParams = new NgTableParams({
        "page": 1,
        "count": 10,
        "sorting": {
          "id": "asc"
        }
      }, {
        "getData": utils.localPagination(partTypes, "id")
      });
    }
  ]);
