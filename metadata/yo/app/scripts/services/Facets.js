"use strict";

angular.module("ngMetaCrudApp")
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
