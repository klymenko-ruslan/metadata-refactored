'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartSearchCtrl', function ($scope, searchService, ngTableParams) {
        $scope.partTableParams = new ngTableParams({
            count: 10,
            page: 1,
            counts: [10, 25, 50, 100]
        });

        // Query Parameters
        $scope.search = {
            queryString: "",
            partType: null,
            facetFilters: {}
        };

        // Latest Results
        $scope.isSearching = false;
        $scope.searchResults = null;

        $scope.doSearch = function () {

            // Don't search if we already are
            if ($scope.isSearching) {
                return;
            }

            // Search
            $scope.isSearching = true;

            searchService($scope.search).then(function (searchResults) {
                $scope.isSearching = false;
                $scope.searchResults = searchResults.data;
            }, function(result) {
                $scope.isSearching = false;
                alert("Could not get search results.");
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
        $scope.$watch('search', function(search) {
            console.log("Search: " + JSON.stringify(search));
            $scope.doSearch();
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
