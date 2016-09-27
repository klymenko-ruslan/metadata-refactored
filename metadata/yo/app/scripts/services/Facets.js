"use strict";

angular.module("ngMetaCrudApp")
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
