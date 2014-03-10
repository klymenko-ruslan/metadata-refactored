'use strict';

angular.module('ngMetaCrudApp')
    .directive('partName', function () {
      return {
        scope: {
          "partName": '=',
          "partNamePrefix": '@',
          "partNameSuffix": '@'
        },
        replace: false,
        restrict: 'A',
        template: '{{partNamePrefix}}'
                + ' [{{partName.id}}]'
                + ' {{partName.manufacturer.name}}'
                + ' {{partName.partType.name}}'
                + '<span style="white-space: nowrap;">'
                + '  {{partName.manufacturerPartNumber}}'
                + '</span>'
                + ' {{partNameSuffix}}'
      }
    });
