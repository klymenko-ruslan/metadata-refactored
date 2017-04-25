"use strict";

angular.module("ngMetaCrudApp")
    .directive("cmeyName", function () {
      return {
        scope: {
          "cmeyName": "=",
          "cmeyNamePrefix": "@",
          "cmeyNameSuffix": "@"
        },
        replace: false,
        restrict: "A",
        template: "{{cmeyNamePrefix}}"
                + " [{{cmeyName.id}}]"
                + " {{cmeyName.model.name}}, "
                + " {{cmeyName.engine.engineSize}}"
                + " {{cmeyName.engine.fuelType.name}}"
                + " {{cmeyName.year.name}}"
                + " {{cmeyNameSuffix}}"
      }
    });
