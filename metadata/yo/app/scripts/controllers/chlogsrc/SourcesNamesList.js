"use strict";

angular.module("ngMetaCrudApp")

.controller("ChangelogSourcesNamesListCtrl",
  ["$scope", "$log", "gToast", "dialogs", "ngTableParams", "restService",
  function($scope, $log, gToast, dialogs, ngTableParams, restService) {

    $scope.forms = {
      create: null
    };

    $scope.data = {
      newName: null
    };

    $scope.sourcesNamesTableParams = new ngTableParams(
      {
        page: 1,
        count: 25,
        sorting: {
          "name": "asc"
        }
      },
      {
        getData: function ($defer, params) {
          // Update the pagination info
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          var sortProperty, sortOrder;
          for (sortProperty in params.sorting()) break;
          if (sortProperty) {
            sortOrder = params.sorting()[sortProperty];
          }
          restService.filterChangelogSourceNames(sortProperty, sortOrder, offset, limit).then(
            function (result) {
              // Update the total and slice the result
              $defer.resolve(result.recs);
              params.total(result.total);
            },
            function (errorResponse) {
              $log.log("Couldn't load changelog sources names.");
              $defer.reject();
            }
          );
        }
      }
    );

    $scope.onCreate = function() {
      restService.createChangeSourceName($scope.data.newName).then(
        function success() {
          gToast.open("The source name has successfully been created.");
          $scope.data.newName = null;
          $scope.sourcesNamesTableParams.reload();
        },
        function (errorResponse) {
          restService.error("Could not create a new source name: " + $scope.data.newName, errorResponse);
        }
      );

    };

    $scope.onEdit = function(entity) {
    };

    $scope.onRemove = function(entity) {
      dialogs.confirm("Confirmation",
        "Are you sure? Do you want to remove this Source Name?").result.then(
          function yes() {
            restService.removeChangelogSourceName(entity.id).then(
              function(removed) {
                if (!removed) {
                  dialogs.error("Failure", "This Source Name [" + entity.id + "] - " + entity.name +
                    " can't be deleted because it is referenced by some Source(s).");
                } else {
                  $scope.sourcesNamesTableParams.reload();
                  gToast.open("The Source Name [" + entity.id + "] - " + entity.name +
                    " has successfully been removed.");
                }
              },
              function(errorResponse) {
                restService.error("Could not remove the Source Name.", errorResponse);
              }
            );
          },
          function no() {
          }
        );
    };

  }
]).directive("uniqueChangelogSourceName", ["$log", "$q", "restService", function($log, $q, restService) {
  // Validator for uniqueness of the changelog source name.
  return {
    require: "ngModel",
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.nonUniqueName = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        restService.findChangelogSourceNameByName(viewValue).then(
          function(changelogSource) {
            if (changelogSource === undefined) {
              def.resolve();
            } else {
              def.reject();
              /*
              var id = $scope.$eval("source.id");
              if (changelogSource.id === id) {
                def.resolve();
              } else {
                def.reject();
              }
              */
            }
          },
          function (errorResponse) {
            $log.log("Couldn't validate name of the changelog source name: " + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
