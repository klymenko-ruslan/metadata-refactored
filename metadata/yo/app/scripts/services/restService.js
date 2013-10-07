'use strict';

angular.module('ngMetaCrudApp')
  .service('restService', function RestService(Restangular) {
        return new function() {

            this.findPart = function(id, params) {
                return Restangular.one("part", id).get(params);
            }

            this.createPart = function(part) {
                return Restangular.post(part);
            }

            this.updatePart = function(part) {
                return Restangular.put(part);
            }

            this.deletePart = function(part) {
                return Restangular.remove(part);
            }

            this.listManufacturers = function(first, count) {
                return Restangular.all("other/manufacturer", {first: first, count: count}).getList();
            }

            this.findManufacturer = function(id) {
                return Restangular.one("manufacturer", id).get();
            }

            this.createManufacturer = function(manufacturer) {
                return Restangular.post(manufacturer);
            }

            this.updateManufacturer = function(manufacturer) {
                return Restangular.put(manufacturer);
            }

            this.deleteManufacturer = function(manufacturer) {
                return Restangular.remove(manufacturer);
            }

            this.listTurboTypesForManufacturerId = function(manufacturerId) {
                return Restangular.all("other/turboType").getList({"manufacturerId": manufacturerId});
            }

            this.listTurboModelsForTurboTypeId = function(turboTypeId) {
                return Restangular.all("other/turboModel").getList({"turboTypeId": turboTypeId});
            }
        };
  });
