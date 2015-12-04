'use strict';

angular.module('ngMetaCrudApp')
  .service('BOM', function BOM(Restangular) {
      
    this.listByParentPartId = function(parentPartId) {
        return Restangular.one("bom/byParentPart", parentPartId).get();
    }
    
    this.getById = function(bomItemId) {
        return Restangular.one("bom", bomItemId).get();
    }
    
    return this;
  });
