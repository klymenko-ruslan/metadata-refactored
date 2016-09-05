"use strict";

angular.module("ngMetaCrudApp")
  .constant("partFacets", [{
    id: "fct_part_type",
    name: "Part Type",
    field: "partType.name"
  }, {
    id: "fct_manufacturer",
    name: "Manufacturer",
    field: "manufacturer.name"
  }, {
    id: "fct_kittype",
    name: "Kit Type",
    field: "kitType.name"
  }, {
    id: "fct_gaskettype",
    name: "Gasket Type",
    field: "gasketType.name"
  }, {
    id: "fct_sealtype",
    name: "Seal Type",
    field: "sealType.name"
  }, {
    id: "fct_coolanttype",
    name: "Coolant Type",
    field: "coolType.name"
  }, {
    id: "fct_turbotype",
    name: "Turbo Type",
    field: "turboModel.turboType.name"
  }, {
    id: "fct_turbomodel",
    name: "Turbo Model",
    field: "turboModel.name"
  }])
  .constant("cmeyFacets", [{
    id: "fct_year",
    name: "Year",
    field: "year.name"
  }, {
    id: "fct_make",
    name: "Make",
    field: "model.make.name"
  }, {
    id: "fct_model",
    name: "Model",
    field: "model.name"
  }, {
    id: "fct_engine",
    name: "Engine",
    field: "engine.engineSize"
  }, {
    id: "fct_fueltype",
    name: "Fuel Type",
    field: "engine.fuelType.name"
  }])
  .constant("carmodelFacets", [{
    id: "fct_make",
    name: "Make",
    field: "make.name"
  }])
  .constant("carengineFacets", [{
    id: "fct_fueltype",
    name: "Fuel Type",
    field: "fuelType.name"
  }]);
