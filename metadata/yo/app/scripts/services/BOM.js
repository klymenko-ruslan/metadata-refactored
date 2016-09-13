"use strict";

angular.module("ngMetaCrudApp")
  .service("BOM", function BOM(Restangular) {

    /**
     * BOMs for the part.
     */
    this.listByParentPartId = function(parentPartId) {
      return Restangular.one("bom/byParentPart", parentPartId).get();
    };

    /**
     * BOMs of specified types for the part.
     */
    this.listByParentPartAndTypeIds = function(parentPartId, typeId) {
      return Restangular.one("bom/byParentPart", parentPartId).getList("type", typeId);
    };

    this.listParentsOfPartBom = function(partId) {
      return Restangular.one("bom/part", partId).getList("parents");
    };

    this.getById = function(bomItemId) {
      return Restangular.one("bom", bomItemId).get();
    };

    this.removeBOM = function(bomId) {
      return Restangular.one("bom", bomId).remove();
    };

    this.addToParentsBOMs = function(partId, request) {
      return Restangular.one("bom/part", partId).post("parents", request);
    };

    return this;
  });
