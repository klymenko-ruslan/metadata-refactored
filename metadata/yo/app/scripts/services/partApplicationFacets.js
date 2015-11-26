'use strict';

angular.module('ngMetaCrudApp')
  .constant('partApplicationFacets', [
  {
    name: 'Year',
    field: 'year.name'
  },
  {
    name: 'Make',
    field: 'model.make.name'
  },
  {
    name: 'Model',
    field: 'model.name'
  },
  {
    name: 'Engine',
    field: 'engine.engineSize'
  },
  {
    name: 'Fuel Type',
    field: 'engine.fuelType.name'
  }
]);
