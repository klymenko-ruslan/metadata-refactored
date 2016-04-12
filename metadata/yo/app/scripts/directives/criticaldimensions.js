"use strict";

angular.module("ngMetaCrudApp")
    .directive("criticalDimensions", ["$log", "$filter", function($log, $filter) {
      return {
        restrict: "E",
        replace: false,
        transclude: true,
        templateUrl: "/views/component/criticaldimensions.html",
        //templateUrl: "criticaldimensions.html",
        scope: {
          part: "=",
          descriptors: "="
        },
        controller: ["$scope", "$log", "Restangular", "gToast", "restService", function($scope, $log, Restangular, gToast, restService) {

          var ERROR_MSG_PREFIX = "Error: ";

          $scope._buildErrorMessage = function(msg) {
            return ERROR_MSG_PREFIX + msg;
          };

          $scope._isErrorMessage = function(displayVal) {
            return displayVal !== undefined && displayVal !== null && displayVal.startsWith(ERROR_MSG_PREFIX);
          };

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
          // If for some reason the value can't be displayed on the web correctly
          // then this method returns error message.
          // For null or undefined values cheks of the value are not done,
          // so it is possible that errors were not detected.
          // Such situations are processed in the code "in-place".
          $scope._getDisplayVal = function(descriptor, value, displayUnit) {
            var retVal;
            if (value === undefined || value === null) {
              retVal = descriptor.nullDisplay || "";
            } else if (descriptor.dataType === "DECIMAL") {
              retVal = $filter("number")(value, descriptor.scale);
            } else if (descriptor.dataType === "ENUMERATION") {
              var valEnum = $scope.part[descriptor.jsonEnum];
              if (valEnum !== undefined) {
                var selected = _.find(valEnum, function(ve) {
                  return ve.value == value;
                });
                if (selected !== undefined) {
                  retVal = selected.name;
                } else {
                  retVal = $scope._buildErrorMessage("value '" + value + "' not found in the enumeration '" + descriptor.jsonEnum + "'.");
                }
              } else {
                retVal = $scope._buildErrorMessage("definition of the enum '" + descriptor.jsonEnum + "' not found.");
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
            if ($scope._isErrorMessage(displayValue)) {
              dispObj.invalidDisplayValue = true;
            }
            if (!dispObj.invalidDisplayValue && dispObj.toleranceJsonName) {
              // If the dispOject has a tolerance.
              var toleranceDisplayValue = $scope._getDisplayVal(dispObj.toleranceDescriptor, dispObj.tolerance, dispObj.toleranceDisplayUnit);
              displayValue += (" " + String.fromCharCode(0x00B1) + " " + toleranceDisplayValue);
              if ($scope._isErrorMessage(displayValue)) {
                dispObj.invalidDisplayValue = true;
              }
            }
            dispObj.displayValue = displayValue;
            dispObj.filterValue = displayValue.toLowerCase();
          };

          // This method build a JS object that represents a row in the UI table.
          // The object contains values for displayed "name" and "value" fields
          // as well as auxiliary properties (objects) for the row editing.
          // A returning object has following mondatory properties:
          //  * id
          //  * jsonName
          //  * displayName
          //  * filterName
          //  * displayUnit
          //  * value
          //  * filterValue
          //  * valueDescriptor
          //  * displayValue
          //  * selectOptions         (optional)
          //  * nullAllowed
          //  * nullDisplay
          // In a case of the "inline layout" the object also has following extra properties:
          //  * toleranceJsonName     (optional)
          //  * tolerance             (optional)
          //  * toleranceDescriptor   (optional)
          //  * toleranceDisplayUnit  (optional)
          //  If a value of a critical dimesions, for some reason, can't be correctly displayed on the UI then following property is added:
          //  * invalidDisplayValue   (optional)
          $scope._toDisplayObject = function(d) {
            var retVal = {};
            retVal.id = d.id;
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
            if (!retVal.invalidDisplayValue && d.dataType === "ENUMERATION") {
              // It is possible that property 'invalidDisplayValue' was not set in the '_getDisplayVal'
              // even when enumeration is not defined (see comments for the method '_getDisplayVal' for details).
              // So we do check too for existence of the enumeration here.
              var enumVals = angular.copy($scope.part[d.jsonEnum]);
              if (enumVals) {
                retVal.selectOptions = enumVals;
                retVal.nullAllowed = d.nullAllowed;
                retVal.nullDisplay = d.nullDisplay;
              } else {
                retVal.displayValue = $scope._buildErrorMessage("definition of the enum '" + d.jsonEnum + "' not found.");
                retVal.invalidDisplayValue = true;
              }
            }
            return retVal;
          }; // $scope._getDisplayObject(d);

          $scope.editedDispObj = null; // critical dimension that is modfying (but modified object is $scope.part)
          $scope.editedPart = null;

          $scope.modifyStart = function(dispObj) {
            $scope.editedPart = Restangular.copy($scope.part);
            $scope.editedDispObj = dispObj;
          };

          $scope.isEditing = function() {
            return $scope.editedDispObj !== null;
          };

          $scope.isModifying = function(d) {
            return $scope.editedDispObj !== null && $scope.editedDispObj.id === d.id;
          };

          $scope.modifySave = function() {
            restService.updatePart($scope.editedPart).then(
              function success(updatedPart) {
                gToast.open("The part has been successfully updated.");
                $scope.part = updatedPart;
                $scope.modifyCancel();
                $scope._copyDescriptorsToDisplay(); // redraw with updated values
              },
              function failure(response) {
                restService.error("Updating of the part failed.", response);
              }
            );
          };

          $scope.modifyUndo = function(formController) {
            $scope.editedPart = Restangular.copy($scope.part);
            formController.$setPristine();
          };

          $scope.modifyCancel = function() {
            $scope.editedDispObj = null;
            $scope.editedPart = null;
          };

        }]
      };
    }]);
