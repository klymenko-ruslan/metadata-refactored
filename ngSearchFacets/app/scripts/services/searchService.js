'use strict';

angular.module('ngSearchFacetsApp')
  .factory('searchService', function ($http) {
     return function (query, facets) {
         var data = {
             from: 0,
             size: 0,
             query: {
                 query_string: {
                     query: query,
                     fields: [
                         "manufacturer_part_number.autocomplete",
                         "manufacturer_part_number.text",
                         "manufacturer_name.autocomplete",
                         "manufacturer_name.text",
                         "part_type.autocomplete",
                         "part_type.text"
                     ]
                 }
             },
             facets: {
                 "manufacturer_name.text": {
                     terms: {
                         field: "manufacturer_name.text"
                     }
                 },
                 "manufacturer_type_name.text": {
                     terms: {
                         field: "manufacturer_type_name.text"
                     }
                 },
                 "part_type.text": {
                     terms: {
                         field: "part_type.text"
                     }
                 },
                 "gasket_type_name.text": {
                     terms: {
                         field: "gasket_type_name.text"
                     }
                 },
                 "kit_type_name.text": {
                     terms: {
                         field: "kit_type_name.text"
                     }
                 }
             }
         };

         angular.forEach(facets, function(facetValue, facetName) {
             if (typeof data.filter === 'undefined') {
                 data.filter = {};
             }
             if (typeof data.filter.term === 'undefined') {
                 data.filter.term = {};
             }
             data.filter.term[facetName] = facetValue;

             // TODO: Apply filter query to each facet so the counts are accurate, basically fix this part.
             data.facets[facetName].query = {};
             data.facets[facetName].query.term = {};
             data.facets[facetName].query.term[facetName] = facetValue;

             data.facets["manufacturer_name.text"].query = {term: {}};
             data.facets["manufacturer_name.text"].query.term[facetName] = facetValue;
         });

         return $http({
             method: 'POST',
             url: "http://127.0.0.1:9200/metadata/_search",
             data: data
         });
     };
  });
