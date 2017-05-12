'use strict';

// Argument primaryPartID is initialized during resolving before
// invocation of this controller (see app.js).
angular.module('ngMetaCrudApp')

.controller('SalesNoteListCtrl', ['$scope', '$log', '$routeParams',
  'NgTableParams', 'restService', 'SalesNotes', 'primaryPartId',

  function($scope, $log, $routeParams, NgTableParams, restService,
    SalesNotes, primaryPartId)
  {

    $scope.states = {
      'current': {
        'draft': true,
        'submitted': true,
        'approved': true,
        'rejected': false,
        'published': true
      }
    };

    $scope.SalesNotes = SalesNotes;
    $scope.partId = $routeParams.id;
    $scope.part = null;

    // Load the part if we know partID.
    if ($scope.partId) {
      $scope.partPromise = restService.findPart($scope.partId).then(
        function (part) {
            $scope.part = part;
            // Make sure we're using the correct part type
            $scope.partType = part.partType.name;
        },
        function (errorResponse) {
            $log.log('Could not get part details', errorResponse);
            restService.error('Could not get part details', errorResponse);
        }
      );
    }

    // Notes Table
    $scope.notesTableParams = new NgTableParams({
      page: 1,
      count: 10,
      sorting: {
        createDate: 'desc'
      }
    }, {
      getData: function(params) {
        if (_.size($scope.search.states) < 1) {
          return [];
        }
        var sortOrder;
        var sorting = params.sorting();
        for (var sortProperty in sorting) break;
        if (sortProperty) {
          sortOrder = sorting[sortProperty];
        }
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();
        $scope.notesPromise = restService
          .filterSalesNotes($scope.search.partNumber, $scope.search.comment,
            primaryPartId, $scope.search.includePrimary,
            $scope.search.includeRelated, $scope.search.states,
            sortProperty, sortOrder, offset, limit)
          .then(
            function(searchResults) {
              // Update the total and slice the result
              params.total(searchResults.hits.total);
              return searchResults.hits.hits;
            },
            function(errorResponse) {
              restService.error('Couldn\'t search for sales notes.',
                errorResponse);
            }
          );
        return $scope.notesPromise;
      }
    });
    // Query Parameters
    $scope.search = {
      'partNumber': null,
      'includePrimary': true,
      'includeRelated': true,
      'states': [],
      'comment': null
    };

    // Keep the states up-to-date
    $scope.$watch('states.current', function(currentStates) {

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
    $scope.$watch('search', function(newVal, oldVal) {
      // Debounce
      if (angular.equals(newVal, oldVal)) {
        return;
      }
      $scope.notesTableParams.reload();
    }, true);

  }
]);
