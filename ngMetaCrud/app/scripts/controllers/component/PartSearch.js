'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartSearchCtrl', function ($scope, searchService, $location, $routeParams, ngTableParams) {
        $scope.partTableParams = new ngTableParams({
            count: 25,
            page: 1,
            total: 0,
            counts: [10, 25, 50, 100]
        });

        // Query Parameters
        $scope.queryString = "";
        $scope.facetFilters = {};

        // Latest Results
        $scope.isSearching = false;
        $scope.searchResults = null;

        // Handle updating search results
        $scope.$watch('[queryString,facetFilters]', $scope.search, true);

        $scope.search = function () {
            $scope.isSearching = true;

            searchService($scope.queryString, $scope.facetFilters).then(function (searchResults) {
                $scope.isSearching = false;
                $scope.searchResults = searchResults.data;
            });
        };
    });
