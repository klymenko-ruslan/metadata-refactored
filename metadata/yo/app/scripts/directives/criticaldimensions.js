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
            )
            .map($scope._toDisplayObject)
            .filter(
              // Filter hide/show blank.
              function(dto) {
                return (!$scope.opts.hideBlank || $scope.opts.hideBlank && dto.displayValue) && (!normalizedFilter || normalizedFilter && (
                  dto.filterName.indexOf(normalizedFilter) > -1 ||
                  dto.filterValue && dto.filterValue.indexOf(normalizedFilter) > -1
                ));
              }
            );
        };

        $scope._unit2displaystr = function(d) {
          var u = d.unit;
          var retVal = u;
          if (retVal === undefined || retVal === null) {
            retVal = "";
          } else if (u === "INCHES") {
            retVal = String.fromCharCode(0x2033);
          } else if (u === "DEGREES") {
            retVal = String.fromCharCode(0x00B0);
          } else if (u === "GRAMS") {
            retVal = " g.";
          }
          return retVal;
        };

        // Get formatted critical dimension value that is ready for displaying on the UI.
        $scope._getDisplayVal = function(descriptor, value, displayUnit) {
          var retVal;
          if (value === undefined || value === null) {
            retVal = "";
          } else if (descriptor.dataType === "DECIMAL") {
            retVal = $filter("number")(value, descriptor.scale);
          } else if (descriptor.dataType === "ENUMERATION") {
            var valEnum = $scope.part[descriptor.jsonEnum];
            if (valEnum !== undefined) {
              // TODO
              var selected = _.find(valEnum, function(ve) {
                return ve.value == value;
              });
              if (selected !== undefined) {
                retVal = selected.name;
              } else {
                retVal = "";
              }
            } else {
              $log.log("Definition of an enum not found: " + descriptor.jsonEnum);
              retVal = String(value);
            }
          } else {
            retVal = String(value);
          }
          if (retVal !== "" && displayUnit) {
            retVal += displayUnit;
          }
          return retVal;
        };

        // Calculate and add property 'displayValue' to the passed 'display object'.
        $scope._addDisplayValue = function(dispObj) {
          // Display value.
          var displayValue = $scope._getDisplayVal(dispObj.valueDescriptor, dispObj.value, dispObj.displayUnit);
          if (dispObj.toleranceJsonName) {
            // If the dispOject has a tolerance.
            displayValue += (" " + String.fromCharCode(0x00B1) + " " + $scope._getDisplayVal(dispObj.toleranceDescriptor, dispObj.tolerance, dispObj.toleranceDisplayUnit));
          }
          dispObj.displayValue = displayValue;
          dispObj.filterValue = displayValue.toLowerCase();
        };

        // This method build a JS object that represents a row in the UI table.
        // The object contains values for displayed "name" and "value" fields
        // as well as auxiliary properties (objects) for the row editing.
        // A returning object has following mondatory properties:
        //  * jsonName
        //  * displayName
        //  * filterName
        //  * displayUnit
        //  * value
        //  * filterValue
        //  * valueDescriptor
        //  * displayValue
        // In a case of the "inline layout" the object also has following extra properties:
        //  * toleranceJsonName
        //  * tolerance
        //  * toleranceDescriptor
        //  * toleranceDisplayUnit
        $scope._toDisplayObject = function(d) {
          var retVal = {};
          retVal.jsonName = d.jsonName;
          retVal.displayName = d.name;
          retVal.filterName = retVal.displayName.toLowerCase();
          retVal.displayUnit = $scope._unit2displaystr(d);
          var val = $scope.part[d.jsonName];
          retVal.value = val;
          retVal.valueDescriptor = d;
          // Add properties for tolerance if any.
          if ($scope.opts.inlineLayout && d.tolerance === false) { // nominal value
            // This is a special case when we should display nominal and tolerance values in the "inline layout" .

            // Try to find a descriptor for a tolerance and a tolerance.
            // In case of success use the descriptor to format the value and add
            // the formatted value to the nominal value to display.
            var toleranceJsonName = d.jsonName + "Tol";
            var toleranceDesc = $scope.idxDescriptorsByJsonName[toleranceJsonName];
            if (toleranceDesc && toleranceDesc.tolerance === true) { // if this is a real tolerance
              var tolerance = $scope.part[toleranceDesc.jsonName];
              if (tolerance !== undefined) {
                retVal.toleranceJsonName = toleranceDesc.jsonName;
                retVal.tolerance = tolerance;
                retVal.toleranceDisplayValue = $filter("number")(tolerance, toleranceDesc.scale);
                retVal.toleranceDisplayUnit = $scope._unit2displaystr(toleranceDesc);
                retVal.toleranceDescriptor = toleranceDesc;
              }
            }
          }
          $scope._addDisplayValue(retVal);
          return retVal;
        }; // $scope._getDisplayName(d);

      }]
    };
  }]);
