/* global _, angular */

'use strict';

angular.module('ngMetaCrudApp').controller('SalesNoteListByPartCtrl', function(
        $scope, $log, $routeParams, ngTableParams, restService, Restangular, SalesNotes) {
    $scope.SalesNotes = SalesNotes;
        
    $scope.possibleStates = ["draft", "submitted", "approved", "rejected", "published"];

    $scope.isStateEnabled = function(state) {
        return _.contains($scope.search.states, state);
    };
    
    $scope.toggleState = function(state) {
        if ($scope.isStateEnabled(state)) {
          $scope.search.states = _.without($scope.search.states, state);
        } else {
            $scope.search.states.push(state);
        }
    }
        
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
          
          if (_.size($scope.search.states) < 1) {
              $defer.resolve([])
              return;
          }

          // Update the pagination info
          $scope.search.page = params.page() - 1;
          $scope.search.pageSize = params.count();
          
          $scope.notesPromise = Restangular.all('other/salesNote/search').post($scope.search).then(
                function (searchResults) {
                  $scope.notes = searchResults.content;

                  // Update the total and slice the result
                  $defer.resolve(searchResults.content);
                  params.total(searchResults.total);
                },
                function (errorResponse) {
                  restService.error("Couldn't search for sales notes.", errorResponse);
                  $defer.reject();
                });
          }
      });

    // Query Parameters
    $scope.search = {
        "primaryPartId": 1,
        "query": null,
        "includePrimary": true,
        "includeRelated": true,
        "states": ["draft", "submitted", "approved", "published"],
        "page": 0,
        "pageSize": 20
    };

    // Handle updating search results
    $scope.$watch('search', function (newVal, oldVal) {

      // Debounce
      if (angular.equals(newVal, oldVal, true)) {
        return;
      }

//      $scope.notesTableParams.reload();
    }, true);
  });
