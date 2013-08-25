'use strict';

angular.module('ngSearchFacetsApp')
  .factory('facetNameService', function () {
      return function (facetName) {
          switch (facetName) {
              case 'manufacturer_name.text':
                  return "Manufacturer";
              case 'manufacturer_type_name.text':
                  return "Manufacturer Type";
              case 'gasket_type_name.text':
                  return "Gasket Type";
              default:
                  return facetName;
          }
      }
  });
