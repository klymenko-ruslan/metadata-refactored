"use strict";

angular.module("ngMetaCrudApp")
  .directive("metanav", function(dialogs, $interval, toastr, User, restService) {
    return {
      transclude: true,
      templateUrl: "/views/component/Metanav.html",
      restrict: "E",
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
              restService.rebuildBom({"indexBoms": true}).then(
                function() {
                  // Success
                  toastr.success("Rebuilding BOM.");
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
            "Reindex search engine data for parts?",
            "You need to run this if changes have been made directly to the database. Proceed?").result.then(
            function() {
              // Yes
              restService.reindexAllParts().then(
                function() {
                  // Success
                  toastr.success("Indexing of parts started, check the server log for progress.");
                },
                function(response) {
                  // Error
                  dialogs.error(
                    "Could not index search engine data for parts.",
                    "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                });
            },
            function() {
              // No
            }
          );
        };

        $scope.reindexApplicationSearch = function() {
          dialogs.confirm(
            "Reindex search engine data for applications?",
            "You need to run this if changes have been made directly to the database. Proceed?").result.then(
            function() {
              // Yes
              restService.reindexAllApplications().then(
                function() {
                  // Success
                  toastr.success("Indexing of applications started, check the server log for progress.");
                },
                function(response) {
                  // Error
                  dialogs.error(
                    "Could not index search engine data.",
                    "Here's the error: <pre>" + response.status +"</pre>");
                });
            },
            function() {
              // No
            }
          );
        };

        $scope.reindexSalesNotesSearch = function() {
          dialogs.confirm(
            "Reindex search engine data for sales notes?",
            "You need to run this if changes have been made directly to the database. Proceed?").result.then(
            function() {
              // Yes
              restService.reindexAllSalesNotes().then(
                function() {
                  // Success
                  toastr.success("Indexing of sales notes started, check the server log for progress.");
                },
                function(response) {
                  // Error
                  dialogs.error(
                    "Could not index search engine data.",
                    "Here's the error: <pre>" + response.status +"</pre>");
                });
            },
            function() {
              // No
            }
          );
        }

        $scope.clearHibernate = function() {
          dialogs.confirm(
            "Clear Hibernate cache?",
            "You need to run this if changes have been made directly to the database. Proceed?").result.then(
            function() {
              // Yes
              restService.clearHibernate().then(
                function() {
                  // Success
                  toastr.success("Hibernate cache cleared.");
                },
                function(response) {
                  // Error
                  dialogs.error(
                    "Could not index search engine data.",
                    "Here's the error: <pre>" + response.status + "</pre>");
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
