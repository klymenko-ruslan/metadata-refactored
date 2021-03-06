'use strict';

angular.module('ngMetaCrudApp')
.controller('PartDetailCtrl', ['$scope', '$log', '$q', '$location', '$cookies', '$routeParams',
    'NgTableParams', 'restService', 'Restangular', 'User', '$uibModal', 'dialogs', 'toastr',
    'part', 'cachedDictionaries', 'partTypes', 'manufacturers',
    function ($scope, $log, $q, $location, $cookies, $routeParams, NgTableParams,
      restService, Restangular, User, $uibModal, dialogs, toastr, part,
      cachedDictionaries, partTypes, manufacturers) {
  $scope.partId = part.id;
  $scope.part = part;
  $scope.partTypeOpts = _.map(partTypes, function (pt) {
    return {'id': pt.value, 'title': pt.name};
  });
  $scope.formMode = 'view';
  $scope.criticalDimensions = null;
  cachedDictionaries.getCriticalDimensionsForPartId(part.partType.id).then(function(cdms) {
    $scope.criticalDimensions = cdms;
  });
  $scope.restService = restService;
  // Make sure we're using the correct part type
  $scope.partType = part.partType.name;

  $scope.activeTabIndex = 0;

  $scope.images = {
    pgNum: 1,
    pgSzVal: 2,
  };

  $scope.images.pgSzTxt = $cookies.get('pagedatails.imgpgsz') || 'two';

  function _imgPgSzTxt2Val(txt) {
    var retval = $scope.part.productImages.length; // all
    if (txt === 'one') {
      retval = 1;
    } else if (txt === 'two') {
      retval = 2;
    } else if (txt === 'three') {
      retval = 3;
    } else if (txt === 'four') {
      retval = 4;
    }
    return retval;
  }

  $scope.images.pgSzVal = _imgPgSzTxt2Val($scope.images.pgSzTxt);

  $scope.onReindex = function() {
    restService.indexPartSync($scope.partId).then(
      function success() {
        toastr.success('The part has been successfully (re)indexed.');
      },
      function failure(error) {
        restService.error('The request to reindex the part failed.', error);
      }
    );
  };

  $scope.onChangeTab = function(tabId) {
    if (tabId === 'part_details') {
      $scope.refreshTabPartDetails();
    } else if (tabId === 'kits') {
      if ($scope.kitCommonTurboTypesTableParams === null || $scope.kitCommonComponentMappingTableParams === null) {
        $scope.refreshTabKits();
      }
    } else if (tabId === 'non_standard') {
      if ($scope.oversizeParts === null && $scope.standardParts === null) {
        $scope.refreshTabNonStandard();
      }
    } else if (tabId === 'audit_log') {
      if ($scope.changelogTableParams === null) {
        $scope.refreshTabAuditLog();
      }
    } else if (tabId === 'prices') {
      if ($scope.prices === null) {
        $scope.refreshTabPrices();
      }
    } else if (tabId === 'turbos') {
      if ($scope.turbos === null) {
        $scope.refreshTabTurbos();
      }
    } else if (tabId === 'also_bought') {
      if ($scope.alsoBoughtTableParams === null) {
        $scope.refreshTabAlsoBought();
      }
    } else if (tabId === 'applications') {
      if ($scope.applications === null) {
        $scope.refreshTabApplications();
      }
    }
  };

  $scope.refreshTabPartDetails = function() {
  };

  $scope.kitCommonTurboTypesLoading = true;
  $scope.kitCommonTurboTypesMapping = null;
  $scope.kitCommonTurboTypesTableParams = null;

  $scope.kitCommonComponentMappingLoading = true;
  $scope.kitCommonComponentMapping = null;
  $scope.kitCommonComponentMappingTableParams = null;

  function _createKitCommonComponentMapping(kitId, exclude) {
    restService.createKitComponent($scope.partId, kitId, exclude).then(
      function success(createResponse) {
        if (createResponse.failure) {
          dialogs.error(
            'Creation of Common Component Mapping failed.',
            'Error message from the storage:\n\n' +
            createResponse.errorMessage).result.then(
              function yes() {
                // ignore
              },
              function no() {
                // ignore
              }
            );
        } else {
          var newCommonComponent = createResponse.commonComponent;
          var mapId = newCommonComponent.id;
          var itm = _.find($scope.kitCommonTurboTypesMapping,
            function(r) { return r.kit.partId === kitId; }
          );
          itm.id = mapId;
          itm.exclude = exclude;
          $scope.kitCommonComponentMapping.push(itm);
          _updateKitCommonTurboTypesRows(mapId, exclude);
          _updateKitCommonComponentRows(mapId, exclude);
        }
      },
      function failure(errorResponse) {
        restService.error('Creation of a kit common component mapping for the Kit [' + kitId +
          '] failed.', errorResponse);
      }
    );
  }

  function _updateKitCommonTurboTypesRows(mapId, exclude) {
    var itm = _.find($scope.kitCommonTurboTypesMapping,
      function(r) { return r.id === mapId; }
    );
    itm.exclude = exclude;
    // We forced to reload all table because it seem that NgTable has a bug
    // and don't reflect changes in a dataset if it is displayed on a page
    // other than first one.
    var pg = $scope.kitCommonTurboTypesTableParams.page();
    $scope.kitCommonTurboTypesTableParams.settings({dataset: $scope.kitCommonTurboTypesMapping});
    $scope.kitCommonTurboTypesTableParams.page(pg);
    return itm;
  }

  function _updateKitCommonComponentRows(mapId, exclude) {
    var itm = _.find($scope.kitCommonComponentMapping,
      function(r) { return r.id === mapId; }
    );
    itm.exclude = exclude;
    // We forced to reload all table because it seem that NgTable has a bug
    // and don't reflect changes in a dataset if it is displayed on a page
    // other than first one.
    var pg = $scope.kitCommonComponentMappingTableParams.page();
    $scope.kitCommonComponentMappingTableParams.settings({dataset: $scope.kitCommonComponentMapping});
    $scope.kitCommonComponentMappingTableParams.page(pg);
    return itm;
  }

  function _updateKitCommonComponentMapping(mapId, exclude) {
    restService.updateKitComponent(mapId, exclude).then(
      function success(updateResponse) {
        if (updateResponse.failure) {
          dialogs.error(
            'Update of Common Component Mapping failed.',
            'Error message from the storage:\n\n' +
            updateResponse.errorMessage).result.then(
              function yes() {
                // ignore
              },
              function no() {
                // ignore
              }
            );
        } else {
          _updateKitCommonComponentRows(mapId, exclude);
          _updateKitCommonTurboTypesRows(mapId, exclude);
        }
      },
      function failure(errorResponse) {
        restService.error('Update of the kit component mapping [' + mapId +
          '] failed.', errorResponse);
      }
    );
  }

  $scope.kitCommonComponentMappingInclude = function(kccmId) {
    _updateKitCommonComponentMapping(kccmId, false);
  };

  $scope.kitCommonComponentMappingExclude = function(kccmId) {
    _updateKitCommonComponentMapping(kccmId, true);
  };

  $scope.kitCommonTurboTypesMappingInclude = function(kctt) {
    var mapId = kctt.id;
    var kitId = kctt.kit.partId;
    if (mapId) { // Common Component Mapping already exists.
      _updateKitCommonComponentMapping(mapId, false);
    } else {
      _createKitCommonComponentMapping(kitId, false);
    }
  };

  $scope.kitCommonTurboTypesMappingExclude = function(kctt) {
    var mapId = kctt.id;
    var kitId = kctt.kit.partId;
    if (mapId) { // Common Component Mapping already exists.
      _updateKitCommonComponentMapping(mapId, true);
    } else {
      _createKitCommonComponentMapping(kitId, true);
    }
  };

  function _initKitCommonComponentMapping(kitCommonComponentMapping) {
    $scope.selectedItems.kccm.allChecked = false;
    $scope.selectedItems.kccm.maps = {};
    $scope.kitCommonComponentMapping = kitCommonComponentMapping;
    $scope.kitCommonComponentMappingTableParams.settings({dataset: $scope.kitCommonComponentMapping});
  }

  $scope.$watch('selectedItems.kccm.allChecked', function(val) {
    if ($scope.kitCommonComponentMapping) {
      _.each($scope.kitCommonComponentMapping, function(kc) {
        $scope.selectedItems.kccm.maps[kc.id] = val;
      });
    }
  });

  $scope.isThereAnySelectedCommonComponentMapping = function() {
    var m = $scope.selectedItems.kccm.maps;
    if (m) {
      for (var k in m) {
        if (m.hasOwnProperty(k) && m[k]) {
          return true;
        }
      }
    }
    return false;
  };

  $scope.onRemoveSelectedCommonComponentsMappings = function() {
    dialogs.confirm(
      'Remove Common Component Mapping?',
      'Do you want to remove this common component mapping?').result.then(
      function() {
        // Yes
        var ids = _.chain(_.pairs($scope.selectedItems.kccm.maps))
          .filter(function(t) { return t[1]; })
          .map(function(t) { return parseInt(t[0]); })
          .value();
        restService.removeKitComponentsInPart($scope.partId, ids).then(
          function(result) {
            // Success
            toastr.success('Common Components Mapping(s) removed.');
            _initKitCommonComponentMapping(result);
            // If some removed common component mapping exists in
            // the tabe 'common turbo types' then updated
            // corresponding records.
            var mapId2recNum = _.chain($scope.kitCommonTurboTypesMapping)
              .map(function(e, index) {
                if (e.id !== null && e.id !== undefined) {
                  return [e.id, index];
                } else {
                  return null;
                }
              })
              .filter(function(e) {
                return e !== null; // filter out nulls
              })
              .object().value();
            _.each(ids, function(mapId) {
              var recNum = mapId2recNum[mapId.toString()];
              if (recNum !== undefined) {
                var mapping = $scope.kitCommonTurboTypesMapping[recNum];
                mapping.id = null;
                mapping.exclude = null;
              }
            });
            $scope.kitCommonTurboTypesTableParams.settings({dataset: $scope.kitCommonTurboTypesMapping});
          },
          function(response) {
            // Error
            restService.error('Could not delete common component mapping(s).', response);
          });
      },
      function() {
        // No
      });
  };

  $scope.refreshTabKits = function() {

    $scope.kitCommonTurboTypesLoading = true;
    restService.listKitCommonTurboTypes($scope.partId).then(
      function(result) {
        $scope.kitCommonTurboTypesLoading = false;
        $scope.kitCommonTurboTypesMapping = result;
        $scope.kitCommonTurboTypesTableParams.settings({dataset: $scope.kitCommonTurboTypesMapping});
      },
      function(errorResponse) {
        $scope.kitCommonTurboTypesLoading = false;
        restService.error('Loading of Kit\'s Common Turbo Types failed.', errorResponse);
      }
    );

    $scope.kitCommonTurboTypesTableParams = new NgTableParams({
      'page': 1,
      'count': 10,
      'sorting': {
        'kit.partNumber': 'asc'
      }
    }, {
      dataset: $scope.kitCommonTurboTypesMapping
    });

    $scope.kitCommonComponentMappingLoading = true;
    restService.listKitComponentsByPartId($scope.partId).then(
      function(result) {
        $scope.kitCommonComponentMappingLoading = false;
        _initKitCommonComponentMapping(result.recs);
      },
      function(errorResponse) {
        $scope.kitCommonComponentMappingLoading = false;
        restService.error('Loading of Kit\'s Common Component Mapping failed.', errorResponse);
      }
    );

    $scope.kitCommonComponentMappingLoading = true;
    $scope.kitCommonComponentMappingTableParams = new NgTableParams({
      'page': 1,
      'count': 10,
      'sorting': {
        'kit.partNumber': 'asc'
      }
    }, {
      dataset: $scope.kitCommonComponentMapping
    });

  };

  $scope.oversizeParts = null;
  $scope.oversizePartsTableParams = new NgTableParams({
    'page': 1,
    'count': 10,
    'sorting': {
      'manufacturerPartNumber': 'asc'
    }
  }, {
    'dataset': $scope.oversizeParts
  });
  $scope.oversizePartsTableParamsLoading = true; // to show icon for a progress of a loading

  $scope.standardParts = null;
  $scope.standardPartsTableParams = new NgTableParams({
    'page': 1,
    'count': 10,
    'sorting': {
      'manufacturerPartNumber': 'asc'
    }
  }, {
    'dataset': $scope.standardParts
  });
  $scope.standardPartsTableParamsLoading = true; // to show icon for a progress of a loading

  $scope.refreshTabNonStandard = function() {
    $scope.oversizePartsTableParamsLoading = true;
    restService.findOversizeParts($scope.partId).then(
      function success(oversizeParts) {
        $scope.oversizeParts = oversizeParts;
        $scope.oversizePartsTableParams.settings({dataset: $scope.oversizeParts});
        $scope.oversizePartsTableParamsLoading = false;
      },
      function failure(errorResponse) {
        $scope.oversizePartsTableParamsLoading = false;
        restService.error('Loading of oversize parts failed.', errorResponse);
      }
    );
    $scope.standardPartsTableParamsLoading = true;
    restService.findStandardParts($scope.partId).then(
      function success(standardParts) {
        $scope.standardParts = standardParts;
        $scope.standardPartsTableParams.settings({dataset: $scope.standardParts});
        $scope.standardPartsTableParamsLoading = false;
      },
      function failure(errorResponse) {
        $scope.standardPartsTableParamsLoading = false;
        restService.error('Loading of standard parts failed.', errorResponse);
      }
    );
  };

  $scope.changelogTableParams = null;
  $scope.changelogTableParamsLoading = true; // to show icon for a progress of a loading
  $scope.changelogRowsCount = null;

  $scope.refreshTabAuditLog = function() {
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
        // var userId = null;
        var retVal = restService.filterChangelog(null, null, null, null, null, null, $scope.partId,
        sortProperty, sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            params.total(result.total);
            $scope.changelogRowsCount = result.total;
            $scope.changelogTableParamsLoading = false;
            return result.recs;
          },
          function(errorResponse) {
            $scope.changelogTableParamsLoading = false;
            restService.error('Search in the changelog failed.', errorResponse);
          }
        );
        return retVal;
      }
    });
  };

  $scope.prices = null;
  $scope.pricesLoading = true; // to show icon for a progress of a loading

  $scope.refreshTabPrices = function() {
    $scope.pricesLoading = true;
    restService.getPartPrices($scope.partId).then(
      function success(prices) {
        $scope.prices = prices;
        $scope.pricesLoading = false;
      },
      function failure(error) {
        $scope.pricesLoading = false;
        restService.error('Loading pri the part failed.', error);
      }
    );
  };

  $scope.applications = null;
  $scope.applicationsTableParams = new NgTableParams({
    'page': 1,
    'count': 10,
    'sorting': {
    }
  }, {
    'dataset': $scope.applications
  });
  $scope.applicationsLoading = true; // to show icon for a progress of a loading

  $scope.refreshTabApplications = function() {
    $scope.applicationsLoading = true;
    restService.findPartApplications($scope.partId).then(
      function (applications) {
        $scope.applications = applications;
        $scope.applicationsTableParams.settings({dataset: $scope.applications});
        $scope.applicationsLoading = false;
      },
      function (errorResponse) {
        $scope.applicationsLoading = false;
        restService.error('Could not get part\'s applications', errorResponse);
      }
    );
  };

  $scope.removeApplication = function(app) {
    var applicationId = app.carModelEngineYear.id;
    dialogs.confirm(
      'Unlink Application Item',
      'Are you sure?').result.then(
        function yes() {
          restService.removePartApplication($scope.partId, applicationId).then(
            function success(applications) {
              $scope.applications = applications;
              $scope.applicationsTableParams.settings({dataset: $scope.applications});
              toastr.success('The applications has been successfully unlinked.');
            },
            function failure(errorResponse) {
              restService.error('Deletion of an application failed.', errorResponse);
            }
          );
        },
        function no() {
        });
  };

  $scope.alsoBoughtTableParams = null;
  $scope.alsoBoughtTableParamsLoading = true; // to show icon for a progress of a loading

  $scope.refreshTabAlsoBought = function() {
    $scope.alsoBoughtTableParamsLoading = true;
    $scope.alsoBoughtTableParams = new NgTableParams({
      page: 1,
      count: 25,
      sorting: {
        qtyShipped: 'desc'
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
        var filter = params.filter();
        return restService.filterAlsoBought($scope.part.manufacturerPartNumber,
                filter.manufacturerPartNumber, filter.partTypeValue, sortProperty, sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            $scope.alsoBoughtTableParamsLoading = false;
            if (result) {
              params.total(result.total);
              return result.recs;
            } else {
              params.total(0);
              return [];
            }
          },
          function(errorResponse) {
            $scope.alsoBoughtTableParamsLoading = false;
            restService.error('Loading of \'also bought\' failed.', errorResponse);
          }
        );
      }
    });
  };

  $scope.turbos = null;
  $scope.turbosTableParams = new NgTableParams({
    'page': 1,
    'count': 10,
    'sorting': { 'id': 'asc' }
  }, {
    'dataset': $scope.turbos
  });
  $scope.turbosTableParamsLoading = true; // to show icon for a progress of a loading

  $scope.refreshTabTurbos = function() {
    $scope.turbosTableParamsLoading = true;
    restService.listTurbosLinkedToGasketKit($scope.partId).then(
      function success(turbos) {
        $scope.turbos = turbos;
        $scope.turbosTableParamsLoading = false;
        $scope.turbosTableParams.settings({dataset: $scope.turbos});
      },
      function failure(errorResponse) {
        $scope.turbosTableParamsLoading = false;
        restService.error('Loading of \'also bought\' failed.', errorResponse);
      }
    );
  };

  $scope.selectedItems = {
    ccm: {
      allChecked: false,
      maps: {}
    },
    kccm: {
      allChecked: false,
      maps: {}
    }
  };

  function _initKitComponents(kitComponents) {
      $scope.selectedItems.ccm.allChecked = false;
      $scope.selectedItems.ccm.maps = {};
      $scope.kitComponents = kitComponents;
      $scope.kitComponentsTableParams.settings({dataset: $scope.kitComponents});
  }

  $scope.$watch('selectedItems.ccm.allChecked', function(val) {
    if ($scope.kitComponents) {
      _.each($scope.kitComponents, function(kc) {
        $scope.selectedItems.ccm.maps[kc.id] = val;
      });
    }
  });

  $scope.isThereAnySelectedKitComponentMapping = function() {
    var m = $scope.selectedItems.ccm.maps;
    if (m) {
      for (var k in m) {
        if (m.hasOwnProperty(k) && m[k]) {
          return true;
        }
      }
    }
    return false;
  };

  $scope.onRemoveSelectedKitComponentsMappings = function() {
    dialogs.confirm(
      'Remove Common Component Mapping?',
      'Do you want to remove this common component mapping from the kit?').result.then(
      function() {
        // Yes
        var ids = _.chain(_.pairs($scope.selectedItems.ccm.maps))
          .filter(function(t) { return t[1]; })
          .map(function(t) { return parseInt(t[0]); })
          .value();
        restService.removeKitComponentsInKit($scope.partId, ids).then(
          function(result) {
            // Success
            toastr.success('Common Components Mapping(s) removed.');
            _initKitComponents(result);
          },
          function(response) {
            // Error
            restService.error('Could not delete common component mapping(s).', response);
          });
      },
      function() {
        // No
      });
  };

  $scope.kitComponentsTableParams = null;
  $scope.kitComponents = null;
  if (part.partType.magentoAttributeSet === 'Kit') {
    $scope.kitComponentsTableParams = new NgTableParams({
      page: 1,
      count: 5,
      sorting: {
        'part.partNumber': 'asc'
      }
    }, {
      dataset: $scope.kitComponents
    });
    restService.listKitComponentsByKitId($scope.partId).then(
      function(result) {
        _initKitComponents(result.recs);
      },
      function(errorResponse) {
        restService.error('Can\'t load common component mappings.', errorResponse);
      }
    );
  }

  $scope.manufacturers = manufacturers;
  $scope.turboTypes = [];
  $scope.turboModels = [];

  $scope.turbo ={
    tm: null,
    tt: null
  };

  $scope.filters = {
    turboType: '',
    turboModel: ''
  };

  if ($scope.part.partType.magentoAttributeSet === 'Turbo') {
    $scope.turbo.tm = part.turboModel;
    $scope.turbo.tt = part.turboModel.turboType;
  }

  $scope.oldPart = Restangular.copy(part);

  $scope.onViewPart = function() {
    $location.path('/part/' + $scope.partId);
  };

  $scope.onChangeManufacturer = function() {
    if ($scope.part.partType.magentoAttributeSet === 'Turbo') {
      var mnfrId = $scope.part.manufacturer.id;
      restService.listTurboTypesForManufacturerId(mnfrId).then(
        function success(turboTypes) {
          $scope.turboTypes.splice(0, $scope.turboTypes.length);
          _.each(turboTypes, function(tt) {
            $scope.turboTypes.push(tt);
          });
        },
        function failure(response) {
          restService.error('Loading of turbo types for the manufacturer [' + mnfrId + '] - ' +
                            $scope.part.manufacturer.name + ' failed.', response);
        }
      );
    }
  };

  $scope.onChangeTurboType = function() {
    if ($scope.part.partType.magentoAttributeSet !== 'Turbo') {
      return;
    }
    var ttId = $scope.turbo.tt.id;
    if (ttId !== undefined) {
      restService.listTurboModelsForTurboTypeId(ttId).then(
        function success(turboModels) {
          $scope.turboModels.splice(0, $scope.turboModels.length);
          _.each(turboModels, function(tm) {
            $scope.turboModels.push(tm);
          });
        },
        function failure(response) {
          restService.error('Loading of turbo models for the turbo type [' + ttId + '] - ' +
                            $scope.part.turboModel.turboType.name + ' failed.', response);
        }
      );
    }
  };

  $scope.onChangeManufacturer();
  $scope.onChangeTurboType();

  $scope.revert = function() {
    $scope.part = Restangular.copy($scope.oldPart);
    // $scope.partForm.$setPristine(true);
    $scope.$broadcast('revert');
  };

  $scope.isManufacturerEnabled = function() {
    return User.hasRole('ROLE_ALTER_PART_MANUFACTURER');
  };

  $scope.isPnEnabled = function() {
    return User.hasRole('ROLE_ALTER_PART_NUMBER');
  };

  $scope.onEditSave = function() {

    //var url = 'part';

    if ($scope.part.partType.magentoAttributeSet === 'Turbo') {
      $scope.part.turboModel = $scope.turbo.tm;
      $scope.part.turboModel.turboType = $scope.turbo.tt;
    }
    restService.updatePartDetails($scope.part).then(
      function(part) {
        $scope.part = part;
        $scope.oldPart = Restangular.copy(part);
        _closeForm();
      },
      function(response) {
        restService.error('Could not update details of the part', response);
      }
    );

  };

  $scope.createTurboType = function() {
    $uibModal.open({
      templateUrl: '/views/part/TurboTypeCreateDlg.html',
      animation: false,
      size: 'lg',
      controller: 'CreateTurboTypeDlgCtrl',
      resolve: {
        create: function() {
          return true;
        },
        turbo: function() {
          return $scope.turbo;
        },
        part: function() {
          return $scope.part;
        },
        turboTypes: function() {
          return $scope.turboTypes;
        }
      }
    });
  };

  $scope.renameTurboType = function() {
    $uibModal.open({
      templateUrl: '/views/part/TurboTypeCreateDlg.html',
      animation: false,
      size: 'lg',
      controller: 'CreateTurboTypeDlgCtrl',
      resolve: {
        create: function() {
          return false;
        },
        turbo: function() {
          return $scope.turbo;
        },
        part: function() {
          return $scope.part;
        },
        turboTypes: function() {
          return $scope.turboTypes;
        }
      }
    });
  };

  $scope.deleteTurboType = function() {
    var ttId = $scope.turbo.tt.id;
    dialogs.confirm(
      'Delete Turbo Type?',
      'Do you want to delete this turbo type?').result.then(
      function() {
        // Yes
        restService.deleteTurboType(ttId).then(
          function() {
            // Success
            toastr.success('Turbo type deleted.');
            $scope.turbo.tt = {};
            var idx = _.findIndex($scope.turboTypes, function(tt) {
              return tt.id === ttId;
            });
            if (idx !== -1) {
              $scope.turboTypes.splice(idx, 1);
            }
          },
          function() {
            // Error
            dialogs.error(
              'Could not delete turbo type.',
              'Turbo type must not be used for any parts or turbo models. Check server log for details.');
          });
      },
      function() {
        // No
      }
    );
  };

  $scope.createTurboModel = function() {
    $uibModal.open({
      templateUrl: '/views/part/TurboModelCreateDlg.html',
      animation: false,
      size: 'lg',
      controller: 'CreateTurboModelDlgCtrl',
      resolve: {
        create: function() {
          return true;
        },
        turbo: function() {
          return $scope.turbo;
        },
        part: function() {
          return $scope.part;
        },
        turboModels: function() {
          return $scope.turboModels;
        }
      }
    });
  };

  $scope.renameTurboModel = function() {
    $uibModal.open({
      templateUrl: '/views/part/TurboModelCreateDlg.html',
      animation: false,
      size: 'lg',
      controller: 'CreateTurboModelDlgCtrl',
      resolve: {
        create: function() {
          return false;
        },
        turbo: function() {
          return $scope.turbo;
        },
        part: function() {
          return $scope.part;
        },
        turboModels: function() {
          return $scope.turboModels;
        }
      }
    });
  };

  $scope.deleteTurboModel = function() {
    var tmId = $scope.turbo.tm.id;
    dialogs.confirm(
      'Delete Turbo Model?',
      'Do you want to delete this turbo model?').result.then(
      function() {
        // Yes
        restService.deleteTurboModel(tmId).then(
          function() {
            // Success
            toastr.success('Turbo model deleted.');
            $scope.turbo.tm = null;
            var idx = _.findIndex($scope.turboModels, function(tm) {
              return tm.id === tmId;
            });
            if (idx !== -1) {
              $scope.turboModels.splice(idx, 1);
            }
          },
          function(/*response*/) {
            // Error
            dialogs.error(
              'Could not delete turbo model.',
              'Turbo model must not be used for any parts. Check server log for details.');
          });
      },
      function() {
          // No
      }
    );
  };

  $scope.onSetGasketKit = function() {
    $location.path('/part/' + $scope.partId + '/gasketkit/search');
  };

  $scope.onClearGasketKit = function() {
    dialogs.confirm(
      'Unlink the Gasket Kit?',
      'Do you want to unlink the Gasket Kit [' + $scope.part.gasketKit.id + '] - ' +
        $scope.part.gasketKit.manufacturerPartNumber + ' from this part [' +  $scope.part.id + '] - ' +
        $scope.part.manufacturerPartNumber + '?'
    ).result.then(
      function () {
        restService.clearGasketKitInPart($scope.partId).then(
          function success(updatedPart) {
            $scope.part = updatedPart;
            toastr.success('The gasket kit unlinked.');
          },
          function failure(result) {
            restService.error('Can\'t unlink the gasket kit.', result);
          }
        );
      },
      function () {
        // No
      }
    );
  };

  $scope.onClearTurbo = function(turboId, partNumber) {
    dialogs.confirm(
      'Unlink the Turbo?',
      'Do you want to unlink this Gasket Kit [' + $scope.part.id + '] - ' +
        $scope.part.manufacturerPartNumber + ' from the turbo [' +  turboId + '] - ' + partNumber + '?'
    ).result.then(
      function () {
        restService.unlinkTurboInGasketKit(turboId).then(
          function success(turbos) {
            toastr.success('The Gasket Kit and Turbo unlinked.');
            $scope.turbos = turbos;
            $scope.refreshTabTurbos();
          },
          function failure(result) {
            restService.error('Can\'t unlink the Gasket Kit and Turbo.', result);
          }
        );
      },
      function () {
        // No
      }
    );
  };

  $scope.onEditStart = function() {
    $scope.formMode = 'edit';
  };

  $scope.onEditCancel = function() {
    $scope.revert();
    _closeForm();
  };

  function _closeForm() {
    $scope.formMode = 'view';
  }

  $scope.onCreateXRef = function() {
    $uibModal.open({
      templateUrl: '/views/part/dialog/CreateXRef.html',
      animation: false,
      controller: 'CreateXRefDlgCtrl',
      resolve: {
        origPart: function() {
          return $scope.part;
        },
        manufacturers: function() {
          return $scope.manufacturers;
        }
      }
    });
  };

  $scope.onChangePartType = function() {
    $uibModal.open({
      templateUrl: '/views/part/dialog/parttypechange/wizard.html',
      animation: false,
      controller: 'partTypeChangeWizardCtrl',
      resolve: {
        part: function() {
          return $scope.part;
        },
        partTypes: function() {
          return partTypes;
        }
      }
    });
  };

  // Images

  $scope.deleteImage = function(image) {

    dialogs.confirm(
      'Delete image?',
      'Do you want to remove this image from the part?',
      {
        // Fake class-marker to locate this dialog in e2e tests.
        windowClass: 'remove-image-confirmation-window'
      }
    ).result.then(
        function() {
          // Yes
          restService.deleteProductImage(image.id).then(
              function() {
                // Success
                toastr.success('Image removed.');

                var idx = _.indexOf($scope.part.productImages, image);
                $scope.part.productImages.splice(idx, 1);
              },
              function(response) {
                // Error
                restService.error('Could not delete image.', response);
              });
        },
        function() {
          // No
        });

  };

  $scope.onChangePublishImage = function(image) {
    restService.publishProductImage(image.id, image.publish).then(
      function success() {
        var verb = image.publish ? 'published' : 'unpublished';
        toastr.success('The image has been ' + verb + '.');
      },
      function failure(result) {
        restService.error('(Un)Publish the image failed.', result);
        image.publish = !image.publish; // return previous state
      }
    );
  };

  $scope.onSetImageAsPrimary = function(image) {
    restService.setProductImageAsPrimary(image.id).then(
      function success() {
        toastr.success('The image has been set as primary.');
        _.each($scope.part.productImages, function (img) {
          img.main = (img.id === image.id);
        });
      },
      function failure(result) {
        restService.error('Set image as primary failed.', result);
        image.main = !image.publish; // return previous state
      }
    );
  };

  $scope.addImage = function() {
    dialogs.create(
      '/views/part/dialog/AddImage.html',
      'AddPartImageCtrl',
      {part: $scope.part}
    ).result.then(function(image) {
      $scope.part.productImages.push(image);
    });
  };

  $scope.onChangeImgPgSzTxt = function() {
    $cookies.put('pagedatails.imgpgsz', $scope.images.pgSzTxt);
    $scope.images.pgSzVal = _imgPgSzTxt2Val($scope.images.pgSzTxt);
  };

  $scope.onShowProductImage = function(imgId) {
    $uibModal.open({
      templateUrl: '/views/part/dialog/DisplayPartImages.html',
      animation: false,
      windowClass: 'part-img-modal-window',
      controller: 'DisplayPartImagesDlgCtrl',
      resolve: {
        imgId: function() {
          return imgId;
        },
        part: function() {
          return $scope.part;
        }
      }
    });
  };

  $scope.onOpenChangelogSourceViewDlg = function(changelogRecord) {
    $uibModal.open({
      templateUrl: '/views/part/dialog/DisplayChangelogSource.html',
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

  $scope.onDeleteOversizePart = function(oversizePart) {
    dialogs.confirm(
      'Delete Oversize Part?',
      'Do you want to delete this oversize part?').result.then(
      function() {
        // Yes
        restService.deleteStandardOversizePart($scope.partId, oversizePart.id).then(
          function() {
            // Success
            toastr.success('The oversize part has been deleted.');
            var idx = _.findIndex($scope.oversizeParts, function(op) {
              return op.id === oversizePart.id;
            });
            $scope.oversizeParts.splice(idx, 1);
            $scope.oversizePartsTableParams.settings({dataset: $scope.oversizeParts});
          },
          function(error) {
            // Error
            restService.error('Could not delete the oversize part.', error);
          }
        );
      },
      function() {
        // No
      }
    );
  };

  $scope.onDeleteStandardPart = function(standardPart) {
    dialogs.confirm(
      'Delete Standard Part?',
      'Do you want to delete this standard part?').result.then(
      function() {
        // Yes
        restService.deleteStandardOversizePart(standardPart.id, $scope.partId).then(
          function success() {
            // Success
            toastr.success('The standard part has been deleted.');
            var idx = _.findIndex($scope.standardParts, function(op) {
              return op.id === standardPart.id;
            });
            $scope.standardParts.splice(idx, 1);
            $scope.standardPartsTableParams.settings({dataset: $scope.standardParts});
          },
          function failure(result) {
            // Error
            restService.error('Could not delete the standard part.', result);
          });
      },
      function() {
        // No
      }
    );
  };

}])
.controller('DisplayPartImagesDlgCtrl', ['$scope', '$log', '$uibModalInstance', 'imgId', 'part',
  function($scope, $log, $uibModalInstance, imgId, part) {

    $scope.imgId = imgId;
    $scope.imgSize = '1000';
    $scope.part = part;

    $scope.onClose = function() {
      $uibModalInstance.close();
    };

  }
])
.controller('CreateXRefDlgCtrl', ['$scope', '$log', '$route', '$uibModalInstance', 'toastr', 'restService', 'origPart',
  'manufacturers',
  function($scope, $log, $route, $uibModalInstance, toastr, restService, origPart, manufacturers) {

    $scope.onChangeManufacturer = function() {
      if ($scope.part.partType.magentoAttributeSet === 'Turbo') {
        var mnfrId = $scope.$eval('part.manufacturer.id');
        if (mnfrId) {
          restService.listTurboTypesForManufacturerId(mnfrId).then(
            function success(turboTypes) {
              $scope.turboTypes.splice(0, $scope.turboTypes.length);
              _.each(turboTypes, function(tt) {
                $scope.turboTypes.push(tt);
              });
            },
            function failure(response) {
              restService.error('Loading of turbo types for the manufacturer [' + mnfrId + '] - ' +
                                $scope.part.manufacturer.name + ' failed.', response);
            }
          );
      	}
      }
    };

    $scope.onChangeTurboType = function() {
      if ($scope.part.partType.magentoAttributeSet !== 'Turbo') {
        return;
      }
      var ttId = $scope.$eval('turbo.tt.id');
      if (ttId) {
        restService.listTurboModelsForTurboTypeId(ttId).then(
          function success(turboModels) {
            $scope.turboModels.splice(0, $scope.turboModels.length);
            _.each(turboModels, function(tm) {
              $scope.turboModels.push(tm);
            });
          },
          function failure(response) {
            restService.error('Loading of turbo models for the turbo type [' + ttId + '] - ' +
                              $scope.part.turboModel.turboType.name + ' failed.', response);
          }
        );
      }
    };

    $scope.isCreating = false;

    $scope.isBttnCreateDisabled = function(form) {
      return form.$invalid || $scope.isCreating;
    };

    $scope.onCreate = function() {
      if ($scope.part.partType.magentoAttributeSet === 'Turbo') {
        $scope.part.turboModel = $scope.turbo.tm;
        $scope.part.turboType = $scope.turbo.tt;
      }
      $scope.isCreating = true;
      $scope.createPromise = restService.createXRefPart($scope.origPart.id, $scope.part).then(
        function success(newPart) {
          $uibModalInstance.close();
          $route.reload();
          toastr.success('A new cross reference [' + newPart.id + '] - ' +
                  newPart.manufacturerPartNumber + ' has been successfully created.');
        },
        function failure(response) {
          restService.error('Creating of X Ref part for [' + $scope.origPart.id + '] - ' +
                  $scope.origPart.manufacturerPartNumber + ' failed.', response);
        }
      ).finally(function() {
        $scope.isCreating = false;
      });
    };

    $scope.onClose = function() {
      $uibModalInstance.close();
    };

    // Initialization.

    $scope.origPart = origPart;
    $scope.manufacturers = manufacturers;

    $scope.turboTypes = [];
    $scope.turboModels = [];

    $scope.turbo ={
      tm: null,
      tt: null
    };

    $scope.part = {
      partType: origPart.partType
    };

    $scope.filters = {
      turboType: '',
      turboModel: ''
    };

    $scope.onChangeManufacturer();
    $scope.onChangeTurboType();

  }
]);
