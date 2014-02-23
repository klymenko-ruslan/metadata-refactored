'use strict';

angular.module('ngMetaCrudApp')
    .factory('searchService', function ($http, $log) {
        return function (search) {
//        console.log("Searching for `" + search.queryString + "`, facets: " + JSON.stringify(search.facetFilters));

            var searchRequest = {
                from: search.count * (search.page - 1),
                size: search.count,
                facets: {
                    "Kit Type": {
                        terms: {
                            field: "kitType.name",
                            order: "term",
                            size: 25
                        }
                    },
                    "Gasket Type": {
                        terms: {
                            field: "gasketType.name",
                            order: "term",
                            size: 25
                        }
                    },
                    "Seal Type": {
                        terms: {
                            field: "sealType.name",
                            order: "term",
                            size: 25
                        }
                    }
                }
            };

            // Build the query
            searchRequest.query = {
                "bool": {
                    "must": []
                }
            };

            // Part type
            if (search.partType) {
//          $log.log("Part Type: " + search.partType.name);
                searchRequest.query.bool.must.push({
                    term: {
                        'part.partType.id': search.partType.id
                    }
                });
            }

            // Manufacturer
            if (search.manufacturer) {
//          $log.log("Manufacturer: " + search.manufacturer.name);
                searchRequest.query.bool.must.push({
                    term: {
                        'part.manufacturer.id': search.manufacturer.id
                    }
                });
            }
            // Kit type
            if (search.kitType) {
//          $log.log("Manufacturer: " + search.manufacturer.name);
                searchRequest.query.bool.must.push({
                    term: {
                        'part.kitType.id': search.kitType.id
                    }
                });
            }
            // Gasket Type
            if (search.gasketType) {
//          $log.log("Manufacturer: " + search.manufacturer.name);
                searchRequest.query.bool.must.push({
                    term: {
                        'part.gasketType.id': search.gasketType.id
                    }
                });
            }
            // Seal Type
            if (search.sealType) {
//          $log.log("Manufacturer: " + search.manufacturer.name);
                searchRequest.query.bool.must.push({
                    term: {
                        'part.sealType.id': search.sealType.id
                    }
                });
            }
            // Coolant Type
            if (search.coolantType) {
//          $log.log("Manufacturer: " + search.manufacturer.name);
                searchRequest.query.bool.must.push({
                    term: {
                        'part.coolantType.id': search.coolantType.id
                    }
                });
            }
//            // Turbo type
//            if (search.turboType) {
////          $log.log("Manufacturer: " + search.manufacturer.name);
//                searchRequest.query.bool.must.push({
//                    term: {
//                        'part.turboType.id': search.turboType.id
//                    }
//                });
//            }
//            // Turbo model
//            if (search.turboModel) {
////          $log.log("Manufacturer: " + search.manufacturer.name);
//                searchRequest.query.bool.must.push({
//                    term: {
//                        'part.turboModel.id': search.turboModel.id
//                    }
//                });
//            }

            // Facet Terms
            if (angular.isObject(search.facetFilters) && !jQuery.isEmptyObject(search.facetFilters)) {
                angular.forEach(search.facetFilters, function (facetValue, facetName) {
                    var term = {};
                    term[facetName] = facetValue;
                    searchRequest.query.bool.must.push({"term": term});
                });
            }

            // Query string
            if (angular.isString(search.queryString) && search.queryString.length > 0) {
                searchRequest.query.bool.must.push({
                    query_string: {
                        query: search.queryString,
                        fields: [
                            "manufacturerPartNumber.autocomplete",
                            "manufacturerPartNumber.text"
                        ]
                    }
                });
            }

            // Default query
            if (searchRequest.query.bool.must.length == 0) {
                searchRequest.query = {match_all: {}};
            }

            // Sorting
            searchRequest.sort = [];
            if (search.sorting) {
                angular.forEach(search.sorting, function (order, field) {

                    // Build the sort field
                    var sortField = {};
                    sortField[field] = {
                        "missing": "_last",
                        "ignore_unmapped": true,
                        "order": order
                    }

//                    searchRequest.sort.push(sortField);
                });
            }

        // Call to ElasticSearch
        return $http({
          method: 'POST',
          headers: {
            "Content-type": "text/plain"
          },
          url: "/search",
          data: searchRequest
        });
      };
    });
