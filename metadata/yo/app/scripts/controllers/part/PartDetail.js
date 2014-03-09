'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartDetailCtrl', function ($scope, $log, $q, $location, $routeParams, ngTableParams, restService, Restangular, $dialogs, gToast) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

        $scope.part = null;
        $scope.partPromise = restService.findPart($scope.partId).then(
            function (part) {
                $scope.part = part;

                // Make sure we're using the correct part type
                $scope.partType = part.partType.typeName;

                // Reload the table
                $scope.bomTableParams.reload();
            },
            function (errorResponse) {
                $log.log("Could not get part details", errorResponse);
                alert("Could not get part details");
            });

        $scope.reindexTurbos = function() {

          $dialogs.confirm(
                  "Reindex part turbos?",
                  "You need to run this if changes have been made directly to the database. Proceed?").result.then(
              function() {
                // Yes
                Restangular.one("part", $scope.partId).one('indexTurbos').get().then(
                    function() {
                      // Success
                      gToast.open("Indexing started, check the server log for progress.");
                    },
                    function(response) {
                      // Error
                      $dialogs.error(
                          "Could not index part turbos.",
                          "Here's the error: <pre>" + response.status +"</pre>");
                    });
              },
              function() {
                // No
              });
        }

    });
