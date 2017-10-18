'use strict';

/**
 * A lazy loading cache for rarely changed in a storage app dictionaries.
 */

angular.module('ngMetaCrudApp')
  .service('cachedDictionaries', ['$q', 'restService', function ($q, restService) {

    this._partTypes = null;
    this._manufacturers = null;
    this._services = null;
    this._criticalDimensionsEnumsVals = null;
    this._criticalDimensions = null;

    // Load all dictionaries.
    this.load = function() {
      this._partTypes = this.getPartTypes();
      this._manufacturers = this.getManufacturers();
      this._services = this.getServices();
      this._criticalDimensions = this.getCriticalDimensions();
      this._criticalDimensionsEnumsVals = this.getCriticalDimensionsEnumsVals();
    };

    // Reset all previosly loaded data.
    this.reset = function() {
      this._partTypes = null;
      this._manufacturers = null;
      this._services = null;
      this._criticalDimensions = null;
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

    this.getCriticalDimensions = function() {
      if (this._criticalDimensions === null) {
        this._criticalDimensions = restService.getCritDimsByPartTypes();
      }
      return this._criticalDimensions;
    };

    this.getCriticalDimensionsForPartId = function(id) {
      if (typeof(id) === 'number') {
        id = id.toString();
      }
      var deferred = $q.defer();
      this.getCriticalDimensions().then(function(cdms) {
        var cd = cdms[id];
        deferred.resolve();
      });
      return deferred.promise;
      /*
      return $q(function(resolve, reject) {
        this.getCriticalDimensions().then(function(cdms) {
          resolve(cdms[id]);
        });
      });
      */
    };

    this.getCriticalDimensionsEnumsVals = function() {
      if (this._criticalDimensionsEnumsVals === null) {
        this._criticalDimensionsEnumsVals = restService.getAllCritDimEnumVals();
      }
      return this._criticalDimensionsEnumsVals;
    };

  }]);
