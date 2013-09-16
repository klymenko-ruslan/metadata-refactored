'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartSearchCtrl', function ($scope, searchService, $location, $routeParams, ngTableParams) {
        $scope.partTableParams = new ngTableParams({
            count: 10,
            page: 1,
            counts: [10, 25, 50, 100]
        });

        // Query Parameters
        $scope.search = {
            queryString: "",
            facetFilters: {}
        }

        // Latest Results
        $scope.isSearching = false;
        $scope.searchResults = null;

        $scope.doSearch= function () {
            $scope.isSearching = true;

            searchService($scope.search).then(function (searchResults) {
                $scope.isSearching = false;
                $scope.searchResults = searchResults.data;
            });
        };

        $scope.$watch("searchResults.hits.total", function(total) {
            $scope.partTableParams.total = total;
        });

        $scope.$watch("partTableParams", function() {
            $scope.search.count = $scope.partTableParams.count;
            $scope.search.page = $scope.partTableParams.page;
            $scope.search.sorting = $scope.partTableParams.sorting;
        }, true);

        // Handle updating search results
        $scope.$watch('search', $scope.doSearch, true);

    });
