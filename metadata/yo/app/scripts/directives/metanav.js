'use strict';

angular.module('ngMetaCrudApp')
  .directive('metanav', function(dialogs, $interval, gToast, User, restService, Restangular) {
    return {
      transclude: true,
      templateUrl: '/views/component/Metanav.html',
      restrict: 'E',
      controller: function($scope) {
        $scope.User = User;

        // Probably not the greatest place for this startup/teardown code,
        // but metanav is available everywhere when logged in
        var timer = $interval(function() {
          restService.refreshStatus();
        }, 1000);
        $scope.$on("$destroy", function() {
          $interval.cancel(timer);
        });

        $scope.rebuildBom = function() {
          dialogs.confirm(
            "Rebuild BOM for all parts?",
            "You need to run this if changes have been made directly to the database. Proceed?").result.then(
            function() {
              // Yes
              Restangular.one("bom/rebuild").post("start", {"indexBoms": true}).then(
                function() {
                  // Success
                  gToast.open("Rebuilding BOM.");
                },
                function(response) {
                  // Error
                  restService.error("Could not rebuild BOM", response);
                });
            },
            function() {
              // No
            }
          );
        };

        $scope.reindexPartSearch = function() {
          dialogs.confirm(
            'Reindex search engine data for parts?',
            'You need to run this if changes have been made directly to the database. Proceed?').result.then(
            function() {
              // Yes
              Restangular.all('search/part/indexAll').post().then(
                function() {
                  // Success
                  gToast.open('Indexing of parts started, check the server log for progress.');
                },
                function(response) {
                  // Error
                  dialogs.error(
                    'Could not index search engine data for parts.',
                    'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
                });
            },
            function() {
              // No
            }
          );
        };

        $scope.reindexApplicationSearch = function() {
          dialogs.confirm(
            'Reindex search engine data for applications?',
            'You need to run this if changes have been made directly to the database. Proceed?').result.then(
            function() {
              // Yes
              Restangular.all('search/application/indexAll').post().then(
                function() {
                  // Success
                  gToast.open('Indexing of applications started, check the server log for progress.');
                },
                function(response) {
                  // Error
                  dialogs.error(
                    'Could not index search engine data.',
                    'Here\'s the error: <pre>' + response.status +'</pre>');
                });
            },
            function() {
              // No
            }
          );
        };

        $scope.reindexSalesNotesSearch = function() {
          dialogs.confirm(
            'Reindex search engine data for sales notes?',
            'You need to run this if changes have been made directly to the database. Proceed?').result.then(
            function() {
              // Yes
              Restangular.all('search/salesnotesparts/indexAll').post().then(
                function() {
                  // Success
                  gToast.open('Indexing of sales notes started, check the server log for progress.');
                },
                function(response) {
                  // Error
                  dialogs.error(
                    'Could not index search engine data.',
                    'Here\'s the error: <pre>' + response.status +'</pre>');
                });
            },
            function() {
              // No
            }
          );
        }

        $scope.clearHibernate = function() {
          dialogs.confirm(
            'Clear Hibernate cache?',
            'You need to run this if changes have been made directly to the database. Proceed?').result.then(
            function() {
              // Yes
              Restangular.one('hibernate/clear').get().then(
                function() {
                  // Success
                  gToast.open('Hibernate cache cleared.');
                },
                function(response) {
                  // Error
                  dialogs.error(
                    'Could not index search engine data.',
                    'Here\'s the error: <pre>' + response.status + '</pre>');
                });
            },
            function() {
              // No
            }
          );
        };
      }
    };
  });
