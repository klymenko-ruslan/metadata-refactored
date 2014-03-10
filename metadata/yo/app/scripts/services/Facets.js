'use strict';

angular.module('ngMetaCrudApp')
  .constant('Facets', [
  {
    name: 'Part Type',
    field: 'partType.name'
  },
  {
    name: 'Manufacturer',
    field: 'manufacturer.name'
  },
  {
    name: 'Kit Type',
    field: 'kitType.name'
  },
  {
    name: 'Gasket Type',
    field: 'gasketType.name'
  },
  {
    name: 'Seal Type',
    field: 'sealType.name'
  },
  {
    name: 'Coolant Type',
    field: 'coolType.name'
  },
  {
    name: 'Turbo Type',
    field: 'turboModel.turboType.name'
  },
  {
    name: 'Turbo Model',
    field: 'turboModel.name'
  }
]);
