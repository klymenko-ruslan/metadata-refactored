'use strict';

angular.module('ngMetaCrudApp')
  .controller('ServiceListCtrl', ['$scope', '$log', 'toastr', 'NgTableParams', 'restService',
    function($scope, $log, toastr, NgTableParams, restService) {

    $scope.requiredSource = {};

    $scope.serviceTableParams = new NgTableParams({
      page: 1,
      count: 25,
      sorting: {
        'name': 'asc'
      }
    }, {
      getData: function(params) {
        var sortOrder;
        var sorting = params.sorting();
        for (var sortProperty in sorting) break;
        if (sortProperty) {
          sortOrder = sorting[sortProperty];
        }
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();

        return restService.filterServices(sortProperty, sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            $scope.requiredSource = {};
            _.each(result.recs, function(srv) {
              $scope.requiredSource[srv.name] = srv.requiredSource;
            });
            params.total(result.total);
            return result.recs;
          },
          function(errorResponse) {
            restService.error('Loading of list of services failed.', errorResponse);
          });
      }
    });

    $scope.onChangeRequiredSource = function(srv) {
      var checked = $scope.requiredSource[srv.name];
      restService.setChangelogSourceRequiredForService(srv.id, checked).then(
        function success() {
          toastr.success('The field has been successfully updated.');
        },
        function failure(errorResponse) {
          restService.error('The operation failed.', errorResponse);
        }
      );
    };

  }]);
