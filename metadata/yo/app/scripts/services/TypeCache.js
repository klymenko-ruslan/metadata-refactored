'use strict';

angular.module('ngMetaCrudApp')
    .service('TypeCache', function ($log, restService) {
        var TypeCache = {};

        var partTypes = [];
        TypeCache.partTypes = function () {

            // Fetch the part types
            if (partTypes.length == 0) {
                var partTypesPromise = restService.listPartTypes();
                partTypesPromise.then(
                    function (newPartTypes) {
                        angular.copy(newPartTypes, partTypes);
                    },
                    function (status, data) {
                        $log.log("Could not get part types.", status);
                    });
            }

            return partTypes;
        }

        return TypeCache;
    });
