"use strict";

angular.module("ngMetaCrudApp")

.constant("BOM_RESULT_STATUS", {
  OK: "OK",
  ASSERTION_ERROR: "ASSERTION_ERROR",
  FOUND_BOM_RECURSION: "FOUND_BOM_RECURSION"
})

.controller("PartBomSearchCtrl", ["$log", "$scope", "$location", "$routeParams", "$uibModal",
  "BOM", "restService", "Restangular", "dialogs", "gToast", "partTypes", "part",
  function($log, $scope, $location, $routeParams, $uibModal, BOM, restService,
    Restangular, dialogs, gToast, partTypes, part) {
    $scope.partTypes = partTypes;
    $scope.restService = restService;
    $scope.partId = $routeParams.id;

    $scope.pickedPart = null;
    $scope.showPickedPart = false;

    // The part whose bom we're editing
    $scope.part = part;

    // Load the part's BOM
    $scope.bom = BOM.listByParentPartId($scope.partId).then(
      function(bom) {
        $scope.bom = bom;
      },
      restService.error
    );

    $scope.bomItem = {
      parentPartId: $scope.partId,
      childPartId: null,
      quantity: 1
    };

    $scope.save = function() {
        $uibModal.open({
        templateUrl: "/views/chlogsrc/LinkDlg.html",
        animation: false,
        size: "lg",
        controller: "ChlogSrcLinkDlgCtrl",
        backdrop: 'static',
        keyboard: false,
        resolve: {
          "partId": function () {
            return $scope.partId;
          },
          "bomItem": function () {
            return $scope.bomItem;
          },
          "sourcesNames": restService.getAllChangelogSourceNames()
        }
      });

    };

    $scope.pickBomItemPart = function(bomItemPartId, allowed) {
      if (allowed) {
        $scope.pickedPart = restService.findPart(bomItemPartId).then(
          function(pickedPart) {
            $scope.pickedPart = pickedPart;
            $scope.bomItem.childPartId = $scope.pickedPart.id;
          },
          function(errorResponse) {
            restService.error("Could not pick part.", errorResponse);
            $log.log("Could not pick part", errorResponse);
          }
        );
      } else {
        dialogs.error("Validation error", "Child part must have the same manufacturer as the Parent part.");
      }
    };

  }
]).controller("ChlogSrcLinkDlgCtrl", ["$scope", "$log", "$location", "dialogs", "gToast", "ngTableParams",
  "$uibModalInstance", "restService", "BOM_RESULT_STATUS", "partId", "bomItem",
  "sourcesNames",
  function($scope, $log, $location, dialogs, gToast, ngTableParams, $uibModalInstance, restService,
    BOM_RESULT_STATUS, partId, bomItem, sourcesNames) {

    $scope.partId = partId;
    $scope.sourcesNames = sourcesNames;
    $scope.forms = {
    };

    $scope.data = {
      confirmCancelView: {
        result: null
      },
      currVw: {
        id: null,
        title: null,
        actionBttnTitle: null
      },
      prevVw: {
        id: null,
        title: null,
        actionBttnTitle: null
      },
      crud: {
        source: {
        }
      }
    };

    $scope.sourceTableParams = new ngTableParams(
      {
        page: 1,
        count: 10,
        sorting: {}
      },
      {
        getData: function ($defer, params) {
          // Update the pagination info
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          var sortProperty, sortOrder;
          for (sortProperty in params.sorting()) break;
          if (sortProperty) {
            sortOrder = params.sorting()[sortProperty];
          }
          // TODO
          /*
          restService.filterCarModelEngineYears($scope.fltrCmey.cmey,
              $scope.fltrCmey.year, $scope.fltrCmey.make,
              $scope.fltrCmey.model, $scope.fltrCmey.engine,
              $scope.fltrCmey.fueltype,
              sortProperty, sortOrder, offset, limit).then(
            function (filtered) {
              $scope.cmeySearchResults = filtered;
              // Update the total and slice the result
              $defer.resolve($scope.cmeySearchResults.hits.hits);
              params.total($scope.cmeySearchResults.hits.total);
            },
            function (errorResponse) {
              $log.log("Couldn't search for 'carmodelengineyear'.");
              $defer.reject();
            }
          );
          */
        }
      }
    );


    function _save() {
      restService.createBom(bomItem).then(
        function(bomResult) {
          if (bomResult.status == BOM_RESULT_STATUS.OK) {
            // Success
            gToast.open("BOM item added.");
            $location.path("/part/" + $scope.partId);
          } else if (bomResult.status == BOM_RESULT_STATUS.ASSERTION_ERROR) {
            dialogs.error("Validation error", bomResult.message);
          } else if (bomResult.status == BOM_RESULT_STATUS.FOUND_BOM_RECURSION) {
            dialogs.error("Validation error", bomResult.message);
          } else {
            dialogs.error("Internal error", "Server returned unknown status of the operation: " + bomResult.status);
          }
        },
        function(response) {
          dialogs.error("Could not add BOM Item", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
        });

    };

    function _chvw(newViewId) {
      angular.copy($scope.data.currVw, $scope.data.prevVw);
      $scope.data.currVw.id = newViewId;
      if (newViewId === "sources_list") {
        $scope.data.currVw.title = "Link source";
        $scope.data.currVw.actionBttnTitle = "Save";
      } else if (newViewId === "create_new_source") {
        $scope.data.currVw.title = "Link source >> Create New Source";
        $scope.data.currVw.actionBttnTitle = "Create";
      } else if (newViewId === "confirm_cancel") {
        $scope.data.currVw.title = "Link source >> Confirmation";
        $scope.data.currVw.actionBttnTitle = "Confirm";
        $scope.data.confirmCancelView.result = "cancel_link"; // default value
      } else {
        throw "Unknown view id: " + angular.toJson(newViewId);
      };
    };

    function _createSource(name, description, url, sourceNameId) {
      restService.createChanlelogSource(name, description, url, sourceNameId).then(
        function success() {
          _chvw("sources_list");
        },
        function failure(errorResponse) {
          $uibModalInstance.close();
          restService.error("Could not create a new changelog source.", errorResponse);
        }
      );
    };

    $scope.actionBttnDisabled = function () {
      var retval = true;
      if ($scope.data.currVw.id === "confirm_cancel") {
        retval = false;
      } else if ($scope.data.currVw.id === "create_new_source" && $scope.forms.changelogSourceForm) {
        retval = $scope.forms.changelogSourceForm.$invalid;
      }
      return retval;
    };

    $scope.isBttnPickDisabled = function(_src) {
      return false; // TODO
    };

    $scope.pick = function(_src) {
      // TODO
    };

    $scope.clear = function() {
      // TODO
    };

    $scope.onCreateNewSource = function() {
      _chvw("create_new_source");
    };

    $scope.cancel = function() {
      var cv = $scope.data.currVw.id;
      if (cv === "sources_list") {
        // TODO: when user did nothing then the dialog can be closed without confirmation
        _chvw("confirm_cancel");
      } else if (cv === "create_new_source") {
        _chvw("sources_list");
      } else if (cv === "confirm_cancel") {
        _chvw($scope.data.prevVw.id); // return to a previous view
      } else {
        throw "Unknown current view [1]: " + angular.toJson(cv);
      }
    };

    $scope.action = function() {
      var cv = $scope.data.currVw.id;
      if (cv === "sources_list") {
        $uibModalInstance.close();
        _save();
      } else if (cv === "create_new_source") {
        var s = $scope.data.crud.source;
        _createSource(s.name, s.description, s.url, s.sourceName.id);
      } else if (cv === "confirm_cancel") {
        var result = $scope.data.confirmCancelView.result;
        $uibModalInstance.close();
        if (result === "cancel_link") {
          _save();
        } else if (result === "cancel_all") {
          $location.path("/part/" + $scope.partId);
        } else {
          throw "Unexpected confirmation dialog result: " + angular.toJson(result);
        }
      } else {
        throw "Unknown current view [2]: " + angular.toJson(cv);
      }
    };

    // *** Initialization ***
    _chvw("sources_list");

  }
]).directive("uniqueChangelogSourceName", ["$log", "$q", "restService", function($log, $q, restService) {
  // Validator for uniqueness of the changelog source name.
  return {
    require: "ngModel",
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.nonUniqueName = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        restService.findChangelogSourceByName(viewValue).then(
          function(changelogSource) {
            if (changelogSource === undefined) {
              def.resolve();
            } else {
              def.reject();
            }
          },
          function (errorResponse) {
            $log.log("Couldn't validate name of the changelog source name: " + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
