'use strict';

angular.module('ngMetaCrudApp')
    .directive('metanav', function ($dialogs, gToast, User, Restangular) {
      return {
        transclude: true,
        templateUrl: '/views/component/Metanav.html',
        restrict: 'E',
        controller: function($scope) {
          $scope.User = User;

          $scope.reindexTurbos = function() {
            $dialogs.confirm(
                    "Reindex all part turbos?",
                    "You need to run this if changes have been made directly to the database. Proceed?").result.then(
                function() {
                  // Yes
                  Restangular.all('part/all/indexTurbos').get().then(
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
                    "You need to run this if changes have been made directly to the database. Proceed?").result.then(
                function() {
                  // Yes
                  Restangular.all('search/indexAll').post().then(
                      function() {
                        // Success
                        gToast.open("Indexing started, check the server log for progress.");
                      },
                      function(response) {
                        // Error
                        $dialogs.error(
                            "Could not index search engine data.",
                            "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                      });
                },
                function() {
                  // No
                });
          }

          $scope.clearHibernate = function() {
            $dialogs.confirm(
                    "Clear Hibernate cache?",
                    "You need to run this if changes have been made directly to the database. Proceed?").result.then(
                function() {
                  // Yes
                  Restangular.one('hibernate/clear').get().then(
                      function() {
                        // Success
                        gToast.open("Hibernate cache cleared.");
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
        }
      };
    });
