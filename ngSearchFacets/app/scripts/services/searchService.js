'use strict';

angular.module('ngSearchFacetsApp')
  .factory('searchService', function ($http) {
     return function (queryString, facetFilters) {
         var queryString = null;
         var searchRequest = {
             from: 0,
             size: 50,
             facets: {
                 "part_type": {
                     terms: {
                         field: "part_type",
                         order: "term"
                     }
                 },
                 "manufacturer_name.text": {
                     terms: {
                         field: "manufacturer_name.text",
                         order: "term"
                     }
                 },
                 "kit_type_name.text": {
                     terms: {
                         field: "kit_type_name.text",
                         order: "term"
                     }
                 },
                 "gasket_type_name.text": {
                     terms: {
                         field: "gasket_type_name.text",
                         order: "term"
                     }
                 }
             }
         };

         // Build the query
         searchRequest.query = {
             "bool": {
                 "must":[]
             }
         };

         // Facet Terms
         if (angular.isObject(facetFilters) && !jQuery.isEmptyObject(facetFilters)) {
             angular.forEach(facetFilters, function(facetValue, facetName) {
                 var term = {};
                 term[facetName] = facetValue;

                 searchRequest.query.bool.must.push({"term":term});
             });
         }

         // Query string
         if (angular.isString(queryString) && queryString.length > 0) {
             searchRequest.query.bool.must.push({
                 query: queryString,
                 fields: [
                     "manufacturer_part_number.autocomplete",
                     "manufacturer_part_number.text",
                     "manufacturer_name.autocomplete",
                     "manufacturer_name.text",
                     "part_type"
                 ]
             });
         }

         // Default query
         if (searchRequest.query.bool.must.length == 0) {
             searchRequest.query = {match_all: {}};
         }
         return $http({
             method: 'POST',
             url: "http://127.0.0.1:9200/metadata/_search",
             data: searchRequest
         });
     };
  });
