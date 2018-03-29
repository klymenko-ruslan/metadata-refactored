'use strict';

angular.module('ngMetaCrudApp')

.controller('GroupsCtrl', function ($log, $scope, restService, dialogs) {
  // Load the groups
  restService.getGroups().then(
    function(groups) {
      $scope.groups = groups;
    },
    function(response) {
      dialogs.error('Could not load group data', 'Server said: <pre>' +
            JSON.stringify(response.data) + '</pre>');
    }
  );
});
