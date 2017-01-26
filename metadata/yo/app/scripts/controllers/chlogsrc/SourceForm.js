"use strict";

angular.module("ngMetaCrudApp")

.controller("ChangelogSourcesFormCtrl", [
    "$scope", "$log", "$location", "gToast", "ngTableParams", "Restangular", "restService", "utils", "begin",
    "sourcesNames", "source",
  function($scope, $log, $location, gToast, ngTableParams, Restangular, restService, utils, begin,
    sourcesNames, source) {

    $scope.source = source;

    $scope.sourcesNames = sourcesNames;

    $scope.forms = {
      changelogSourceForm: null
    };

    $scope.data = {
      crud: {
        source: null
      },
      attachDescr: null
    };

    var attachments = null;
    var file = null;
    var formData = new FormData();

    if ($scope.source) {
      $scope.data.crud.source = Restangular.copy($scope.source);
      if (angular.isArray($scope.source.attachments)) {
        attachments = $scope.source.attachments.slice(); // copy array
      }
    }

    if (!angular.isArray(attachments)) {
      attachments = [];
    }

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

    $scope.onViewList = function() {
      $location.path("/changelog/source/list");
    };

    $scope.onView = function() {
      $location.path("/changelog/source/" + $scope.source.id);
    };

    $scope.onRevert = function() {
      $scope.data.crud.source = Restangular.copy($scope.source);
      var form = forms.changelogSourceForm;
      // form.$rollbackViewValue();
      form.$setPristine();
    };

    $scope.onSave = function() {
      if($scope.source) { // edit
        alert("TODO: save changes");
      } else { // create
        var s = $scope.data.crud.source;
        restService.createChanlelogSource(s.name, s.description, s.url, s.sourceName.id).then(
          function success() {
            gToast.open("The changelog source has been successfully created.");
            $location.path("/changelog/source/list");
          },
          function failure(errorResponse) {
            restService.error("Could not create a new changelog source.", errorResponse);
          }
        );
      }
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
      restService.changelogSourceUploadAttachmentTmp(file, file.name, $scope.data.attachDescr).then(
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

  }

]);

