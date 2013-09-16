'use strict';

angular.module('ngMetaCrudApp')
    .factory('searchService', function ($http) {
        return function (queryString, facetFilters) {
            console.log("Searching for `" + queryString + "`, facets: " + JSON.stringify(facetFilters));

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
                    "must": []
                }
            };

            // Facet Terms
            if (angular.isObject(facetFilters) && !jQuery.isEmptyObject(facetFilters)) {
                angular.forEach(facetFilters, function (facetValue, facetName) {
                    var term = {};
                    term[facetName] = facetValue;

                    searchRequest.query.bool.must.push({"term": term});
                });
            }

            // Query string
            if (angular.isString(queryString) && queryString.length > 0) {
                searchRequest.query.bool.must.push({
                    query_string: {
                        query: queryString,
                        fields: [
                            "_id^1000",
                            "part_type^1000",
                            "manufacturer_part_number.autocomplete",
                            "manufacturer_part_number.text",
                            "manufacturer_name.autocomplete",
                            "manufacturer_name.text",
                            "name"
                        ]
                    }
                });
            }

            // Default query
            if (searchRequest.query.bool.must.length == 0) {
                searchRequest.query = {match_all: {}};
            }

            // Call to ElasticSearch
            return $http({
                method: 'POST',
                url: "https://api.searchbox.io/api-key/xmqnaud6grtokcswjvna7nzvwukk6ont/metadata/_search",
                data: searchRequest
            });
        };
    });
