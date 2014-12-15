'use strict';

angular.module('ngMetaCrudApp')
    .directive('metanav', function ($dialogs, $interval, gToast, User, restService, Restangular) {
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
          $scope.$on('$destroy', function() {
            $interval.cancel(timer);
          });

          $scope.rebuildBomAncestry = function() {
            $dialogs.confirm(
                'Rebuild BOM ancestry for all parts?',
                'You need to run this if changes have been made directly to the database. Proceed?').result.then(
                function() {
                  // Yes
                  Restangular.all('part/all').customGET('rebuildBomAncestry').then(
                      function() {
                        // Success
                        gToast.open('Rebuilding BOM ancestry.');
                      },
                      function(response) {
                        // Error
                        restService.error("Could not rebuild BOM ancestry", response);
                      });
                },
                function() {
                  // No
            }
            );
          };

          $scope.reindexSearch = function() {
            $dialogs.confirm(
                    'Reindex search engine data?',
                    'You need to run this if changes have been made directly to the database. Proceed?').result.then(
                function() {
                  // Yes
                  Restangular.all('search/indexAll').post().then(
                      function() {
                        // Success
                        gToast.open('Indexing started, check the server log for progress.');
                      },
                      function(response) {
                        // Error
                        $dialogs.error(
                            'Could not index search engine data.',
                            'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
                      });
                },
                function() {
                  // No
            }
            );
          };

          $scope.clearHibernate = function() {
            $dialogs.confirm(
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
                        $dialogs.error(
                            'Could not index search engine data.',
                            'Here\'s the error: <pre>' + response.status +'</pre>');
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
