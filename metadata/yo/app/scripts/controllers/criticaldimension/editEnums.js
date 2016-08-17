"use strict";

angular.module("ngMetaCrudApp")
    .controller("CriticalDimensionEnumsCtrl", [
      "$scope", "$log", "Restangular", "gToast", "ngTableParams", "restService", "utils", "critDimEnums",
      function($scope, $log, Restangular, gToast, ngTableParams, restService, utils, critDimEnums) {

        $scope.selectedRow = null;

        $scope.critDimEnumItms = null;

        $scope.cdEnumToAdd = null;

        $scope.cdEnumToDel = null;

        $scope.cdEnumItmToAdd = null;

        $scope.cdEnumItmToDel = null;

        // Edited critical dimension (copy of a row in the UI table).
        $scope.cdeModifying = null; // Don't rename. The name is '$eval'ed in validators
        $scope.cdeModifyingRef = null;

        $scope.cdevModifying = null; // Don't rename. The name is '$eval'ed in validators
        $scope.cdevModifyingRef = null;

        $scope.critDimEnumsTableParams = new ngTableParams({
          page: 1,
          count: 10
        }, {
          getData: utils.localPagination(critDimEnums)
        });

        $scope.critDimEnumValsTableParams = null;

        $scope.critDimEnumValsPending = false;

        $scope.selectRow = function(row) {
          if ($scope.critDimEnumValsPending || $scope.cdeIsModifying()) {
            return;
          }
          $scope.selectedRow = null;

          $scope.cdeModifying = null;
          $scope.cdeModifyingRef = null;
          $scope.cdevModifying = null;
          $scope.cdevModifyingRef = null;

          $scope.critDimEnumValsPending = true;
          // Load items of the enumeration.
          restService.getCritDimEnumVals(row.id)
            .then(
              function success(enumVals) {
                $scope.critDimEnumItms = enumVals;
                $scope.critDimEnumValsTableParams = new ngTableParams({
                  page: 1,
                  count: 10
                }, {
                  getData: utils.localPagination($scope.critDimEnumItms)
                });
                $scope.selectedRow = row;
              },
              function failure(response) {
                $scope.critDimEnumItms = null;
                restService.error("Loading of items for the enumeration failed.", response);
              }
            )
            .finally(function() {
              $scope.critDimEnumValsPending = false;
            });
        };

        $scope.isSelected = function(row) {
          return $scope.selectedRow !== null && (row === undefined || row.id === $scope.selectedRow.id);
        };

        $scope.selectFirstEnum = function() {
          if (angular.isObject(critDimEnums) && critDimEnums.length > 0) {
            $scope.selectRow(critDimEnums[0]);
            return true;
          }
          return false;
        };

        $scope.showAddEnumDlg = function() {
          //$scope._resetForm($scope.formAddEnum);
          $scope.cdEnumToAdd = {
            name: null
          };
          $("#addEnumDlg").modal("show");
        };

        $scope.addEnum = function() {
          restService.addCritDimEnum($scope.cdEnumToAdd).then(
            function success(addedCde) {
              critDimEnums.push(addedCde);
              $scope.critDimEnumsTableParams.reload();
              $("#addEnumDlg").modal("hide");
            },
            function failure(response) {
              $("#addEnumDlg").modal("hide");
              restService.error("Can't add the new enumeration.", response);
            }
          ).finally(function() {
            $scope.cdEnumToAdd = null;
          });
        };

        $scope.showDeleteEnumDlg = function(event, row) {
          event.stopPropagation();
          $scope.cdEnumToDel = row;
          $("#delEnumDlg").modal("show");
        };

        $scope.removeCritDimEnum = function() {
          var id = $scope.cdEnumToDel.id;
          restService.removeCritDimEnum(id)
            .then(
              function success() {
                var foundIdx = _.findIndex(critDimEnums, function(e) {
                  return e.id === id;
                });
                if (foundIdx > -1) {
                  critDimEnums.splice(foundIdx, 1);
                  $scope.critDimEnumsTableParams.reload();
                }
                if (id == $scope.selectedRow.id) {
                  var selected = $scope.selectFirstEnum();
                  if (!selected) {
                    $scope.critDimEnumValsTableParams = new ngTableParams({
                      page: 1,
                      count: 10
                    }, {
                      getData: utils.localPagination(null)
                    });
                  }
                }
                $("#delEnumDlg").modal("hide");
              },
              function failure(response) {
                $("#delEnumDlg").modal("hide");
                restService.error("Can't delete the enumeration.", response);
              }
            ).finally(function() {
              $scope.cdEnumToDel = null;
            });
        };

        $scope.showAddEnumItmDlg = function() {
          //$scope._resetForm($scope.formAddItmEnum);
          $scope.cdEnumItmToAdd = {
            val: null
          };
          $("#addEnumItmDlg").modal("show");
        };

        $scope.addEnumItm = function() {
          restService.addCritDimEnumItm($scope.selectedRow.id, $scope.cdEnumItmToAdd).then(
            function success(addedCdeItm) {
              $scope.critDimEnumItms.push(addedCdeItm);
              $scope.critDimEnumValsTableParams.reload();
              $("#addEnumItmDlg").modal("hide");
            },
            function failure(response) {
              $("#addEnumItmDlg").modal("hide");
              restService.error("Can't add the new enumeration item.", response);
            }
          ).finally(function() {
            $scope.cdEnumItmToAdd = null;
            $scope._resetForm($scope.formAddItmEnum);
          });
        };

        $scope.showDeleteEnumItmDlg = function(event, row) {
          event.stopPropagation();
          $scope.cdEnumItmToDel = row;
          $("#delEnumItmDlg").modal("show");
        };

        $scope.removeCritDimEnumItm = function() {
          var id = $scope.cdEnumItmToDel.id;
          restService.removeCritDimEnumItm(id)
            .then(
              function success() {
                var foundIdx = _.findIndex($scope.critDimEnumItms, function(e) {
                  return e.id === id;
                });
                if (foundIdx > -1) {
                  $scope.critDimEnumItms.splice(foundIdx, 1);
                  $scope.critDimEnumValsTableParams.reload();
                }
                $("#delEnumItmDlg").modal("hide");
              },
              function failure(response) {
                $("#delEnumItmDlg").modal("hide");
                restService.error("Can't delete the item in the enumeration.",
                  response);
              }
            ).finally(function() {
              $scope.cdEnumItmToDel = null;
            });
        };

        $scope._resetForm = function(form) {
          form.$rollbackViewValue();
          form.$setPristine();
        };

        $scope.cdeIsModifying = function(row) {
          return angular.isObject($scope.cdeModifying) && (!angular.isObject(row) || row.id === $scope.cdeModifying.id);
        };

        $scope.cdevIsModifying = function(row) {
          return angular.isObject($scope.cdevModifying) && (!angular.isObject(row) || row.id === $scope.cdevModifying.id);
        };

        $scope.cdeStartModify = function(event, row) {
          event.stopPropagation();
          $scope.cdeModifyingRef = row;
          $scope.cdeModifying = Restangular.copy(row);
        };

        $scope.cdevStartModify = function(row) {
          $scope.cdevModifyingRef = row;
          $scope.cdevModifying = Restangular.copy(row);
        };

        $scope.cdeModifyUndo = function(event, row, form) {
          $scope.cdeStartModify(event, row);
          $scope._resetForm(form);
        };

        $scope.cdevModifyUndo = function(row, form) {
          $scope.cdevStartModify(row);
          $scope._resetForm(form);
        };

        $scope.cdeModifyCancel = function(event) {
          event.stopPropagation();
          $scope.cdeModifying = null;
          $scope.cdeModifyingRef = null;
        };

        $scope.cdevModifyCancel = function() {
          $scope.cdevModifying = null;
          $scope.cdevModifyingRef = null;
        };

        $scope.cdeModifySave = function(event) {
          event.stopPropagation();
          var tosave = $scope.cdeModifying;
          restService.updateCritDimEnum(tosave)
            .then(
              function success(updatedCde) {
                $scope.cdeModifyingRef.id = updatedCde.id;
                $scope.cdeModifyingRef.name = updatedCde.name;
              },
              function failure(response) {
                restService.error("Can't update the enumeration.",
                  response);
              }
            ).finally(function() {
              $scope.cdeModifyCancel(event);
            });
        };

        $scope.cdevModifySave = function() {
          var tosave = $scope.cdevModifying;
          restService.updateCritDimEnumItm(tosave)
            .then(
              function success(updatedCdev) {
                $scope.cdevModifyingRef.id = updatedCdev.id;
                $scope.cdevModifyingRef.val = updatedCdev.val;
              },
              function failure(response) {
                restService.error("Can't update the item in the enumeration.",
                  response);
              }
            ).finally(function() {
              $scope.cdevModifyCancel();
            });
        };

        // Select the first row.
        $scope.selectFirstEnum();

      }
    ])
    .directive("uniqueCdeName", ["$log", "$q", "restService", function($log, $q, restService) {
      return {
        require: "ngModel",
        link: function($scope, elm, attr, ctrl) {
          ctrl.$asyncValidators.uniqueCdeName = function(modelVal, viewVal) {
            if (ctrl.$isEmpty(modelVal)) {
              return $q.when();
            }
            var defer = $q.defer();
            restService.findCritDimEnumByName(viewVal).then(
              function success(foundCde) {
                if (foundCde === undefined) {
                  defer.resolve();
                } else {
                  var id = $scope.$eval("cdeModifying.id");
                  if (foundCde.id == id) {
                    defer.resolve();
                  } else {
                    defer.reject();
                  }
                }
              },
              function failure(response) {
                $log.log("Can't validate uniqueness of an enumeration name: " + angular.toJson(viewVal));
                def.reject();
              }
            );
            return defer.promise;
          };
        }
      };
    }])
    .directive("uniqueCdevName", ["$log", "$q", "restService", function($log, $q, restService) {
      return {
        require: "ngModel",
        link: function($scope, elm, attr, ctrl) {
          ctrl.$asyncValidators.uniqueCdevName = function(modelVal, viewVal) {
            if (ctrl.$isEmpty(modelVal)) {
              return $q.when();
            }
            var defer = $q.defer();
            var enumId = $scope.$eval("selectedRow.id");
            restService.findCritDimEnumItmByName(enumId, viewVal).then(
              function success(foundCdeItm) {
                if (foundCdeItm === undefined) {
                  defer.resolve();
                } else {
                  var id = $scope.$eval("cdevModifying.id");
                  if (foundCdeItm.id == id) {
                    defer.resolve();
                  } else {
                    defer.reject();
                  }
                }
              },
              function failure(response) {
                $log.log("Can't validate uniqueness of an enumeration item name: " + angular.toJson(viewVal));
                def.reject();
              }
            );
            return defer.promise;
          };
        }
      };
    }]);
