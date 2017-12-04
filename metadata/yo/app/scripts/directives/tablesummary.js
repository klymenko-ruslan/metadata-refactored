'use strict';

angular.module('ngMetaCrudApp').directive('tableSummary', [
  function () {
    return {
      scope: {
        rowscount: '='
      },
      templateUrl: '/views/component/tablesummary.html',
      restrict: 'E'
    };
  }
]);

