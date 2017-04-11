"use strict";

angular.module("ngMetaCrudApp")

.controller("ChangelogSourcesListCtrl",
  ["$scope", "$log", "$location", "dialogs", "ngTableParams", "utils", "restService", "sourcesNames",
  function($scope, $log, $location, dialogs, ngTableParams, utils, restService, sourcesNames) {

    $scope.sourcesNames = sourcesNames;

    $scope.fltrSource = {
      name: null,
      description: null,
      url: null,
      sourceName: null
    };

    $scope.clearFilter = function() {
      $scope.fltrSource.name = null;
      $scope.fltrSource.description = null;
      $scope.fltrSource.url = null;
      $scope.fltrSource.sourceName = null;
    };

    $scope.refreshList = function() {
      $scope.sourceTableParams.reload();
    };

    // Handle updating search results
    $scope.$watch("[fltrSource]",
      function (newVal, oldVal) {
        // Debounce
        if (angular.equals(newVal, oldVal, true)) {
          return;
        }
        $scope.sourceTableParams.reload();
      },
      true
    );

    $scope.onCreateNewSource = function() {
      $location.path("/changelog/source/create");
    };

    $scope.onView = function(srcId) {
      $location.path("/changelog/source/" + srcId);
    };

    $scope.sourceTableParams = new ngTableParams(
      {
        page: 1,
        count: 25,
        sorting: {
          "name.lower_case_sort": "asc"
        }
      },
      {
        getData: function ($defer, params) {
          // Update the pagination info
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          var sortProperty, sortOrder;
          for (sortProperty in params.sorting()) break;
          if (sortProperty) {
            sortOrder = params.sorting()[sortProperty];
          }
          var snid = null;
          if ($scope.fltrSource.sourceName) {
            snid = $scope.fltrSource.sourceName.id;
          }
          restService.filterChangelogSource($scope.fltrSource.name, $scope.fltrSource.description,
            $scope.fltrSource.url, snid, sortProperty, sortOrder, offset, limit)
          .then(
            function (filtered) {
              // Update the total and slice the result
              $defer.resolve(filtered.hits.hits);
              params.total(filtered.hits.total);
            },
            function (errorResponse) {
              $log.log("Couldn't search for 'changelog source'.");
              $defer.reject();
            }
          );
        }
      }
    );

  }
]);
