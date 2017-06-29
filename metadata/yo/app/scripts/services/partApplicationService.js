'use strict';

angular.module('ngMetaCrudApp')
  .factory('partApplicationService', ['$log', 'restService', function PartApplicationService($log, restService) {
    return {
      removeApplication: function(partId, applications, idx) {
        var pa = applications[idx];
        var applicationId = pa.carModelEngineYear.id;
        restService.removePartApplication(partId, applicationId).then(
          function() {
            applications.splice(idx, 1);
          },
          restService.error
        );
      }
    };
  }]);


