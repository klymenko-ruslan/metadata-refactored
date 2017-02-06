"use strict";

angular.module("ngMetaCrudApp")

.controller("ChlogSrcLinkDlgCtrl", ["$scope", "$log", "$location", "dialogs", "gToast", "ngTableParams",
  "$uibModalInstance", "utils", "restService", "BOM_RESULT_STATUS", "partId", "bomItem",
  "sourcesNames", "lastPicked", "User", "cancelUrl", "begin",
  function($scope, $log, $location, dialogs, gToast, ngTableParams, $uibModalInstance, utils,
    restService, BOM_RESULT_STATUS, partId, bomItem, sourcesNames, lastPicked, User, cancelUrl, begin) { // injection "begin" is important

    $scope.partId = partId;
    $scope.sourcesNames = sourcesNames;

    var userMustLink = !User.hasRole("ROLE_CHLOGSRC_SKIP");
    var canCreateSource = User.hasRole("ROLE_CHLOGSRC_CREATE");
    var canCreateSourceName = User.hasRole("ROLE_CHLOGSRCNAME_CREATE");

    var pickedSources = null;
    var pickedSourceIds = null;

    $scope.pickedSourcesRatings = null;

    var attachments = null;

    var file = null;

    // Data to be uploaded
    var formData = null;

    $scope.fltrSource = null;

    $scope.data = null;

    function _reset() {
      pickedSources = [];
      $scope.pickedSourcesRatings = [];
      pickedSourceIds = {};
      attachments = [];

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
        },
        description: null,
        attachDescr: null,
        newSourceName: null
      };
    };

    _reset();

    $scope.lastPickedTableParams = new ngTableParams(
      {
        page: 1,
        count: 5,
        sorting: {}
      },
      {
        counts: [5, 10, 15],
        getData: utils.localPagination(lastPicked)
      }
    );

    $scope.pickedSourcesTableParams = new ngTableParams(
      {
        page: 1,
        count: 5,
        sorting: {}
      },
      {
        counts: [5, 10, 15],
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

    function _save(woSource) {

      var srcIds, ratings;
      if(woSource) {
        srcIds = null;
        ratings = null;
      } else {
        srcIds = _.map(pickedSources, function(ps) { return ps.id; });
        ratings = $scope.pickedSourcesRatings;
      }

      restService.createBom(bomItem, srcIds, ratings, $scope.data.description).then(
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
        $scope.data.currVw.title = "Pick One or More Sources";
        $scope.data.currVw.actionBttnTitle = "Save";
      } else if (newViewId === "create_new_source") {
        $scope.data.currVw.title = "Link source >> Create New Source";
        $scope.data.currVw.actionBttnTitle = "Create";
      } else if (newViewId === "create_source_name") {
        $scope.data.currVw.title = "Link source >> Create New Source >> Create New Source Name";
        $scope.data.currVw.actionBttnTitle = "Create";
      } else {
        throw "Unknown view id: " + angular.toJson(newViewId);
      };
    };

    function _cleanCreateSourceForm() {
      $scope.data.crud.source = {}; // clean form
      attachments.splice(0, attachments.length);
      $scope.data.attachDescr = null;
      formData.delete("file");
    };

    function _createSource() {
      var s = $scope.data.crud.source;
      restService.createChangelogSource(s.name, s.description, s.url, s.sourceName.id).then(
        function success(newSource) {
          _chvw("sources_list");
          $scope.pick(newSource);
          $scope.sourceTableParams.reload();
        },
        function failure(errorResponse) {
          $uibModalInstance.close();
          restService.error("Could not create a new changelog source.", errorResponse);
        }
      );
    };

    function _createSourceName() {

      restService.createChangeSourceName($scope.data.newSourceName).then(
        function success(newSourceName) {
          gToast.open("The source name has successfully been created.");
          $scope.data.newSourceName = null;

          $scope.sourcesNames.push(newSourceName);
          $scope.sourcesNames.sort(function(a, b) {
            var name_a = a.name.toLowerCase();
            var name_b = b.name.toLowerCase();
            return (name_a < name_b ? -1 :(name_a > name_b ? 1 : 0));
          });
          $scope.data.crud.source.sourceName = newSourceName;

          _chvw("create_new_source");
        },
        function (errorResponse) {
          restService.error("Could not create a new source name: " + $scope.data.newName, errorResponse);
        }
      );

    };

    $scope.isActionBttnDisabled = function () {
      var retval = true;
      if ($scope.data.currVw.id === "sources_list") {
        retval = pickedSources.length === 0 || !canCreateSource;
      } else if ($scope.data.currVw.id === "create_new_source" && $scope.forms.changelogSourceForm) {
        retval = $scope.forms.changelogSourceForm.$invalid || !canCreateSource;
      } else if ($scope.data.currVw.id === "create_source_name" && $scope.forms.newSourceName) {
        retval = $scope.forms.newSourceName.$invalid || !canCreateSourceName;
      }
      return retval;
    };

    $scope.isBttnPickDisabled = function(s) {
      return s === undefined || pickedSourceIds[s.id];
    };

    $scope.isBttnUnpickAllDisabled = function() {
      return pickedSources.length === 0;
    };

    $scope.isBttnSaveWoSourceVisible = function() {
      return $scope.data.currVw.id === "sources_list";
    };

    $scope.isBttnSaveWoSourceDisabled = function() {
      return userMustLink;
    };

    $scope.saveWoSource = function() {
      $uibModalInstance.close();
      _save(true);
    };

    $scope.pick = function(pickedSrc) {
      pickedSources.push(pickedSrc);
      $scope.pickedSourcesRatings.push(0);
      pickedSourceIds[pickedSrc.id] = true;
      $scope.pickedSourcesTableParams.reload();
    };

    $scope.unpick = function(srcId) {
      var idx = _.findIndex(pickedSources, function(s) {
        return s.id === srcId;
      });
      pickedSources.splice(idx, 1);
      $scope.pickedSourcesRatings.splice(idx, 1);
      delete pickedSourceIds[srcId];
      $scope.pickedSourcesTableParams.reload();
    };

    $scope.unpickAll = function() {
      _.each(pickedSources, function(ps) {
        delete pickedSourceIds[ps.id];
      });
      pickedSources.splice(0, pickedSources.length);
      $scope.pickedSourcesRatings.splice(0, $scope.pickedSourcesRatings.length);
      $scope.pickedSourcesTableParams.reload();
    };

    $scope.clearFilter = function() {
      $scope.fltrSource.name = null;
      $scope.fltrSource.description = null;
      $scope.fltrSource.url = null;
      $scope.fltrSource.sourceName = null;
    };

    $scope.refreshList = function() {
      $scope.sourceTableParams.reload();
    };

    $scope.onCreateNewSource = function() {
      _cleanCreateSourceForm();
      _chvw("create_new_source");
    };

    $scope.onCreateSourceName = function() {
       $scope.data.newSourceName = null;
      _chvw("create_source_name");
    };

    $scope.isUploadBttnDisabled = function () {
      return !formData.has("file");
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
    };

    $scope.uploadAttachment = function() {
      restService.changelogSourceUploadAttachmentTmp(file, file.name, $scope.data.attachDescr).then(
        function(updatedAttachmentsResponse) {
          // Success
        _updateAttachmentsTable(updatedAttachmentsResponse.rows);
          gToast.open("File uploaded.");
          $scope.data.attachDescr = null;
          formData.delete("file");
          // TODO: reset upload form
        },
        function(response) {
          // Error
          alert("Could not upload the attachment.");
          $log.log("Could not upload the attachment.", response);
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
        $uibModalInstance.close();
        //$location.path("/part/" + $scope.partId);
        $location.path(cancelUrl);
      } else if (cv === "create_new_source") {
        _chvw("sources_list");
      } else if (cv === "create_source_name") {
        $scope.data.newSourceName = null;
        _chvw("create_new_source");
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
        _save(false);
      } else if (cv === "create_new_source") {
        _createSource();
      } else if (cv === "create_source_name") {
        _createSourceName();
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
]).directive("uniqueChangelogSourceByName", ["$log", "$q", "restService", function($log, $q, restService) {
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
              var id = $scope.$eval("source.id");
              if (changelogSource.id === id) {
                def.resolve();
              } else {
                def.reject();
              }
            }
          },
          function (errorResponse) {
            $log.log("Couldn't validate name of the changelog source: " + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
