'use strict';

angular.module('ngMetaCrudApp')
    .service('PartTypes', function PartTypes($log, $rootScope, Restangular) {
        var PartTypes = this;

        /**
         * The array of part types.
         * @type {Array}
         */
        this.list = null;

        /**
         * Fetches the part types from the server.
         * @returns {Promise|*}
         */
        this.refreshPromise = null;
        this.refresh = function () {
            $log.log("PartTypes.refresh");

            // Out with the old
            this.list = null;

            // In with the new
            return PartTypes.refreshPromise = Restangular.all("type/part").getList()
                .then(
                function (newPartTypes) {
                    PartTypes.list = newPartTypes;
                },
                function (status, data) {
                    $log.log("Could not get part types.", status);
                })
                .finally(function () {
                    PartTypes.refreshPromise = null;
                });
        };

        /**
         * Gets a part type by it's ID.
         * @param partTypeId the part type ID.
         * @returns {Object|promise} the part type object
         */
        this.getById = function (partTypeId) {
            var partType = _.find(this.list, function (partType) {
                return partTypeId == partType.id;
            });

            if (!angular.isObject(partType)) {
                return Restangular.all("type/part").one(partTypeId).get();
            }
        };

        /**
         * Gets a part type by it's Java class name.
         * @param partTypeClassName the Java class name.
         * @returns {Object} the part type object
         */
        this.getByClassName = function (partTypeClassName) {
            return _.find(this.list, function (partType) {
                return partTypeClassName == partType.typeName;
            });
        };

        // Load the part types
//      this.refresh();

        return this;
    });
