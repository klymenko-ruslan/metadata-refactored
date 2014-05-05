'use strict';

angular.module('ngMetaCrudApp')
    .service('PartTypes', function PartTypes($log, $rootScope, Restangular) {
        var PartTypes = this; // jshint ignore:line

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
            $log.log('PartTypes.refresh');

            // Out with the old
            this.list = null;

            // In with the new
            PartTypes.refreshPromise = Restangular.all('type/part').getList()
                .then(
                function (newPartTypes) {
                    PartTypes.list = newPartTypes;
                  },
                function (status) {
                    $log.log('Could not get part types.', status);
                  })
                .finally(function () {
                    PartTypes.refreshPromise = null;
                  });
                  
            return PartTypes.refreshPromise;
          };

        /**
         * Gets a part type by it's ID.
         * @param partTypeId the part type ID.
         * @returns {Object|promise} the part type object
         */
        this.getById = function (partTypeId) {
            var partType = _.find(this.list, function (partType) {
                return partTypeId === partType.id;
              });

            if (!angular.isObject(partType)) {
              Restangular.setParentless(false);
              return Restangular.all('type').one('part', partTypeId).get();
            }
          };

        /**
         * Gets a part type by it's Java class name.
         * @param partTypeClassName the Java class name.
         * @returns {Object} the part type object
         */
        this.getByClassName = function (partTypeClassName) {
            return _.find(this.list, function (partType) {
                return partTypeClassName === partType.name;
              }
          );
          };

        // Load the part types
//      this.refresh();

        return this;
      });
