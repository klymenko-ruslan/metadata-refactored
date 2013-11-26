'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartSearchCtrl', function ($log, $q, $scope, searchService, ngTableParams) {

      // Query Parameters
      $scope.search = {
        queryString: "",
        facetFilters: {}
      };

      // Latest Results
      $scope.searchResults = null;

      $scope.partTableParams = new ngTableParams({
        page: 1,
        count: 10,
        sorting: {
          manufacturerPartNumber: 'asc'
        }
      }, {
        getData: function($defer, params) {
          // Update the pagination info
          $scope.search.count = params.count();
          $scope.search.page = params.page();
          $scope.search.sorting = params.sorting();

          $log.log("Searching", $scope.search);

          searchService($scope.search).then(
              function(searchResults) {
                $scope.searchResults = searchResults.data;

                // Update the total and slice the result
                $defer.resolve($scope.searchResults.hits.hits);
                params.total($scope.searchResults.hits.total);
              },
              function(errorResponse) {
                alert("Could not complete search");
                $log.log("Could not complete search", errorResponse);
              });
        }
      });

      // Handle updating search results
      $scope.$watch('search', function() {
        $log.log("Searching", $scope.search);
        $scope.partTableParams.reload();
      }, true);

      $scope.$watch('actions', function(actions) {
        if (angular.isString(actions)) {
          $scope.actionList = $scope.actions.split(',');
        }
      }, true);

    });
