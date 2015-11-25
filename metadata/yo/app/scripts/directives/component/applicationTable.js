'use strict';

angular.module('ngMetaCrudApp').directive('applicationTable', function ($log,restService) {
  return {
    scope: true,
    restrict: 'E',
    replace: false,
    templateUrl: '/views/component/applicationTable.html',
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
        $dialogs.confirm('Remove Application Item', 'Are you sure?').result.then(
          function() {
            // Yes
            // TODO
            // alert('partApplicationService: ' + angular.toJson(partApplicationService));
            // partApplicationService.removeApplication($scope.applications, idx);
            $scope.applications.splice(idx, 1);
          }
        );
      }
    }
  };
}).directive('applicationTableExisting', function ($log, restService) {
  return {
    scope: true,
    restrict: 'E',
    replace: false,
    templateUrl: '/views/component/applicationTable_Existing.html',
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
    }
  };
}).directive('applicationTablePicked', function ($log, restService) {
  return {
    scope: true,
    restrict: 'E',
    replace: false,
    templateUrl: '/views/component/applicationTable_Picked.html',
    controller: function($scope, $dialogs) {
    $scope.applications = null;
      $scope.unpick = function(idx) {
        $scope.applications.splice(idx, 1);
      }
    }
  };
});
