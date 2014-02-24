'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartListCtrl', function ($scope, $log, $modal, $dialogs, gToast) {

        $scope.createPart = function () {
            var modalInstance = $modal.open({
                templateUrl: '/views/part/PartCreateModal.html',
                controller: 'PartCreateModalCtrl'
            });
        };

      $scope.reindexTurbos = function() {
        $dialogs.confirm(
                "Reindex all part turbos?",
                "You should only need to run this if changes have been made directly to the database. Proceed?").result.then(
            function() {
              // Yes
              Restangular.one('all/indexTurbos').get().then(
                  function() {
                    // Success
                    gToast.open("Indexing started, check the server log for progress.");
                  },
                  function(response) {
                    // Error
                    $dialogs.error(
                        "Could not index search engine data.",
                        "Here's the error: <pre>" + response.status +"</pre>");
                  });
            },
            function() {
              // No
            });
      }

      $scope.reindexSearch = function() {
        $dialogs.confirm(
                "Reindex search engine data?",
                "You should only need to run this if changes have been made directly to the database. Proceed?").result.then(
            function() {
              // Yes
              Restangular.one('all/indexSearch').get().then(
                  function() {
                    // Success
                    gToast.open("Indexing started, check the server log for progress.");
                  },
                  function(response) {
                    // Error
                    $dialogs.error(
                        "Could not index search engine data.",
                        "Here's the error: <pre>" + response.status +"</pre>");
                  });
            },
            function() {
              // No
            });
      }

    });
