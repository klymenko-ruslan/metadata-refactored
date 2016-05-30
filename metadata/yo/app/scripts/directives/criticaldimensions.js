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
        controller: ["$scope", "$log", "Restangular", "gToast", "restService", "dialogs",
          function($scope, $log, Restangular, gToast, restService, dialogs) {

          $scope._buildErrorMessage = function(msg) {
            return "Error: " + msg;
          };

          // Index descriptors by name.
          //$scope.idxDescriptorsByJsonName = _.indexBy($scope.descriptors, "jsonName");

          // Make a dictionary: nominal's descriptor ID -> tolerance descriptor.
          $scope.idxDim2Tol = _.chain($scope.descriptors)
            .filter(function(d) {
              // Get only dimensions with parents (in other words -- tolerances).
              return d.parent !== null;
            })
            .indexBy(function(d) {
              // Associate each nominal's descriptor ID with a tolerance descriptor.
              return d.parent.id;
            })
            .value();

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

          // A list which will be actually displayed on the web.
          // See function _copyDescriptorsToDisplay() below to
          // know how this list is filled in.
          $scope.toDisplay = null;
          $scope.errors = null; // jsonName => errorMessage

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
            $scope.toDisplay = _.chain($scope.descriptors).
              filter(
                // Filter for inline layout.
                function(d) {
                  // For "inline layout" skip desctiptors which are tolerance.
                  // In "inline layout" a tolarace is displayed on the line of nominal value.
                  return !$scope.opts.inlineLayout || $scope.opts.inlineLayout && !d.tolerance;
                }
              )
              .map($scope._toDisplayObject) // convert a descriptor to a "display object"
              .filter(
                // Filter hide/show blank.
                function(dto) {
                  return (!$scope.opts.hideBlank || $scope.opts.hideBlank && dto.displayValue) && (!normalizedFilter || normalizedFilter && (
                    dto.filterName.indexOf(normalizedFilter) > -1 ||
                    dto.filterValue && dto.filterValue.indexOf(normalizedFilter) > -1
                  ));
                }
              )
              .value();
            $scope.errors = {};
          }; // _copyDescriptorsToDisplay


          // Watch properties valid/invalid for controls on the form
          // and update state of properties "error message" in
          // the backend "display objects".
          _.each($scope.descriptors, function(d) {
            $scope.$watch("cdForm." + d.jsonName + ".$valid", function(valid, oldVal) {
              //$log.log("Watch::" + d.jsonName + ": " + newVal + ", " + oldVal);
              if (valid === true) {
                //$log.log("Clear error messaged in: " + d.jsonName);
                delete $scope.errors[d.jsonName];
              } else if (valid === false) {
                //$log.log("Added error messaged in: " + d.jsonName);
                $scope.errors[d.jsonName] = $scope.getErrorFor(d.jsonName);
              }
            });
          });

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

          $scope._inputType = function(d) {
            var retVal = null;
            var dt = d.dataType;
            if (dt !== "ENUMERATION") {
              retVal = dt == "DECIMAL" ? "number" : "text";
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
              var valEnum;
              var mEnum = descriptor.enumeration;
              if (mEnum) {
                valEnum = mEnum.values;
              } else {
                throw Error("definition of the enum for the field '" + descriptor.jsonName + "' not found.");
              }
              if (valEnum !== undefined) {
                var selected = _.find(valEnum, function(ve) {
                  return ve.id == value.id;
                });
                if (selected !== undefined) {
                  retVal = selected.val; // 'val' is a text representation
                } else {
                  throw new Error("value '" + value + "' not found in the enumeration '" + angular.toJson(descriptor.enumeration) + "'.");
                }
              } else {
                throw new Error("definition of the enum '" + mEnum.name + "' not found.");
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
            try {
              // Display value.
              var displayValue = $scope._getDisplayVal(dispObj.valueDescriptor, dispObj.value, dispObj.displayUnit);
              if (dispObj.toleranceJsonName) {
                // If the dispOject has a tolerance.
                var toleranceDisplayValue = $scope._getDisplayVal(dispObj.toleranceDescriptor, dispObj.tolerance, dispObj.toleranceDisplayUnit);
                if (displayValue) {
                  displayValue += (" " + String.fromCharCode(0x00B1) + " " + toleranceDisplayValue);
                }
              }
              dispObj.displayValue = displayValue;
              dispObj.filterValue = displayValue.toLowerCase();
            } catch (e) {
              dispObj.invalidDisplayValue = true;
              dispObj.displayValue = $scope._buildErrorMessage(e.message);
            }

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
          //  * inputType             (optional)
          // In a case of the "inline layout" the object also has following extra properties:
          //  * toleranceJsonName     (optional)
          //  * tolerance             (optional)
          //  * toleranceDescriptor   (optional)
          //  * toleranceDisplayUnit  (optional)
          //  * toleranceInputType    (optional)
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
            var inputType = $scope._inputType(d);
            if (inputType) {
              retVal.inputType = inputType;
            }
            // Add properties for tolerance if any.
            if ($scope.opts.inlineLayout && d.tolerance === false) { // nominal value
              // This is a special case when we should display nominal and tolerance values in the "inline layout" .

              // Try to find a descriptor for a tolerance and a tolerance.
              // In case of success use the descriptor to format the value and add
              // the formatted value to the nominal value to display.
              /*
              var toleranceJsonName = d.jsonName + "Tol";
              var toleranceDesc = $scope.idxDescriptorsByJsonName[toleranceJsonName];
              */
              var toleranceDesc = $scope.idxDim2Tol[d.id];
              if (toleranceDesc && toleranceDesc.tolerance === true) { // if this is a real tolerance
                var tolerance = $scope.part[toleranceDesc.jsonName];
                if (tolerance !== undefined) {
                  retVal.toleranceJsonName = toleranceDesc.jsonName;
                  retVal.tolerance = tolerance;
                  retVal.toleranceDisplayValue = $filter("number")(tolerance, toleranceDesc.scale);
                  retVal.toleranceDisplayUnit = $scope._unit2displaystr(toleranceDesc);
                  retVal.toleranceDescriptor = toleranceDesc;
                  var toleranceInputType = $scope._inputType(toleranceDesc);
                  if (toleranceInputType) {
                    retVal.toleranceInputType = toleranceInputType;
                  }
                }
              }
            }
            $scope._addDisplayValue(retVal);
            if (!retVal.invalidDisplayValue && d.dataType === "ENUMERATION") {
              // It is possible that property 'invalidDisplayValue' was not set in the '_getDisplayVal'
              // even when enumeration is not defined (see comments for the method '_getDisplayVal' for details).
              // So we do check too for existence of the enumeration here.
              if (d.enumeration) {
                retVal.selectOptions = angular.copy(d.enumeration.values);
              } else {
                retVal.displayValue = $scope._buildErrorMessage("definition of the enum for the field '" + d.jsonName + "' not found.");
                retVal.invalidDisplayValue = true;
              }
            }
            return retVal;
          }; // $scope._getDisplayObject(d);

          $scope._errorId2errorMessage = function(errorId) {
            if (errorId === "required") {
              return "The vaue is required!";
            } else if (errorId === "number") {
              return "Invalid number!";
            } else if (errorId === "maxlength") {
              return "The value is too long!";
            } else if (errorId === "min") {
              return "The value is lower than minimal allowed value!";
            } else if (errorId === "max") {
              return "The value is higher than maximal allowed value!";
            } else if (errorId == "criticalDimensionScaleValidator") {
              return "Too many digits in the fraction!";
            } else {
              return "Unknown error: " + angular.toJson(errorId);
            }
          };

          $scope.getErrorFor = function(controlName) {
            var control = $scope.cdForm[controlName];
            if (control === undefined) {
              return null;
            }
            var errors = control.$error;
            if (errors === undefined) {
              return null;
            }
            var firstError;
            _.find(errors, function(value, key) {
              if (value === true) {
                firstError = key;
                return true;
              } else {
                return false;
              }
            });
            return $scope._errorId2errorMessage(firstError);
          };

          // ************
          // *** CRUD ***
          // ************
          $scope.editedDispObjs = {}; // critical dimensions that are modfying (but modified object is $scope.editedPart)
          // It is important to copy the 'part' to 'editedPart' because validation on the UI form is done against 'editedPart'
          // and if this member is null or undefined than some validators (e.g. 'required') can be triggered (false positive).
          $scope.editedPart = Restangular.copy($scope.part);

          $scope.modifyStart = function(dispObj) {
            //$scope.editedPart = Restangular.copy($scope.part);
            $scope.editedDispObjs[dispObj.id] = dispObj;
          };

          $scope.modifyStartAll = function() {
            $scope.editedPart = Restangular.copy($scope.part);
            _.each($scope.toDisplay, function(dispObj) {
              $scope.editedDispObjs[dispObj.id] = dispObj;
            });
          };

          $scope._modifyEnd = function(d) {
            delete $scope.editedDispObjs[d.id];
          };

          $scope._modifyEndAll = function() {
            $scope.editedDispObjs = {};
          };

          $scope.isEditing = function() {
            return !angular.equals({}, $scope.editedDispObjs);
          };

          $scope.isModifying = function(d) {
            return $scope.editedDispObjs[d.id] !== undefined;
          };

          $scope.modifySaveAll = function() {
            restService.updatePart($scope.editedPart).then(
              function success(updatedPart) {
                gToast.open("The part has been successfully updated.");
                $scope.part = updatedPart;
                $scope.editedPart = Restangular.copy($scope.part);
                $scope._modifyEndAll();
                $scope._copyDescriptorsToDisplay(); // redraw with updated values
              },
              function failure(response) {
                restService.error("Updating of the part failed.", response);
              }
            );
          };

          $scope.modifyUndo = function(d) {
            $scope.editedPart[d.jsonName] = $scope.part[d.jsonName];
            if (d.toleranceJsonName !== null) {
              $scope.editedPart[d.toleranceJsonName] = $scope.part[d.toleranceJsonName];
            }
          };

          $scope.modifyUndoAll = function() {
            $scope.editedPart = Restangular.copy($scope.part);
          };

          $scope.modifyCancel = function(d) {
            $scope.modifyUndo(d);
            $scope._modifyEnd(d);
          };

          $scope.modifyCancelAll = function() {
            $scope.modifyUndoAll();
            $scope._modifyEndAll();
          };

          var fdLegendImage = null;

          $scope.onSelectLegendImage = function(files) {
            $scope.legendImage = files[0];
          };

          $scope.uploadLegend = function() {
            $("#dlgUploadLegend").modal("hide");
            var fdLegendImage = new FormData();
            fdLegendImage.append("file", $scope.legendImage);
            restService.uploadPartCritDimsLegend($scope.part.id, /*fdLegendImage*/ $scope.legendImage).then(
              function success(imageUrl) {
                $scope.part.cdLegendUrl = imageUrl;
              },
              function failure(response) {
                restService.error("Uploading of a part critical dimensions legend failed.", response);
              }
            ).finally(function() {
              fdLegendImage = null;
              $scope.legendImage = null;
            });
          };

          $scope.showDeleteLegendDlg = function() {
            if ($scope.part.cdLegendUrl) {
              $('#dlgDeleteLegend').modal("show");
            }
          };

          $scope.deleteLegend = function() {
            $('#dlgDeleteLegend').modal("hide");
            restService.deletePartCritdimsLegend($scope.part.id).then(
              function success() {
                $scope.part.cdLegendUrl = null;
              },
              function failure(response) {
                restService.error("Deletion of a part critical dimensions legend failed.", response);
              }
            );
          };

        }]
      };
    }])
  .directive('criticalDimensionValidator', ["$log", function($log) {
    /**
     * Do validation of a critical dimension.
     * It validates only constraints which are not covered by
     * standart validators.
     */
    return {
      restrict: "A",
      require: "ngModel",
      link: function(scope, elm, attrs, ctrl) {
        var dispObj = scope[attrs.criticalDimensionValidator];
        if (!angular.isObject(dispObj)) {
          return;
        }
        // Find a critical dimension descriptor.
        var descriptor = null;
        var jsonName = attrs.name;
        if (jsonName === dispObj.jsonName) {
          descriptor = dispObj.valueDescriptor;
        } else {
          descriptor = dispObj.toleranceDescriptor;
        }
        if (!angular.isObject(descriptor)) {
          $log.error("Critical dimension descriptor not found: " + jsonName);
        }
        if (descriptor.dataType === "DECIMAL" && descriptor.scale !== null) {
          ctrl.$validators.criticalDimensionScaleValidator = function(modelValue, viewValue) {
            if (!angular.isObject(viewValue)) {
              return true;
            }
            var dp = viewValue.indexOf(".");
            if (dp == -1) {
              return true;
            }
            var scaleLength = viewValue.length - dp - 1;
            if (scaleLength <= descriptor.scale) {
              return true;
            }
            return false;
          };
        }
      }
    };
  }]);

