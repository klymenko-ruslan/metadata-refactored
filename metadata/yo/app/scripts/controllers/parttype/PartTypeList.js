"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartTypeListCtrl", ["$scope", "ngTableParams", "utils", "partTypes",
    function ($scope, ngTableParams, utils, partTypes) {
      $scope.partTypesTableParams = new ngTableParams({
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
