'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartSearchCtrl', function ($log, $q, $scope, searchService, ngTableParams) {

      // Query Parameters
      $scope.search = {
        queryString: "",
        partType: null,
        facetFilters: {}
      };

      // Latest Results
      $scope.searchResults = null;

      $scope.partTableParams = new ngTableParams({
        page: 1,
        count: 10
      }, {
        getData: function($defer, params) {
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

      $scope.$watch("partTableParams", function() {
        $scope.search.count = $scope.partTableParams.count();
        $scope.search.page = $scope.partTableParams.page();
        $scope.search.sorting = $scope.partTableParams.sorting();
      }, true);

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

      $scope.$watch('partType', function(partType) {
        $scope.search.partType = partType;
      });

    });
