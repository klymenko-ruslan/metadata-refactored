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
          "sourcesNames": restService.getAllChangelogSourceNames(),
          "begin": function() {
            return restService.chanlelogSourceBeginEdit(); // needs to clear session attribute on the server side
          }
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
  "$uibModalInstance", "utils", "restService", "BOM_RESULT_STATUS", "partId", "bomItem",
  "sourcesNames", "begin",
  function($scope, $log, $location, dialogs, gToast, ngTableParams, $uibModalInstance, utils,
    restService, BOM_RESULT_STATUS, partId, bomItem, sourcesNames, begin) {

    $scope.partId = partId;
    $scope.sourcesNames = sourcesNames;

    var pickedSources = null;
    var pickedSourceIds = null;

    var attachments = null;

    var file = null;

    // Data to be uploaded
    var formData = null;

    $scope.model = null;

    function _reset() {
      pickedSources = [];
      pickedSourceIds = {};
      attachments = [];

      $scope.model = {
        attachDescr: null
      };

      formData = new FormData();

      $scope.forms = {
      };

      $scope.fltrSource = {
        name: null,
        description: null,
        url: null,
        sourceName: null
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
    };

    _reset();

    $scope.pickedSourcesTableParams = new ngTableParams(
      {
        page: 1,
        count: 10,
        sorting: {}
      },
      {
        getData: utils.localPagination(pickedSources)
      }
    );

    $scope.sourceTableParams = new ngTableParams(
      {
        page: 1,
        count: 10,
        sorting: {
          "name.lower_case_sort": "asc"
        }
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
          var snid = null;
          if ($scope.fltrSource.sourceName) {
            snid = $scope.fltrSource.sourceName.id;
          }
          restService.filterChangelogSource($scope.fltrSource.name, $scope.fltrSource.description,
            $scope.fltrSource.url, snid, sortProperty, sortOrder, offset, limit)
          .then(
            function (filtered) {
              // Update the total and slice the result
              $defer.resolve(filtered.hits.hits);
              params.total(filtered.hits.total);
            },
            function (errorResponse) {
              $log.log("Couldn't search for 'changelog source'.");
              $defer.reject();
            }
          );
        }
      }
    );

    $scope.attachmentsTableParams = new ngTableParams(
      {
        page: 1,
        count: 10,
        sorting: {}
      },
      {
        getData: utils.localPagination(attachments)
      }
    );

    function _save() {
      var srcIds = _.map(pickedSources, function(ps) {
        return ps.id;
      });
      restService.createBom(bomItem, srcIds).then(
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
          $scope.sourceTableParams.reload();
        },
        function failure(errorResponse) {
          $uibModalInstance.close();
          restService.error("Could not create a new changelog source.", errorResponse);
        }
      );
    };

    $scope.isActionBttnDisabled = function () {
      var retval = true;
      if ($scope.data.currVw.id === "sources_list") {
        retval = !pickedSources || pickedSources.length === 0;
      } else if ($scope.data.currVw.id === "confirm_cancel") {
        retval = false;
      } else if ($scope.data.currVw.id === "create_new_source" && $scope.forms.changelogSourceForm) {
        retval = $scope.forms.changelogSourceForm.$invalid;
      }
      return retval;
    };

    $scope.isBttnPickDisabled = function(s) {
      return s === undefined || pickedSourceIds[s.id];
    };

    $scope.isBttnUnpickAllDisabled = function() {
        return pickedSources.length === 0;
    };

    $scope.pick = function(pickedSrc) {
      pickedSources.push(pickedSrc);
      pickedSourceIds[pickedSrc.id] = true;
      $scope.pickedSourcesTableParams.reload();
    };

    $scope.unpick = function(srcId) {
      var idx = _.findIndex(pickedSources, function(s) {
        return s.id === srcId;
      });
      pickedSources.splice(idx, 1);
      delete pickedSourceIds[srcId];
      $scope.pickedSourcesTableParams.reload();
    };

    $scope.unpickAll = function(pickedSrcId) {
      _.each(pickedSources, function(ps) {
        delete pickedSourceIds[ps.id];
      });
      pickedSources.splice(0, pickedSources.length);
      $scope.pickedSourcesTableParams.reload();
    };

    $scope.clearFilter = function() {
      $scope.fltrSource.name = null;
      $scope.fltrSource.description = null;
      $scope.fltrSource.url = null;
      $scope.fltrSource.sourceName = null;
    };

    $scope.onCreateNewSource = function() {
      _chvw("create_new_source");
    };

    $scope.changedAttachment = function(files) {
      file = files[0];
      formData.append("file", files[0]);
    };

    function _updateAttachmentsTable(updatedAttachments) {
      attachments.splice(0, attachments.length);
      _.each(updatedAttachments, function (e) {
        attachments.push(e);
      });
      $scope.attachmentsTableParams.reload();
      formData = new FormData();
    };

    $scope.uploadAttachment = function() {
      restService.changelogSourceUploadAttachmentTmp(file, file.name, $scope.model.attachDescr).then(
        function(updatedAttachmentsResponse) {
          // Success
        _updateAttachmentsTable(updatedAttachmentsResponse.rows);
          gToast.open("File uploaded.");
        },
        function(response) {
          // Error
          restService.error("Could not upload the attachment.", response);
        }
      );
    };

    $scope.removeAttachment = function (idx) {
      restService.changelogSourceRemoveAttachmentTmp(idx).then(
        function(updatedAttachmentsResponse) {
          _updateAttachmentsTable(updatedAttachmentsResponse.rows);
        },
        function(errorResponse) {
          restService.error("Could not remove attachment.", errorResponse);
        }
      );
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

    // Handle updating search results
    $scope.$watch("[fltrSource]",
      function (newVal, oldVal) {
        // Debounce
        if (angular.equals(newVal, oldVal, true)) {
          return;
        }
        $scope.sourceTableParams.reload();
      },
      true
    );

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
