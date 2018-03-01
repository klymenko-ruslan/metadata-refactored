'use strict';

angular.module('ngMetaCrudApp')

.service('LinkSource', ['$log', 'dialogs', '$uibModal', 'restService', 'User',
  function($log, dialogs, $uibModal, restService, User) {

    this.link = function(cbSave, requiredSource, cancelUrl) {
      if (requiredSource) {
        var authorized = User.hasRole('ROLE_CHLOGSRC_READ') &&
              User.hasRole('ROLE_CHLOGSRCNAME_READ');
        if (authorized) {
          $uibModal.open({
            templateUrl: '/views/chlogsrc/LinkDlg.html',
            animation: false,
            size: 'lg',
            controller: 'ChlogSrcLinkDlgCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
              'cbSave': function () {
                return cbSave;
              },
              'sourcesNames': restService.getAllChangelogSourceNames(),
              'lastPicked': restService.getLastPickedChangelogSources,
              'begin': function() {
                // needs to clear session attribute on the server side
                return restService.changelogSourceBeginEdit();
              },
              'cancelUrl': function() {
                return cancelUrl;
              }
            }
          });
        } else {
          dialogs.error('Not authorized', 'To complete this operation you ' +
              'must have at least following roles: ' +
              'ROLE_CHLOGSRC_READ, ROLE_CHLOGSRCNAME_READ.');
        }
      } else {
        cbSave(null, null, null, null);
      }
    };

    function _isSourceRequired(services, serviceName) {
      var srv = _.find(services, function(s) {
          return s.name === serviceName;
      });
      if (!srv) {
        throw 'Service "' + serviceName + '" not found.';
      }
      return srv.requiredSource;
    }

    this.isSourceRequiredForApplication = function(services) {
      return _isSourceRequired(services, 'APPLICATIONS');
    };

    this.isSourceRequiredForBOM = function(services) {
      return _isSourceRequired(services, 'BOM');
    };

    this.isSourceRequiredForInterchange = function(services) {
      return _isSourceRequired(services, 'INTERCHANGE');
    };

    this.isSourceRequiredForPart = function(services) {
      return _isSourceRequired(services, 'PART');
    };

    this.isSourceRequiredForSalesNote = function(services) {
      return _isSourceRequired(services, 'SALESNOTES');
    };

    return this;

  }
])
.controller('ChlogSrcLinkDlgCtrl', ['$scope', '$log', '$location', 'dialogs',
  'toastr', 'NgTableParams', '$uibModalInstance', 'restService',
  'cbSave', 'sourcesNames', 'lastPicked', 'User', 'cancelUrl', 'begin',
  function($scope, $log, $location, dialogs, toastr, NgTableParams,
    $uibModalInstance, restService, cbSave, sourcesNames, lastPicked,
    User, cancelUrl, begin)
  { // injection "begin" is important
    begin = null; // dummy statement to avoid jshint
                  // complain about unused var begin

    var ATTR_UPLOAD_ID = 'upload_id';

    $scope.sourcesNames = sourcesNames;

    var userMustLink = !User.hasRole('ROLE_CHLOGSRC_SKIP');
    var canCreateSource = User.hasRole('ROLE_CHLOGSRC_CREATE');
    var canCreateSourceName = User.hasRole('ROLE_CHLOGSRCNAME_CREATE');

    var pickedSources = null;
    var pickedSourceIds = null;

    $scope.pickedSourcesRatings = null;

    var sourceAttachments = null;

    var file = null;

    // Data to be uploaded
    var formData = null;

    $scope.fltrSource = null;

    $scope.data = null;

    var markdown, dropzone;
    var baseUrl = $location.protocol() + '://' + $location.host() + ':' +
      $location.port();

    function onClickLinkBttn(uploadId, file, link) {

      if (!file || file.status !== Dropzone.SUCCESS) {
        return;
      }
      var e = markdown;
      var chunk, cursor, selected = e.getSelection();
      if (selected.length === 0) {
        chunk = file.name;
      } else {
        chunk = selected.text;
      }
      var sanitizedLink = $('<div>' + link + '</div>').text();
      if ((/\.(gif|jpg|jpeg|tiff|png)$/i).test(file.name)) {
        e.replaceSelection('![' + chunk + '](' + sanitizedLink + ' "' +
          chunk + '")');
      } else {
        e.replaceSelection('[' + chunk + '](' + sanitizedLink + ')');
      }
      cursor = selected.start + 1;
      e.setSelection(cursor, cursor + chunk.length);
      markdown.change(markdown);
    }

    //var uploadPreviewTemplate = document.getElementById('upload-preview-template');

    $scope.markdownEditorOpts = {
      iconlibrary: 'fa',
      addExtraButtons: true,
      resize: 'vertical',
      fullscreen: {enable: false},
      hiddenButtons: 'Preview',
      dropZoneOptions: {
        url: '/metadata/changelogsourcelink/description/attachment/upload',
        maxFilesize:20,
        createImageThumbnails: false,
        maxFiles: 100,
        parallelUploads: 1,
        previewsContainer: '#descriptionUploads',
        previewTemplate: document.getElementById('upload-preview-template')
          .innerHTML,
        clickable: false,
        autoProcessQueue: true,
        init: function() {
          dropzone = this;
          markdown = $('#descriptionEditor').data('markdown');
          jQuery('body').fileClipboard({
            accept: 'image/*',
            on: {
              load: function(e, file) {
                dropzone.addFile(file);
              }
            }
          });

          this.on('removedfile', function(file) {
            var uploadId = $(file.previewElement).attr(ATTR_UPLOAD_ID);
            if (uploadId) {
              if (file.status === Dropzone.SUCCESS) {
                restService.removeChangelogSourceLinkDescriptionAttachment(
                  uploadId).then(
                  function success() {
                    // ignore
                  },
                  function failure(response) {
                    $log.log('Deletion of the attachment [' + response.id +
                      '] failed: ' + angular.toJson(response, 2));
                  }
                );
              }
            }
          });
          this.on('success', function(file, response) {
            var uploadId = response[0].id;
            var downloadLink = baseUrl + '/metadata/changelogsourcelink/' +
              'description/attachment/download/' + uploadId;
            $(file.previewElement).find('.dz-filename').first()
              .attr('href', downloadLink);
            $(file.previewElement).attr(ATTR_UPLOAD_ID, uploadId);
            $(file.previewElement).find('.btn-info').first().click(function() {
              onClickLinkBttn(uploadId, file, downloadLink);
            });
          });
          this.on('error', function(file, message) {
            $log.log('An error caught. File: ' + angular.toJson(file, 2) +
              '\nmessage: ' + message);
          });
        }
      }
    };

    function _reset() {
      pickedSources = [];
      $scope.pickedSourcesRatings = [];
      pickedSourceIds = {};
      sourceAttachments = [];

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
    }

    _reset();

    $scope.lastPickedTableParams = new NgTableParams(
      {
        page: 1,
        count: 5,
        sorting: {
          lastLinked: 'desc'
        }
      },
      {
        counts: [5, 10, 15],
        dataset: lastPicked
      }
    );

    $scope.pickedSourcesTableParams = new NgTableParams(
      {
        page: 1,
        count: 5,
        sorting: {}
      }, {
        counts: [5, 10, 15],
        dataset: pickedSources
      }
    );

    $scope.sourceTableParams = new NgTableParams(
      {
        page: 1,
        count: 10,
        sorting: {
          'name.lower_case_sort': 'asc'
        }
      },
      {
        getData: function (params) {
          // Update the pagination info
          var offset = params.count() * (params.page() - 1);
          var limit = params.count();
          var sortProperty, sortOrder;
          for (sortProperty in params.sorting()) {
              break;
          }
          if (sortProperty) {
            sortOrder = params.sorting()[sortProperty];
          }
          var snid = null;
          if ($scope.fltrSource.sourceName) {
            snid = $scope.fltrSource.sourceName.id;
          }
          return restService.filterChangelogSource($scope.fltrSource.name,
            $scope.fltrSource.description, $scope.fltrSource.url, snid,
            sortProperty, sortOrder, offset, limit)
          .then(
            function (filtered) {
              // Update the total and slice the result
              params.total(filtered.hits.total);
              return filtered.hits.hits;
            },
            function () {
              $log.log('Couldn\'t search for "changelog source".');
            }
          );
        }
      }
    );

    $scope.sourceAttachmentsTableParams = new NgTableParams(
      {
        page: 1,
        count: 10,
        sorting: {}
      }, {
        dataset: sourceAttachments
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

      var attachIds = $('#descriptionUploads').find('li')
        .map(function() {return $(this).attr(ATTR_UPLOAD_ID); }).get();

      cbSave(srcIds, ratings, $scope.data.description, attachIds);

    }

    function _chvw(newViewId) {
      angular.copy($scope.data.currVw, $scope.data.prevVw);
      $scope.data.currVw.id = newViewId;
      if (newViewId === 'sources_list') {
        $scope.data.currVw.title = 'Pick One or More Sources';
        $scope.data.currVw.actionBttnTitle = 'Save';
      } else if (newViewId === 'create_new_source') {
        $scope.data.currVw.title = 'Link source >> Create New Source';
        $scope.data.currVw.actionBttnTitle = 'Create';
      } else if (newViewId === 'create_source_name') {
        $scope.data.currVw.title = 'Link source >> Create New Source >> ' +
          'Create New Source Name';
        $scope.data.currVw.actionBttnTitle = 'Create';
      } else {
        throw 'Unknown view id: ' + angular.toJson(newViewId);
      }
    }

    function _cleanCreateSourceForm() {
      $scope.data.crud.source = {}; // clean form
      sourceAttachments.splice(0, sourceAttachments.length);
      $scope.data.attachDescr = null;
      formData.delete('file');
    }

    function _createSource() {
      var s = $scope.data.crud.source;
      restService.createChangelogSource(s.name, s.description, s.url,
        s.sourceName.id).then(
        function success(newSource) {
          _chvw('sources_list');
          $scope.pick(newSource);
          $scope.sourceTableParams.reload();
        },
        function failure(errorResponse) {
          $uibModalInstance.close();
          restService.error('Could not create a new changelog source.',
            errorResponse);
        }
      );
    }

    function _createSourceName() {

      restService.createChangeSourceName($scope.data.newSourceName).then(
        function success(newSourceName) {
          toastr.success('The source name has successfully been created.');
          $scope.data.newSourceName = null;

          $scope.sourcesNames.push(newSourceName);
          $scope.sourcesNames.sort(function(a, b) {
            var nameA = a.name.toLowerCase();
            var nameB = b.name.toLowerCase();
            return (nameA < nameB ? -1 :(nameA > nameB ? 1 : 0));
          });
          $scope.data.crud.source.sourceName = newSourceName;

          _chvw('create_new_source');
        },
        function (errorResponse) {
          restService.error('Could not create a new source name: ' +
            $scope.data.newName, errorResponse);
        }
      );

    }

    $scope.isActionBttnDisabled = function () {
      var retval = true;
      if ($scope.data.currVw.id === 'sources_list') {
        retval = pickedSources.length === 0 || dropzone.getQueuedFiles()
          .length;
      } else if ($scope.data.currVw.id === 'create_new_source' &&
        $scope.forms.changelogSourceForm)
      {
        retval = $scope.forms.changelogSourceForm.$invalid || !canCreateSource;
      } else if ($scope.data.currVw.id === 'create_source_name' &&
        $scope.forms.newSourceName)
      {
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
      return $scope.data.currVw.id === 'sources_list';
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
      $scope.pickedSourcesTableParams.settings({dataset: pickedSources});
    };

    $scope.unpick = function(srcId) {
      var idx = _.findIndex(pickedSources, function(s) {
        return s.id === srcId;
      });
      pickedSources.splice(idx, 1);
      $scope.pickedSourcesRatings.splice(idx, 1);
      delete pickedSourceIds[srcId];
      $scope.pickedSourcesTableParams.settings({dataset: pickedSources});
    };

    $scope.unpickAll = function() {
      _.each(pickedSources, function(ps) {
        delete pickedSourceIds[ps.id];
      });
      pickedSources.splice(0, pickedSources.length);
      $scope.pickedSourcesRatings.splice(0, $scope.pickedSourcesRatings
        .length);
      $scope.pickedSourcesTableParams.settings({dataset: pickedSources});
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
      _chvw('create_new_source');
    };

    $scope.onCreateSourceName = function() {
       $scope.data.newSourceName = null;
      _chvw('create_source_name');
    };

    $scope.isUploadBttnDisabled = function () {
      return !formData.has('file');
    };

    $scope.changedAttachment = function(files) {
      $scope.$apply(function() {
        file = files[0];
        formData.append('file', files[0]);
      });
    };

    function _updateSourceAttachmentsTable(updatedSourceAttachments) {
      sourceAttachments.splice(0, sourceAttachments.length);
      _.each(updatedSourceAttachments, function (e) {
        sourceAttachments.push(e);
      });
      $scope.sourceAttachmentsTableParams.settings({dataset: sourceAttachments});
    }

    $scope.uploadSourceAttachment = function() {
      restService.changelogSourceUploadAttachmentTmp(file, file.name,
        $scope.data.attachDescr)
        .then(
          function(updatedAttachmentsResponse) {
            // Success
           _updateSourceAttachmentsTable(updatedAttachmentsResponse.rows);
            toastr.success('File uploaded.');
            $scope.data.attachDescr = null;
            formData.delete('file');
          },
          function(response) {
            // Error
            $log.log('Could not upload the attachment.', response);
          }
        );
    };

    $scope.removeSourceAttachment = function (id) {
      restService.changelogSourceRemoveAttachmentTmp(id).then(
        function(updatedAttachmentsResponse) {
          _updateSourceAttachmentsTable(updatedAttachmentsResponse.rows);
        },
        function(errorResponse) {
          restService.error('Could not remove attachment.', errorResponse);
        }
      );
    };

    $scope.cancel = function() {
      var cv = $scope.data.currVw.id;
      if (cv === 'sources_list') {
        $uibModalInstance.close();
        if (cancelUrl) {
          $location.path(cancelUrl);
        }
      } else if (cv === 'create_new_source') {
        _chvw('sources_list');
      } else if (cv === 'create_source_name') {
        $scope.data.newSourceName = null;
        _chvw('create_new_source');
      } else if (cv === 'confirm_cancel') {
        _chvw($scope.data.prevVw.id); // return to a previous view
      } else {
        throw 'Unknown current view [1]: ' + angular.toJson(cv);
      }
    };

    $scope.action = function() {
      var cv = $scope.data.currVw.id;
      if (cv === 'sources_list') {
        $uibModalInstance.close();
        _save(false);
      } else if (cv === 'create_new_source') {
        _createSource();
      } else if (cv === 'create_source_name') {
        _createSourceName();
      } else {
        throw 'Unknown current view [2]: ' + angular.toJson(cv);
      }
    };

    // Handle updating search results
    $scope.$watch('[fltrSource]',
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
    _chvw('sources_list');

  }
]).directive('uniqueChangelogSourceByName', ['$log', '$q', 'restService',
  function($log, $q, restService) {
    // Validator for uniqueness of the changelog source name.
    return {
      require: 'ngModel',
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
                var id = $scope.$eval('source.id');
                if (changelogSource.id === id) {
                  def.resolve();
                } else {
                  def.reject();
                }
              }
            },
            function () {
              $log.log('Couldn\'t validate name of the changelog source: ' +
                viewValue);
              def.reject();
            }
          );
          return def.promise;
        };
      }
    };
  }
]);
