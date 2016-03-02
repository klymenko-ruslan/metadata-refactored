"use strict";

angular.module("ngMetaCrudApp")
  .directive("interchangeableParts", ["$log", "utils", "restService", "ngTableParams",
    function($log, utils, restService, ngTableParams) {
      return {
        restrict: 'E',
        scope: {
          part: "="
        },
        templateUrl: '/views/component/interchangeable_parts.html',
        controller: ["$scope", "$parse", function($scope, $parse) {
          $scope.$watch("part", function(newVal, oldVal) {
            if (!angular.isObject(newVal) && angular.equals(newVal, oldVal)) {
              return;
            }
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
                  getData: utils.localPagination($scope.interchange.parts, "manufacturer.name")
                });
              },
              function(error) {
                restService.error("Can't load interchangeable parts.", error);
              }
            );
          });
        }]
      };
    }
  ]);
