'use strict';

angular.module('ngMetaCrudApp')
  .directive('metanav', function ($dialogs, $log, gToast, Restangular) {
    return {
      transclude: true,
      template:
          '<div class="row">' +

           // Transcluded
          '<ul class="nav nav-pills" ng-transclude  style="display: inline-block;">' +
          '</ul>' +

          '<ul class="nav nav-pills" style="display: inline-block;">' +

           // Parts
          '  <li>' +
          '    <a authorize="ROLE_READ"' +
          '       ng-href="#/"' +
          '       class="btn btn-default">' +
          '      <i class="fa fa-bars"></i>' +
          '    Part List' +
          '    </a>' +
          '  </li>' +

           // Indexing and Cache
          '  <li class="dropdown" authorize="ROLE_ADMIN">' +
          '    <a class="btn btn-warning dropdown-toggle" data-toggle="dropdown" href="#">' +
          '      Indexing and Cache <span class="caret"></span>' +
          '    </a>' +
          '    <ul class="dropdown-menu">' +
          '      <li>' +
          '        <a ng-click="reindexTurbos()"' +
          '           class="btn">' +
          '          <i class="fa fa-cogs"></i>' +
          '          Reindex Turbos' +
          '        </a>' +
          '      </li>' +
          '      <li>' +
          '        <a ng-click="reindexSearch()"' +
          '           class="btn">' +
          '          <i class="fa fa-cogs"></i>' +
          '          Reindex Search' +
          '        </a>' +
          '      </li>' +
          '      <li>' +
          '        <a ng-click="clearHibernate()"' +
          '           class="btn">' +
          '          <i class="fa fa-cogs"></i>' +
          '          Clear Hibernate Cache' +
          '        </a>' +
          '      </li>' +
          '    </ul>' +
          '  </li>' +

           // Security
          '  <li class="dropdown" authorize="ROLE_ADMIN">' +
          '    <a class="btn btn-default dropdown-toggle" data-toggle="dropdown">' +
          '      Security <span class="caret"></span>' +
          '    </a>' +
          '    <ul class="dropdown-menu">' +
          '     <li>' +
          '       <a ng-href="#/security/groups/"' +
          '          class="btn">' +
          '         <i class="fa fa-group"></i>' +
          '         Groups' +
          '       </a>' +
          '     </li>' +
          '     <li>' +
          '       <a ng-href="#/security/users/"' +
          '          class="btn">' +
          '         <i class="fa fa-user"></i>' +
          '         Users' +
          '       </a>' +
          '     </li>' +
          '    </ul>' +
          '  </li>'+

           // My Account
          '  <li>' +
          '    <a ng-href="#/security/me"' +
          '       class="btn btn-primary">' +
          '      <i class="fa fa-smile-o"></i>' +
          '      My Account' +
          '    </a>' +
          '  </li>' +
          '</ul>' +
          '</div>',
      restrict: 'E',
      controller: function($scope) {


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
                Restangular.all('part/all/indexSearch').post().then(
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
