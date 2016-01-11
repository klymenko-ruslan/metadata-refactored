"use strict";

angular.module("ngMetaCrudApp")
  .directive("interchangeableParts", ["restService", "ngTableParams", function(restService, ngTableParams) {
    return {
      restrict: 'E',
      scope: {
        part: "="
      },
      templateUrl: '/views/component/interchangeable_parts.html',
      controller: function($scope) {
        restService.findInterchange($scope.part.interchange.id).then(
          function(interchange) {
            // Remove the parent part.
            var idx = _.findIndex(interchange.parts, function(part) {
              return part.id == $scope.part.id;
            });
            if (idx > -1) {
              interchange.parts.splice(idx, 1);
            }
            $scope.interchangeablePartsTableParams = new ngTableParams({
              page: 1,
              count: 10
            }, {
              getData: function($defer, params) {
                params.total(interchange.parts.length);
                var recs = interchange.parts.slice((params.page() - 1) * params.count(), params.page() * params.count());
                $defer.resolve(recs);
              }
            });
          },
          function(error) {
            restService.error("Can't load interchangeable parts.", error);
          }
        );
      }
    };
  }]);
