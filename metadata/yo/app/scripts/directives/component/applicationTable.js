'use strict';

angular.module('ngMetaCrudApp').directive('applicationTable', function ($log,restService) {
  return {
    scope: true,
    restrict: 'E',
    replace: false,
    templateUrl: '/views/component/applicationTable.html',
    controller: function($scope, $dialogs) {
      restService.findPartApplications($scope.partId).then(
        function (applications) {
          $scope.applications = applications;
        },
        function (errorResponse) {
          $log.log("Could not get part's applications", errorResponse);
          restService.error("Could not get part's applications", errorResponse);
        }
      );
      $scope.remove = function(item) {
        $dialogs.confirm('Remove Application Item', 'Are you sure?').result.then(
          function() {
            // Yes
            var part_id = 567;        // TODO
            var application_id = 123; // TODO
            restService.removePartApplication(part_id, application_id).then(
              function() {
                alert('Removed!');
              },
              restService.error
            );
          }
        );
      }
    }
  };
});
