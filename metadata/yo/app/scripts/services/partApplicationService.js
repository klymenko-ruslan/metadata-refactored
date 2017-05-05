'use strict';

angular.module('ngMetaCrudApp')
  .factory('partApplicationService', ['$log', 'restService', function PartApplicationService($log, restService) {
    return {
      removeApplication: function(part_id, applications, idx) {
        var pa = applications[idx];
        var application_id = pa.carModelEngineYear.id;
        restService.removePartApplication(part_id, application_id).then(
          function() {
            applications.splice(idx, 1);
          },
          restService.error
        );
      }
    };
  }]);


