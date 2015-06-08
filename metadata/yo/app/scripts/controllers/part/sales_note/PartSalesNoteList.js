'use strict';

angular.module('ngMetaCrudApp')
	.controller('PartSalesNoteListCtrl', function($scope, $log, $q, $location,
    $routeParams, ngTableParams, restService, Restangular, $dialogs, gToast) {
    $scope.partId = $routeParams.id;

    // Load the part
    $scope.part = null;
    $scope.partPromise = restService.findPart($scope.partId).then(
        function (part) {
            $scope.part = part;

            // Make sure we're using the correct part type
            $scope.partType = part.partType.name;
        },
        function (errorResponse) {
            $log.log("Could not get part details", errorResponse);
            restService.error("Could not get part details", errorResponse);
        });

    // Latest Results
    $scope.notes = null;

    // Notes Table
    $scope.notesTableParams = new ngTableParams({
      page: 1,
      count: 10,
      sorting: {}
    }, {
      getData: function ($defer, params) {

          // Update the pagination info
          $scope.search.count = params.count();
          $scope.search.page = params.page();
          $scope.search.sorting = params.sorting();

          $scope.notesPromise = Restangular.one('other/salesNote/listByPartId', $scope.partId).get().then(
                function (searchResults) {
                  $scope.notes = searchResults.content;

                  // Update the total and slice the result
                  $defer.resolve(searchResults.numberOfElements);
                  params.total(searchResults.totalElements);
                },
                function (errorResponse) {
                  restService.error("Couldn't search for sales notes.", errorResponse);
                  $defer.reject();
                });
          }
      });


    // Query Parameters
    $scope.search = {
      partNumber: "",
      facets: {},
      sort: {}
    };

    $scope.clear = function() {
      $scope.search = {
        partNumber: "",
        facets: {},
        sort: {}
      }
    }

    // Handle updating search results
    $scope.$watch('search', function (newVal, oldVal) {

      // Debounce
      if (angular.equals(newVal, oldVal, true)) {
        return;
      }

      $scope.notesTableParams.reload();
    }, true);
  });
