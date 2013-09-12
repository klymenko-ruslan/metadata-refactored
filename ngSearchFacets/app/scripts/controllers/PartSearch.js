'use strict';

angular.module('ngSearchFacetsApp')
  .controller('PartSearchCtrl', function ($scope, searchService) {

        // Query Parameters
        $scope.queryString = "";
        $scope.facetFilters = {};

        // Latest Results
        $scope.isSearching = false;
        $scope.searchResults = null;

        // Handle updating search results
        $scope.$watch('[queryString,facetFilters]', $scope.search, true);

        $scope.search = function() {
            $scope.isSearching = true;

            searchService($scope.queryString, $scope.facetFilters).then(function(searchResults) {
                $scope.isSearching = false;
                $scope.searchResults = searchResults.data;
            });
        };
  });
