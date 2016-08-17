"use strict";

angular.module("ngMetaCrudApp")
  .service("PartTypes", ["$log", "$rootScope", "restService",
      function PartTypes($log, $rootScope, restService) {
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
    this.refresh = function() {
      $log.log("PartTypes.refresh()");
      // Out with the old
      this.list = null;
      // In with the new
      PartTypes.refreshPromise = restService.listPartTypes().then(
        function(newPartTypes) {
          PartTypes.list = newPartTypes;
        },
        function(status) {
          $log.log("Could not get part types.", status);
        });
      return PartTypes.refreshPromise;
    };
    /**
     * Gets a part type by it's ID.
     * @param partTypeId the part type ID.
     * @returns {Object|promise} the part type object
     */
    this.getById = function(partTypeId) {
      return _.find(PartTypes.list, function(partType) {
        return String(partTypeId) === String(partType.id);
      });
    };
    /**
     * Gets a part type by it's Java class name.
     * @param partTypeClassName the Java class name.
     * @returns {Object} the part type object
     */
    this.getByClassName = function(partTypeClassName) {
      return _.find(PartTypes.list, function(partType) {
        return partTypeClassName === partType.name;
      });
    };

    this.getPromise = function() {
      if (PartTypes.refreshPromise !== null) {
        return PartTypes.refreshPromise;
      }
      return PartTypes.refresh();
    };

    return this;

  }]);
