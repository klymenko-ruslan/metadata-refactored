'use strict';

angular.module('ngMetaCrudApp').directive('partApplicationTable', function ($log,restService) {
  return {
    scope: true,
    restrict: 'E',
    replace: false,
    templateUrl: '/views/component/partApplicationTable.html',
    controller: function($scope, $dialogs, partApplicationService) {
      restService.findPartApplications($scope.partId).then(
        function (applications) {
          $scope.applications = applications;
        },
        function (errorResponse) {
          $log.log("Could not get part's applications", errorResponse);
          restService.error("Could not get part's applications", errorResponse);
        }
      );
      $scope.remove = function(idx) {
        $dialogs.confirm('Unlink Application Item', 'Are you sure?').result.then(
          function() {
            // Yes
            partApplicationService.removeApplication($scope.partId, $scope.applications, idx);
          }
        );
      }
    }
  };
});
