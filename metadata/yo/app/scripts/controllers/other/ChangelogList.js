'use strict';

/* globals jsondiffpatch:false */

angular.module('ngMetaCrudApp')
  .controller('ChangelogListCtrl', ['$scope', '$log', 'NgTableParams', '$uibModal', 'restService', 'users',
    'DATE_FORMAT', function(
    $scope, $log, NgTableParams, $uibModal, restService, users, DATE_FORMAT) {

    $scope.dateFormat = DATE_FORMAT;

    $scope.users = users;

    $scope.opened = {
      startDate: false,
      finishDate: false
    };
    $scope.openStartDateCalendar = function() {
      $scope.opened.startDate = true;
    };

    $scope.openFinishDateCalendar = function() {
      $scope.opened.finishDate = true;
    };

    $scope.datePickerOptions = {
      dateDisabled: false,
      startingDay: 1
    };

    $scope.changelogTableParams = new NgTableParams({
      page: 1,
      count: 25,
      sorting: {
        changeDate: 'desc'
      }
    }, {
      getData: function(params) {
        var sortOrder;
        var sorting = params.sorting();
        for (var sortProperty in sorting) {
            break;
        }
        if (sortProperty) {
          sortOrder = sorting[sortProperty];
        }
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();
        var userId = null;
        if (angular.isObject($scope.search.user)) {
          userId = $scope.search.user.id;
        }
        return restService.filterChangelog($scope.search.startDate, $scope.search.finishDate,
          $scope.search.service, userId, $scope.search.description, $scope.search.data, null,
          sortProperty, sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            params.total(result.total);
            return result.recs;
          },
          function(errorResponse) {
            restService.error('Search in the changelog failed.', errorResponse);
          });
      }
    });

    // Query Parameters
    $scope.search = {
      'startDate': null,
      'finishDate': null,
      'service': null,
      'user': null,
      'description': null,
      'data': null
    };

    $scope.applyFilter = function() {
      $scope.changelogTableParams.reload();
    };

    $scope.onOpenViewDlg = function(changelogRecord) {
      $uibModal.open({
        templateUrl: '/views/changelog/view.html',
        animation: false,
        size: 'lg',
        controller: 'ChangelogViewDlgCtrl',
        resolve: {
          changelogRecord: function() {
            return changelogRecord;
          },
          changelogSourceLink: ['restService', function(restService) {
            return restService.findChangelogSourceLinkByChangelogId(changelogRecord.id);
          }]
        }
      });
    };

  }])
  .controller('ChangelogViewDlgCtrl', ['$scope', '$log', '$location', 'NgTableParams', '$uibModalInstance', 'changelogRecord', 'changelogSourceLink',
    function($scope, $log, $location, NgTableParams, $uibModalInstance,  changelogRecord, changelogSourceLink) {
      $scope.readonly = true;
      $scope.date = changelogRecord.changeDate;
      $scope.user = changelogRecord.user;
      $scope.description = changelogRecord.description;
      $scope.changes = null;
      $scope.changelogSourceLink = changelogSourceLink;
      if (changelogSourceLink && changelogSourceLink.changelogSources) {
        $scope.changelogSources = changelogSourceLink.changelogSources;
      } else {
        $scope.changelogSources = [];
      }

      $scope.changelogSourcesTableParams = new NgTableParams(
        {
          page: 1,
          count: 10,
          sorting: {}
        },
        {
          dataset: $scope.changelogSources
        }
      );

      if (changelogRecord && changelogRecord.data !== undefined && changelogRecord.data !== null) {
        var data = changelogRecord.data;
        try {
          $scope.changes = angular.fromJson(data);
        } catch (e) {
          var patched = data.replace(/(['"])?([a-zA-Z0-9_]+)(['"])?:/g, '"$2": ');
          try {
            $scope.changes = angular.fromJson(patched);
          } catch(e) {
            $log.log('Bad data of a changelog record [' + changelogRecord.id + ']: ' + e);
            $scope.changes = data; // string
          }
        }
      }

      $scope.showChanges = {
        as: 'raw'
      };
      var hasDiff = $scope.changes && typeof($scope.changes) === 'object' &&
        $scope.changes.hasOwnProperty('original') &&
        $scope.changes.hasOwnProperty('updated');
      if (hasDiff) {
        var original = $scope.changes.original;
        var updated = $scope.changes.updated;
        var delta = jsondiffpatch.create({
          objectHash: function(obj, index) {
            return obj.name || obj.id || obj.partId || obj._id || '$$index:' + index;
          },
          arrays: {
            detectMove: true
          }
        }).diff(original, updated);
        jsondiffpatch.formatters.html.hideUnchanged();
        $scope.diffHtml = jsondiffpatch.formatters.html.format(delta, original);
      }

      $scope.onSourceView = function(srcId) {
        $uibModalInstance.close();
        $location.path('/changelog/source/' + srcId);
      };

      $scope.onUserView = function(id) {
        $uibModalInstance.close();
        $location.path('/security/user/' + id);
      };

      $scope.onCloseViewDlg = function() {
        $uibModalInstance.close();
      };

  }]);
