'use strict';

angular.module('ngMetaCrudApp')
    .service('restService', function RestService(Restangular, $dialogs) {
        return new function () {  // jshint ignore:line

            this.error = function(title, response) {
              $dialogs.error(
                  title, 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
            };

            this.findPart = function (id, params) {
                return Restangular.one('part', id).get(params);
              };

            this.createPart = function (part) {
                return Restangular.post(part);
              };

            this.updatePart = function (part) {
                return Restangular.put(part);
              };

            this.deletePart = function (part) {
                return Restangular.remove(part);
              };

            this.listManufacturers = function (first, count) {
                return Restangular.all('other/manufacturer', {first: first, count: count}).getList();
              };

            this.listPartTypes = function () {
                return Restangular.all('type/part').getList();
              };

            this.findManufacturer = function (id) {
                return Restangular.one('manufacturer', id).get();
              };

            this.createManufacturer = function (manufacturer) {
                return Restangular.post(manufacturer);
              };

            this.updateManufacturer = function (manufacturer) {
                return Restangular.put(manufacturer);
              };

            this.deleteManufacturer = function (manufacturer) {
                return Restangular.remove(manufacturer);
              };

            this.listTurboTypesForManufacturerId = function (manufacturerId) {
                return Restangular.all('other/turboType').getList({'manufacturerId': manufacturerId});
              };

            this.listTurboModelsForTurboTypeId = function (turboTypeId) {
                return Restangular.all('other/turboModel').getList({'turboTypeId': turboTypeId});
              };
          };
      });
