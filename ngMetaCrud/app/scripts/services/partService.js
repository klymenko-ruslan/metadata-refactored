'use strict';

angular.module('ngMetaCrudApp')
  .service('PartService', function PartService() {
        var findPart = function(id) {
            return Restangular.one("part", id);
        }

        var createPart = function(part) {
            return Restangular.post(part);
        }

        var updatePart = function(part) {
            return Restangular.put(part);
        }

        var deletePart = function(part) {
            return Restangular.remove(part);
        }
  });
