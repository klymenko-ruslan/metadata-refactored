'use strict';

angular.module('ngMetaCrudApp')
  .factory('partApplicationService', ['$log', 'restService', function PartApplicationService($log, restService) {
    return {
      removeApplication: function(applications, idx) {
        $log.debug('applications: ' + angular.toJson(applications));
        alert('idx: ' + idx + '\n' + 'applications: ' + applications + '\ntype: ' + typeof(applications));
        partApplication = applications[idx];
        alert('partApplication: ' + partApplication);
        var part_id = partApplication.turbo.id;
        alert('part_id: ' + part_id);
        var application_id = partApplication.carModelEngineYear.id;
        alert('application_id: ' + application_id);
        restService.removePartApplication(part_id, application_id).then(
          function() {
            applications.splice(idx, 1);
          },
          restService.error
        );
      }
    }
  }]);


