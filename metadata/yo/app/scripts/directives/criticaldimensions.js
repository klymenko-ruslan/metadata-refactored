"use strict";

angular.module("ngMetaCrudApp")
  .directive("criticalDimensions", ["$log", "$filter",
    function($log, $filter) {

      var _indexTolerances = function(descriptors) {
        // TODO: optimize (use singe iteration)
        return _.chain(descriptors)
          .filter(function(d) {
            // Get only dimensions with parents
            // (in other words -- tolerances).
            return d.parent !== null;
          })
          .groupBy(function(d) {
            // Associate each nominal's descriptor ID with
            // a tolerance descriptor.
            // Returns: {"parent_id": [tolerance0, tolerance1], ...}
            return d.parent.id;
          })
          .mapObject(function(descriptors_array, key) {
            // Returns:
            // {
            //    "parent_id": {
            //      "BOTH": plus_minus_tolerance |
            //      "UPPER": upper_tolerance,
            //      "LOWER: lover_tolerance"
            //    }
            // }
            return _.indexBy(descriptors_array, "tolerance");
          })
          .value();
      };

      var _unit2displaystr = function(d) {
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

      var _inputType = function(d) {
        var retVal = null;
        var dt = d.dataType;
        if (dt !== "ENUMERATION") {
          retVal = dt == "DECIMAL" ? "number" : "text";
        }
        return retVal;
      };

      var _buildErrorMessage = function(msg) {
        return "Error: " + msg;
      };

      var _errorId2errorMessage = function(errorId) {
        if (errorId === "required") {
          return "The vaue is required!";
        } else if (errorId === "number") {
          return "Invalid number!";
        } else if (errorId === "maxlength" || errorId === "criticalDimensionLengthValidator") {
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

      // Get formatted critical dimension value that is ready for displaying
      // on the UI. If for some reason the value can't be displayed on
      // the web correctly then this method returns error message.
      // For null or undefined values cheks of the value are not done,
      // so it is possible that errors were not detected.
      // Such situations are processed in the code "in-place".
      var _getDisplayVal = function(descriptor, value) {
        var retVal = "";
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
            throw Error("definition of the enum for the field '" +
              descriptor.jsonName + "' not found.");
          }
          if (valEnum !== undefined) {
            var selected = _.find(valEnum, function(ve) {
              return ve.id == value.id;
            });
            if (selected !== undefined) {
              retVal = selected.val; // 'val' is a text representation
            } else {
              throw new Error("value '" + value +
                "' not found in the enumeration '" +
                angular.toJson(descriptor.enumeration) + "'.");
            }
          } else {
            throw new Error("definition of the enum '" +
              mEnum.name + "' not found.");
          }
        } else {
          retVal = String(value);
        }
        if (retVal !== "") {
          retVal += _unit2displaystr(descriptor);
        }
        return retVal;
      };

      var _addTolerance2dispObj = function(prefix, part, toleranceDesc,
        dispObj) {
        if (toleranceDesc) {
          var toleranceValue = part[toleranceDesc.jsonName];
          if (toleranceValue !== undefined) {
            dispObj[prefix + "ToleranceJsonName"] = toleranceDesc.jsonName;
            dispObj[prefix + "Tolerance"] = toleranceValue;
            dispObj[prefix + "ToleranceDisplayValue"] =
              $filter("number")(toleranceValue, toleranceDesc.scale);
            // dispObj[prefix + "ToleranceDisplayUnit"] =
            //   _unit2displaystr(toleranceDesc);
            dispObj[prefix + "ToleranceDescriptor"] = toleranceDesc;
            var toleranceInputType = _inputType(toleranceDesc);
            if (toleranceInputType) {
              dispObj[prefix + "ToleranceInputType"] = toleranceInputType;
            }
          } else {
            $log.log("Critical dimension '" + toleranceDesc.jsonName +
              "' is not defined in the 'Part' [" + part.class + "] entity. " +
              "Check JPA entity.");
          }
        }
      };

      var _getToleranceDisplayVal = function(prefix, dispObj) {
        var retVal = null;
        var toleranceDescriptor = dispObj[prefix + "ToleranceDescriptor"];
        if (toleranceDescriptor) { // If the dispOject has a tolerance.
          var toleranceValue = dispObj[prefix + "Tolerance"];
          retVal = _getDisplayVal(toleranceDescriptor,
            toleranceValue);
        }
        return retVal;
      };

      var _addDisplayValue = function(dispObj) {
        // Calculate and add property 'displayValue' to
        // the 'display object'.
        try {
          // Display value.
          var displayValue = _getDisplayVal(dispObj.valueDescriptor,
            dispObj.value);

          var bothToleranceDispVal = _getToleranceDisplayVal("both",
            dispObj);
          if (bothToleranceDispVal) {
            displayValue += (" " + String.fromCharCode(0x00B1) + " " +
              bothToleranceDispVal);
          }
          var lowerToleranceDispVal = _getToleranceDisplayVal("lower",
            dispObj);
          if (lowerToleranceDispVal) {
            displayValue += (" " + String.fromCharCode(8595) +
              lowerToleranceDispVal);
          }
          var upperToleranceDispVal = _getToleranceDisplayVal("upper",
            dispObj);
          if (upperToleranceDispVal) {
            displayValue += (" " + String.fromCharCode(8593) +
              upperToleranceDispVal);
          }
          dispObj.displayValue = displayValue;
          dispObj.filterValue = displayValue.toLowerCase();
        } catch (e) {
          dispObj.invalidDisplayValue = true;
          dispObj.displayValue = _buildErrorMessage(e.message);
        }
      };

      return {
        restrict: "E",
        replace: false,
        transclude: true,
        templateUrl: "/views/component/criticaldimensions.html",
        scope: {
          part: "=",
          descriptors: "="
        },
        controller: ["$scope", "$log", "Restangular", "gToast",
          "restService", "METADATA_BASE",
          function($scope, $log, Restangular, gToast,
            restService, METADATA_BASE) {

            $scope.METADATA_BASE = METADATA_BASE;

            // Make a dictionary: nominal's
            // {"descriptor_id": {
            //    "BOTH": plus_minus_tolerance |
            //    "UPPER": upper_tolerance, "LOWER: lover_tolerance"}
            // }
            $scope.idxDim2Tol = _indexTolerances($scope.descriptors);

            //$log.log("idxDim2Tol: " + angular.toJson($scope.idxDim2Tol, 2));

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
                    // For "inline layout" skip desctiptors which
                    // are tolerance.
                    // In "inline layout" a tolarace is displayed
                    // on the line of nominal value.
                    return (!$scope.opts.inlineLayout ||
                      $scope.opts.inlineLayout && d.tolerance === null);
                  }
                )
                // convert a descriptor to a "display object"
                .map($scope._toDisplayObject)
                .filter(
                  // Filter hide/show blank.
                  function(dto) {
                    return (!$scope.opts.hideBlank ||
                        $scope.opts.hideBlank && dto.displayValue) &&
                      (!normalizedFilter || normalizedFilter && (
                        dto.filterName.indexOf(normalizedFilter) > -1 ||
                        dto.filterValue &&
                        dto.filterValue.indexOf(normalizedFilter) > -1
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
              $scope.$watch("cdForm." + d.jsonName + ".$valid",
                function(valid, oldVal) {
                  //$log.log("Watch::" + d.jsonName + ": " + newVal +
                  //  ", " + oldVal);
                  if (valid === true) {
                    //$log.log("Clear error messaged in: " + d.jsonName);
                    delete $scope.errors[d.jsonName];
                  } else if (valid === false) {
                    //$log.log("Added error messaged in: " + d.jsonName);
                    $scope.errors[d.jsonName] = $scope.getErrorFor(
                      d.jsonName);
                  }
                });
            });

            // This method build a JS object that represents a row in
            // the UI table.
            // The object contains values for displayed "name" and "value"
            // fields as well as auxiliary properties (objects) for
            // the row editing.
            // A returning object has following mondatory properties:
            //  * id
            //  * jsonName
            //  * displayName
            //  * filterName
            //  * // displayUnit
            //  * value
            //  * filterValue
            //  * valueDescriptor
            //  * displayValue
            //  * selectOptions         (optional)
            //  * inputType             (optional)
            // In a case of the "inline layout" the object also has
            // following extra properties:
            //  * toleranceJsonName     (optional)
            //  * tolerance             (optional)
            //  * toleranceDescriptor   (optional)
            //  * // toleranceDisplayUnit  (optional)
            //  * toleranceInputType    (optional)
            //  If a value of a critical dimesions, for some reason,
            //  can't be correctly displayed on the UI then following
            //  property is added:
            //  * invalidDisplayValue   (optional)
            $scope._toDisplayObject = function(d) {
              var retVal = {};
              retVal.id = d.id;
              retVal.jsonName = d.jsonName;
              retVal.displayName = d.name;
              retVal.filterName = retVal.displayName.toLowerCase();
              // retVal.displayUnit = _unit2displaystr(d);
              var val = $scope.part[d.jsonName];
              retVal.value = val;
              retVal.valueDescriptor = d;
              var inputType = _inputType(d);
              if (inputType) {
                retVal.inputType = inputType;
              }
              // Add properties for tolerance if any.
              var isNominal = d.tolerance === null;
              if ($scope.opts.inlineLayout && isNominal) {
                // This is a special case when we should display nominal
                // and tolerance values in the "inline layout" .

                // Try to find a descriptor for a tolerance.
                // In case of success use the descriptor to format
                // the value and add
                // the formatted value to the nominal value to display.
                var tolerancesDescs = $scope.idxDim2Tol[d.id];
                if (tolerancesDescs) { // if this is a real tolerance
                  var both = tolerancesDescs.BOTH;
                  var lower = tolerancesDescs.LOWER;
                  var upper = tolerancesDescs.UPPER;
                  if (both & (lower | upper)) {
                    $log.log("both: " + angular.toJson(both, 2));
                    $log.log("lower: " + angular.toJson(lower, 2));
                    $log.log("upper: " + angular.toJson(upper, 2));
                    throw new Error("Internal error. Expected either " +
                      "regular tolerance or upper or/and lower ones.");
                  }
                  _addTolerance2dispObj("both", $scope.part, both, retVal);
                  _addTolerance2dispObj("lower", $scope.part, lower, retVal);
                  _addTolerance2dispObj("upper", $scope.part, upper, retVal);
                }
              }
              _addDisplayValue(retVal);
              if (!retVal.invalidDisplayValue &&
                d.dataType === "ENUMERATION") {
                // It is possible that property 'invalidDisplayValue'
                // was not set in the '_getDisplayVal'
                // even when enumeration is not defined (see comments
                // for the method '_getDisplayVal' for details).
                // So we do check too for existence of the enumeration here.
                if (d.enumeration) {
                  retVal.selectOptions = angular.copy(d.enumeration.values);
                } else {
                  retVal.displayValue = _buildErrorMessage("definition " +
                    "of the enum for the field '" +
                    d.jsonName +
                    "' not found.");
                  retVal.invalidDisplayValue = true;
                }
              }
              return retVal;
            }; // $scope._getDisplayObject(d);

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
              return _errorId2errorMessage(firstError);
            };

            // ************
            // *** CRUD ***
            // ************
            // Critical dimensions that are modfying (but modified
            // object is $scope.editedPart).
            $scope.editedDispObjs = {};
            // It is important to copy the 'part' to 'editedPart' because
            // validation on the UI form is done against 'editedPart'
            // and if this member is null or undefined than some
            // validators (e.g. 'required') can be
            // triggered (false positive).
            $scope.editedPart = Restangular.copy($scope.part);

            $scope.modifyStart = function(dispObj) {
              //$scope.editedPart = Restangular.copy($scope.part);
              //$log.log("dispObj: " + angular.toJson(dispObj, 2));
              $scope.editedDispObjs[dispObj.id] = dispObj;
            };

            $scope.modifyStartAll = function() {
              //$scope.editedPart = Restangular.copy($scope.part);
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
                  // redraw with updated values
                  $scope._copyDescriptorsToDisplay();
                },
                function failure(response) {
                  restService.error("Updating of the part failed.", response);
                }
              );
            };

            $scope.modifyUndo = function(d) {
              $scope.editedPart[d.jsonName] = $scope.part[d.jsonName];
              if (d.toleranceJsonName !== null) {
                $scope.editedPart[d.toleranceJsonName] =
                  $scope.part[d.toleranceJsonName];
              }
              if (d.bothToleranceJsonName !== null) {
                $scope.editedPart[d.bothToleranceJsonName] =
                  $scope.part[d.bothToleranceJsonName];
              }
              if (d.lowerToleranceJsonName !== null) {
                $scope.editedPart[d.lowerToleranceJsonName] =
                  $scope.part[d.lowerToleranceJsonName];
              }
              if (d.upperToleranceJsonName !== null) {
                $scope.editedPart[d.upperToleranceJsonName] =
                  $scope.part[d.upperToleranceJsonName];
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

            $scope.onSelectLegendImage = function(files) {
              $scope.legendImage = files[0];
            };

            $scope.uploadLegend = function() {
              $("#dlgUploadLegend").modal("hide");
              if ($scope.legendImage === null ||
                $scope.legendImage === undefined) {
                $("#dlgFileNotSelected").modal("show");
                return;
              }
              restService.uploadPartCritDimsLegend($scope.part.id,
                $scope.legendImage).then(
                function success(httpResponse) {
                  $scope.part.legendImgFilename =
                    httpResponse.data.legendImgFilename;
                },
                function failure(response) {
                  restService.error("Uploading of a part critical " +
                    "dimensions legend failed.", response);
                }
              ).finally(function() {
                $scope.legendImage = null;
              });
            };

            $scope.showDeleteLegendDlg = function() {
              if ($scope.part.legendImgFilename) {
                $('#dlgDeleteLegend').modal("show");
              }
            };

            $scope.deleteLegend = function() {
              $('#dlgDeleteLegend').modal("hide");
              restService.deletePartCritdimsLegend($scope.part.id).then(
                function success() {
                  $scope.part.legendImgFilename = null;
                },
                function failure(response) {
                  restService.error("Deletion of a part " +
                    "critical dimensions legend failed.", response);
                }
              );
            };

          }
        ]
      };
    }
  ])
  .directive('criticalDimensionValidator', ["$log", "$parse",
    function($log, $parse) {
      /**
       * Do validation of a critical dimension.
       * It validates only constraints which are not covered by
       * standart validators.
       */
      return {
        restrict: "A",
        require: "ngModel",
        link: function(scope, elm, attrs, ctrl) {
          var descriptor = $parse(attrs.criticalDimensionValidator)(scope);
          if (!angular.isObject(descriptor)) {
            $log.log("Descriptor not found.");
            return;
          }
          if (descriptor.dataType === "DECIMAL" &&
            descriptor.scale !== null) {
            ctrl.$validators.criticalDimensionScaleValidator =
              function(modelValue, viewValue) {
                if (viewValue != null) { // null or undefined
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
            ctrl.$validators.criticalDimensionLengthValidator =
              function(modelValue, viewValue) {
                if (viewValue == null) { // null or undefined
                  return true;
                }
                try {
                  var valid = Math.abs(viewValue).toString().length <=
                    descriptor.length;
                  return valid;
                } catch (e) {
                  return true;
                }
              };
          }
        }
      };
    }
  ]);
