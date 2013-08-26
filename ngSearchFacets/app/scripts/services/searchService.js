'use strict';

angular.module('ngSearchFacetsApp')
  .factory('searchService', function ($http) {
     return function (searchRequest) {
         return $http({
             method: 'POST',
             url: "http://127.0.0.1:9200/metadata/_search",
             data: searchRequest
         });
     };
  });
