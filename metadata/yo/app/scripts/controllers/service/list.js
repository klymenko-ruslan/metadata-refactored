"use strict";

angular.module("ngMetaCrudApp")
  .controller("ServiceListCtrl", ["$scope", "$log", "gToast", "ngTableParams", "restService",
    function($scope, $log, gToast, ngTableParams, restService) {

    $scope.requiredSource = {};

    $scope.serviceTableParams = new ngTableParams({
      page: 1,
      count: 25,
      sorting: {
        "name": "asc"
      }
    }, {
      getData: function($defer, params) {
        var sortOrder;
        var sorting = params.sorting();
        for (var sortProperty in sorting) break;
        if (sortProperty) {
          sortOrder = sorting[sortProperty];
        }
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();

        restService.filterServices(sortProperty, sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            $scope.requiredSource = {};
            _.each(result.recs, function(srv) {
              $scope.requiredSource[srv.name] = srv.requiredSource;
            });
            $defer.resolve(result.recs);
            params.total(result.total);
          },
          function(errorResponse) {
            restService.error("Loading of list of services failed.", errorResponse);
            $defer.reject();
          });
      }
    });

    $scope.onChangeRequiredSource = function(srv) {
      var checked = $scope.requiredSource[srv.name];
      restService.setChangelogSourceRequiredForService(srv.id, checked).then(
        function success() {
          gToast.open("The field has been successfully updated.");
        },
        function failure(errorResponse) {
          restService.error("The operation failed.", errorResponse);
        }
      );
    };

  }]);
