"use strict";

angular.module("ngMetaCrudApp")
  .constant("partFacets", [{
    name: "Part Type",
    field: "partType.name"
  }, {
    name: "Manufacturer",
    field: "manufacturer.name"
  }, {
    name: "Kit Type",
    field: "kitType.name"
  }, {
    name: "Gasket Type",
    field: "gasketType.name"
  }, {
    name: "Seal Type",
    field: "sealType.name"
  }, {
    name: "Coolant Type",
    field: "coolType.name"
  }, {
    name: "Turbo Type",
    field: "turboModel.turboType.name"
  }, {
    name: "Turbo Model",
    field: "turboModel.name"
  }]).constant("cmeyFacets", [{
    name: "Year",
    field: "year.name"
  }, {
    name: "Make",
    field: "model.make.name"
  }, {
    name: "Model",
    field: "model.name"
  }, {
    name: "Engine",
    field: "engine.engineSize"
  }, {
    name: "Fuel Type",
    field: "engine.fuelType.name"
  }]).constant("carmodelFacets", [{
    name: "Make",
    field: "make.name"
  }]).constant("carengineFacets", [{
    name: "Fuel Type",
    field: "fuelType.name"
  }]);
