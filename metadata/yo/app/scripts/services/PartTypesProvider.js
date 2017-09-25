'use strict';

angular.module('ngMetaCrudApp')
.provider('PartTypes', ['restService', function PartTypes(restService) {

  console.log('Part types provider initialization.');

  var list = restService.listPartTypes();

  return {

    $get: function() {
      return {
        getList: function() {
          return list;
        }
      };
    }

  };

}]);
