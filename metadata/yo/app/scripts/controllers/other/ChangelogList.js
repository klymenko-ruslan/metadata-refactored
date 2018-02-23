'use strict';

/* globals jsondiffpatch:false */

angular.module('ngMetaCrudApp')
  .controller('ChangelogListCtrl', ['$scope', '$log', 'NgTableParams', '$uibModal', 'restService',
  'users', function(
    $scope, $log, NgTableParams, $uibModal, restService, users) {

    $scope.tabs = {
        activeIndex: 0
    };

    $scope.users = _.chain(users)
        .map(function(u) {
          return {
            id: u.id,
            label: u.name
          };
        })
        .sortBy('label')
        .value();

    $scope.services = _.sortBy([
      {
        id: 'BOM',
        label: 'BOM'
      }, {
        id: 'INTERCHANGE',
        label: 'INTERCHANGE'
      }, {
        id: 'MAS90SYNC',
        label: 'MAS90SYNC'
      }, {
        id: 'SALESNOTES',
        label: 'SALESNOTES'
      }, {
        id: 'APPLICATIONS',
        label: 'APPLICATIONS'
      }, {
        id: 'KIT',
        label: 'KIT'
      }, {
        id: 'PART',
        label: 'PART'
      }, {
        id: 'TURBOMODEL',
        label: 'TURBOMODEL'
      }, {
        id: 'TURBOTYPE',
        label: 'TURBOTYPE'
      }, {
        id: 'CRITICALDIM',
        label: 'CRITICALDIM'
      }, {
        id: 'IMAGE',
        label: 'IMAGE'
      }
    ], 'label');

    var dateFormat = 'YYYY-MM-DD';

    $scope.datePickerOptions = {
      locale: {
        format: dateFormat
      },
      ranges: {
        'Today': [moment(), moment()],
        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        'This Week': [moment().startOf('week'), moment().endOf('week')],
        'Last Week': [moment().subtract(1, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
        'This Month': [moment().startOf('month'), moment().endOf('month')],
        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
        'This Year': [moment().startOf('year'), moment().endOf('year')],
        'Last Year': [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
      }
    };

    $scope.changelogTableParamsLoading = true;
    $scope.changelogTableParams = null;

    $scope.aggregationTableParamsLoading = true;
    var changelogAggregation = null;
    $scope.changelogAggregationTableParams = new NgTableParams({
      page: 1,
      count: 10,
      sorting: {
        'user.name': 'asc'
      }
    }, {
      dataset: changelogAggregation
    });

    // Query Parameters
    $scope.search = {
      'date': { startDate: null, endDate: null },
      'services': [],
      'users': [],
      'description': null,
      'data': null
    };

    $scope.onChangeTab = function(tabId) {
      if (tabId === 'changelog_tab_facts') {
        if ($scope.changelogTableParams === null) {
          refreshTabFacts();
        }
      } else if (tabId === 'changelog_tab_aggregation') {
        if ($scope.changelogAggregation === null) {
          refreshTabAggregation();
        }
      }
    };

    function refreshTabFacts() {
      $scope.changelogTableParamsLoading = true;
      $scope.changelogTableParams = new NgTableParams({
        page: 1,
        count: 10,
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
          var startDate = null;
          if ($scope.search.date.startDate != null) {
            startDate = moment($scope.search.date.startDate).format(dateFormat);
          }
          var endDate = null;
          if ($scope.search.date.endDate != null) {
            endDate = moment($scope.search.date.endDate).format(dateFormat);
          }
          var selectedServiceIds = _.map($scope.search.services, function(s) { return s.id; });
          var userIds = _.map($scope.search.users, function(u) { return u.id; });
          return restService.filterChangelog(startDate, endDate,
            selectedServiceIds, userIds, $scope.search.description, $scope.search.data, null,
            sortProperty, sortOrder, offset, limit).then(
              function success(result) {
                // Update the total and slice the result.
                params.total(result.total);
                return result.recs;
              },
              function failure(errorResponse) {
                restService.error('Search in the changelog failed.', errorResponse);
              }
            ).finally(function() {
              $scope.changelogTableParamsLoading = false;
            });
        }
      });
    }

    refreshTabFacts();

    function refreshTabAggregation() {
      $scope.aggregationTableParamsLoading = true;
      var startDate = null;
      if ($scope.search.date.startDate != null) {
        startDate = moment($scope.search.date.startDate).format(dateFormat);
      }
      var endDate = null;
      if ($scope.search.date.endDate != null) {
        endDate = moment($scope.search.date.endDate).format(dateFormat);
      }
      var selectedServiceIds = _.map($scope.search.services, function(s) { return s.id; });
      var userIds = _.map($scope.search.users, function(u) { return u.id; });
      restService.filterChangelogAggregation(startDate, endDate, selectedServiceIds,
          userIds, $scope.search.description, $scope.search.data)
        .then(
          function success(changelogAggregation) {
            $scope.changelogAggregationTableParams.settings({dataset: changelogAggregation});
          },
          function failure(errorResponse) {
            restService.error('Changelog aggregation failed.', errorResponse);
          }
        ).finally(function() {
          $scope.aggregationTableParamsLoading = false;
        });


    }

    $scope.applyFilter = function() {
      if ($scope.tabs.activeIndex === 0) {
        $scope.changelogAggregation = null;
        refreshTabFacts();
      } else if ($scope.tabs.activeIndex === 1) {
        $scope.changelogTableParams = null;
        refreshTabAggregation();
      }
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
