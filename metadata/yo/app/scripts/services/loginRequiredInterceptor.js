'use strict';

angular.module('ngMetaCrudApp')

.factory('loginRequiredInterceptor', ['$q', '$location',
  function($q, $location) {
    return {
      response: function(response) {
//console.log('DBG: response: ' + angular.toJson(response, 2));
        return response;
      },
      responseError: function(response) {
//console.log('DBG: responseError: ' + angular.toJson(response, 2));
        if (response.status === 401 || response.status === 403) {
//console.log('DBG: status: ' + response.status);
          $location.path('/');
        }
        return $q.reject(response);
      }
    };
  }
]);
