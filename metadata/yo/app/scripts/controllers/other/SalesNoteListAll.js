/* global _, angular */

'use strict';

angular.module('ngMetaCrudApp').controller('SalesNoteListAllCtrl', function(
        $scope, $log, $routeParams, ngTableParams, restService, Restangular, SalesNotes) {
    $scope.SalesNotes = SalesNotes;

    $scope.states = {
        "current": {
            "draft":true,
            "submitted":true,
            "approved":true,
            "published":true
        }
    };

    // Notes Table
    $scope.notesTableParams = new ngTableParams({
      page: 1,
      count: 10,
      sorting: {
        createDate: 'desc'
      }
    }, {
      getData: function ($defer, params) {
          $log.info("Searching", $scope.search, params);
          if (_.size($scope.search.states) < 1) {
              $defer.resolve([])
              return;
          }
          var sorting = params.sorting();
          for (var sortProperty in sorting) break;
          if (sortProperty) {
            var sortOrder = sorting[sortProperty];
          }
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          $scope.notesPromise = restService.filterSalesNotes($scope.search.query, $scope.search.includePrimary,
            $scope.search.includeRelated, $scope.search.states, sortProperty, sortOrder, offset, limit).then(
                function (searchResults) {
                  // Update the total and slice the result
                  $defer.resolve(searchResults.hits.hits);
                  params.total(searchResults.hits.total);
                },
                function (errorResponse) {
                  restService.error("Couldn't search for sales notes.", errorResponse);
                  $defer.reject();
                });
          }
      });

    // Query Parameters
    $scope.search = {
        "query": null,
        "includePrimary": true,
        "includeRelated": true,
        "states": [],
    };

    // Keep the states up-to-date
    $scope.$watch('states.current', function (currentStates) {

        // Get a list of active states, currentStates={stateName:boolean, ...}
        var newStates = _.chain(currentStates)
                         .map(function(value, key) {
                             return value === true ? key : null;
                         }).compact().value();

        // Update the states and reload the table
        if (!angular.equals($scope.search.states, newStates)) {
            $scope.search.states = newStates;
        }
    }, true);

    // Refresh the search when it changes
    $scope.$watch('search', function() {
        $scope.notesTableParams.reload();
    }, true);
  });
