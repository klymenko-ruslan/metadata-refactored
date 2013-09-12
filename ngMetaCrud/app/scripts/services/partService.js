'use strict';

angular.module('ngMetaCrudApp')
  .service('partService', function PartService(Restangular) {
        return new function() {
            this.findPart = function(id) {
                return Restangular.one("part", id).get();
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
        };
  });
