"use strict";

angular.module("ngMetaCrudApp")
  .directive("criticalDimensions", ["$log", "$filter", function($log, $filter) {
    return {
      restrict: "E",
      replace: false,
      transclude: true,
      templateUrl: "/views/component/criticaldimensions.html",
      scope: {
        part: "=",
        descriptors: "="
      },
      controller: ["$scope", "$log", function($scope, $log) {

        // Index descriptors by name.
        $scope.idxDescriptorsByJsonName = _.indexBy($scope.descriptors, "jsonName");

        // A list which will be actually displayed on the web.
        // See function _copyDescriptorsToDisplay() below to
        // know how this list is filled in.
        $scope.toDisplay = null;

        // Options bar's values.
        $scope.opts = {
          hideBlank: true,
          inlineLayout: true,
          filter: ""
        };

        // Track changes on the options bar.
        $scope.$watchCollection("opts", function(newOpts, oldOpts) {
          $scope._copyDescriptorsToDisplay();
        });

        // This is a heart function of this directive.
        // It filters critical dimensions desctiptors according to
        // option's bar values and transrofms them to
        // a simple DTO object with formatted values prepared to
        // display on a web view.
        $scope._copyDescriptorsToDisplay = function() {
          var normalizedFilter = null;
          if ($scope.opts.filter) {
            normalizedFilter = $scope.opts.filter.toLowerCase();
          }
          $scope.toDisplay = _.filter($scope.descriptors,
            // Filter for inline layout.
            function(d) {
              // For "inline layout" skip desctiptors which are tolerance.
              // In "inline layout" a tolarace is displayed on the line of nominal value.
              return !$scope.opts.inlineLayout || $scope.opts.inlineLayout && !d.tolerance;
            }
          ).map(
            function(d) {
              return {
                displayName: $scope._getDisplayName(d),
                displayValue: $scope._getDisplayValue(d)
              };
            }
          ).filter(
            // Filter hide/show blank.
            function(dto) {
              return (!$scope.opts.hideBlank || $scope.opts.hideBlank && dto.displayValue) && (!normalizedFilter || normalizedFilter && (
                dto.displayName.toLowerCase().indexOf(normalizedFilter) > -1 ||
                dto.displayValue && dto.displayValue.toLowerCase().indexOf(normalizedFilter) > -1
              ));
            }
          );
        };

        $scope._getDisplayName = function(d) {
          var retVal = d.name;
          if (d.unit) {
            retVal += ("; / " +  $scope._unit2displaystr(d.unit) + " /");
          }
          return retVal;
        };

        $scope._unit2displaystr = function(u) {
          var retVal = u;
          if (u === "INCHES") {
            retVal = String.fromCharCode(0x2033);
          } else if (u === "DEGREES") {
            retVal = String.fromCharCode(0x00B0);
          } else if (u === "GRAMS") {
            retVal = "g.";
          }
          return retVal;
        };

        $scope._getDisplayValue = function(d) {
          var retVal = $scope.part[d.jsonName];
          if (retVal === undefined || retVal === null) {
            retVal = "";
          } else if ($scope.opts.inlineLayout && d.tolerance === false) { // nominal value
            // Try to find a descriptor for a tolerance and a tolerance.
            // In case of success use the descriptor to format the value and add
            // the formatted value to the nominal value to display.
            var toleranceJsonName = d.jsonName + "Tol";
            var toleranceDesc = $scope.idxDescriptorsByJsonName[toleranceJsonName];
            if (toleranceDesc && toleranceDesc.tolerance === true) {
              var tolerance = $scope.part[toleranceDesc.jsonName];
              if (tolerance !== undefined) {
                var scale = toleranceDesc.scale;
                if (scale !== undefined) {
                  tolerance = $filter("number")(tolerance, scale);
                }
                retVal = $filter("number")(retVal, d.scale);
                retVal += (" " + String.fromCharCode(0x00B1) + " " + tolerance);
              }
            } else {
              retVal = retVal.toString();
            }
          } else {
            if (d.dataType === "DECIMAL") {
              retVal = $filter("number")(retVal, d.scale);
            } else {
              retVal = retVal.toString();
            }
          }
          return retVal;
        };

      }]
    };
  }]);
