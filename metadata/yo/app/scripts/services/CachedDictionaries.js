'use strict';

/**
 * A lazy loading cache for rarely changed in a storage app dictionaries.
 */

angular.module('ngMetaCrudApp')
  .service('cachedDictionaries', ['restService', function (restService) {

    this._partTypes = null;
    this._manufacturers = null;
    this._services = null;
    this._criticalDimensionsEnumsVals = null;
    /*
     * Two members below contain almost the same structure -- map(partTypeId => criticalDimensions).
     * But the firs member '_criticalDimensions' contains mapping for all part types
     * while the second one '_criticalDimensionsByPartId' is filled by requests.
     * So method 'getCriticalDimensionsForPartId()' below has clever logic
     * which try to avoid any requests to the REST service (see below).
     * Also when we load a value to the '_criticalDimensions' we in the same
     * time resets values in the '_criticalDimensionsByPartId' as unneeded
     * (see method 'getCriticalDimensions' below).
     */
    this._criticalDimensions = null;
    this._criticalDimensionsByPartId = {};

    // Reset all previosly loaded data.
    this.reset = function() {
      this._partTypes = null;
      this._manufacturers = null;
      this._services = null;
      this._criticalDimensions = null;
      this._criticalDimensionsByPartId = {};
      this._criticalDimensionsEnumsVals = null;
    };

    this.getPartTypes = function() {
      if (this._partTypes === null) {
        this._partTypes = restService.listPartTypes();
      }
      return this._partTypes;
    };

    this.getManufacturers = function() {
      if (this._manufacturers === null) {
        this._manufacturers = restService.listManufacturers();
      }
      return this._manufacturers;
    };

    this.getServices = function() {
      if (this._services === null) {
        this._services = restService.getAllServices();
      }
      return this._services;
    };

    this.getCriticalDimensionsForPartId = function(id) {
      var retVal;
      if (typeof(id) === 'string') {
        id = parseInt(id);
      }
      if (this._criticalDimensions !== null) {
        retVal = this._criticalDimensions[id];
      } else {
        retVal = this._criticalDimensionsByPartId[id];
        if (retVal === undefined) {
          retVal = restService.findCriticalDimensionsForThePart(id);
          this._criticalDimensionsByPartId[id] = retVal;
        }
      }
      return retVal;
    };

    this.getCriticalDimensionsEnumsVals = function() {
      if (this._criticalDimensionsEnumsVals === null) {
        this._criticalDimensionsEnumsVals = restService.getAllCritDimEnumVals();
      }
      return this._criticalDimensionsEnumsVals;
    };

    this.getCriticalDimensions = function() {
      if (this._criticalDimensions === null) {
        this._criticalDimensions = restService.getCritDimsByPartTypes();
        this._criticalDimensionsByPartId = {};
      }
      return this._criticalDimensions;
    };

  }]);
