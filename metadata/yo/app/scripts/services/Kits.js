'use strict';

angular.module('ngMetaCrudApp')
  .service('Kits', function Kits(Restangular) {
      this.listComponents = function(kitId) {
        return Restangular.one('kit/' + kitId, 'component').get();  
      };
    return this;
  });
