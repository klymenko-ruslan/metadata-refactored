'use strict';

angular.module('ngMetaCrudApp')
  .factory('partApplicationService', function PartApplicationService(restService) {
    return {
      removeApplication: function(applications, idx) {
        item = applications[idx];
        // TODO: extract IDs from 'item'.
        var part_id = 567;
        var application_id = 123; // TODO
//            restService.removePartApplication(part_id, application_id).then(
//              function() {
//                $scope.applications.splice(idx, 1);
//              },
//              restService.error
//            );
        applications.splice(idx, 1);
      }
    }
  });


