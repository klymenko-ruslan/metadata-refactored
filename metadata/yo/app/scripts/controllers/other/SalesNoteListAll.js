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
      sorting: {}
    }, {
      getData: function ($defer, params) {
          $log.info("Searching", $scope.search, params);
          
          
          
          if (_.size($scope.search.states) < 1) {
              $defer.resolve([])
              return;
          }

          // Update the pagination info
          $scope.search.page = params.page() - 1;
          $scope.search.pageSize = params.count();
//          $scope.search.states = _.chain($scope.states).map(function(value, state) {
//              if (value) {
//                  return state;
//              } else {
//                  return null;
//              }
//          }).compact().value();
          
          $scope.notesPromise = Restangular.all('other/salesNote/searchWithParts').post($scope.search).then(
                function (searchResults) {

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
        "query": null,
        "includePrimary": true,
        "includeRelated": true,
        "statesObject": {},
        "states": [],
        "page": 0,
        "pageSize": 20
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
