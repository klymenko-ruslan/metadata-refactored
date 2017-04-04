"use strict";

angular.module("ngMetaCrudApp").service("BOM", ["$log", "Restangular", function BOM($log, Restangular) {

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
      return Restangular.one("bom/byParentPart", parentPartId).getList("type", {typeId: typeId});
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

    this.addToParentsBOMs = function(partId, sourcesIds, ratings, description, rows, attachIds) {
      var request = {
        sourcesIds: sourcesIds,
        attachIds: attachIds,
        chlogSrcRatings: ratings,
        chlogSrcLnkDescription: description,
        rows: rows
      };
      return Restangular.one("bom/part", partId).post("parents", request);
    };

    return this;
  }]);
