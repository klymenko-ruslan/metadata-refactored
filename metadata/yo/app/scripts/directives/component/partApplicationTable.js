'use strict';

angular.module('ngMetaCrudApp')
  .directive('partApplicationTable', ['$log', 'NgTableParams', 'toastr', 'restService',
    function ($log, NgTableParams, toastr, restService) {
      return {
        scope: true,
        restrict: 'E',
        replace: false,
        templateUrl: '/views/component/partApplicationTable.html',
        controller: function($scope, dialogs, partApplicationService) {

          $scope.applications = null;
          $scope.applicationsTableParams = new NgTableParams({
            'page': 1,
            'count': 10,
            'sorting': {
            }
          }, {
            'dataset': $scope.applications
          });

          restService.findPartApplications($scope.partId).then(
            function (applications) {
              $scope.applications = applications;
              $scope.applicationsTableParams.settings({dataset: $scope.applications});
            },
            function (errorResponse) {
              $log.log('Could not get part\'s applications', errorResponse);
              restService.error('Could not get part\'s applications', errorResponse);
            }
          );

          $scope.remove = function(app) {
            var applicationId = app.carModelEngineYear.id;
            dialogs.confirm('Unlink Application Item', 'Are you sure?').result.then(
              function yes() {
                restService.removePartApplication($scope.partId, applicationId).then(
                  function success(applications) {
                    $scope.applications = applications;
                    $scope.applicationsTableParams.settings({dataset: $scope.applications});
                    toastr.success('The applications has been successfully unlinked.');
                  },
                );
              },
              function no() {
              });
          };

        }
      };
    }
  ]
  );
