'use strict';

angular.module('ngMetaCrudApp')
    .factory('facetNameService', function () {
        return function (facetName) {
            switch (facetName) {
                case 'part_type':
                    return "Part Type";
                case 'kit_type_name.text':
                    return "Kit Type";
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
