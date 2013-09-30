'use strict';

angular.module('ngMetaCrudApp')
    .factory('searchService', function ($http) {
        return function (search) {
            console.log("Searching for `" + search.queryString + "`, facets: " + JSON.stringify(search.facetFilters));

            var searchRequest = {
                from: search.count * (search.page - 1),
                size: search.count,
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
