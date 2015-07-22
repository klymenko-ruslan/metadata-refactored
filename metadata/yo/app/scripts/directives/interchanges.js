'use strict';

angular.module('ngMetaCrudApp')
  .directive('interchanges', function ($log, Restangular) {
    return {
      scope: {
          interchangeId: "=",
          parentPartId: "="
      },
      template: '<part-table parts="interchange.parts"></part-table>',
      restrict: 'E',
      link: function postLink(scope, element, attrs) {
          scope.$watch("interchangeId", function(interchangeId) {
            if (interchangeId === undefined) {
                return;
            }
              
            if (_.isObject(scope.interchange) && interchangeId === scope.interchange.id) {
                return;
            }
            
            $log.log("Loading parts for interchange ", interchangeId);
            Restangular.one("interchange", interchangeId).get().then(function(interchange) {
                // Remove the parent part
                var idx = _.findIndex(interchange.parts, function(part) {
                    return part.id = scope.parentPartId;
                });

                if (idx > -1) {
                    interchange.parts.splice(idx, 1);
                }

                scope.interchange = interchange;
            });
        });
      }
    };
  });
