'use strict';

angular.module('ngMetaCrudApp')
    .factory('loginRequiredInterceptor', function($location, $q) {
      return {
        response: function(response) {
          return response;
        },
        responseError: function(response) {
          if (response.status === 401 || response.status === 403) {
            $location.path('/');
            return $q.reject(response);
          } else {
            return $q.reject(response);
          }
        }
      };
    });
