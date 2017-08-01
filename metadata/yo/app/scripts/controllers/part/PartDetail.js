'use strict';

angular.module('ngMetaCrudApp')
.controller('PartDetailCtrl', ['$scope', '$log', '$q', '$location', '$cookies', '$route', '$routeParams', 'Kits',
    'NgTableParams', 'utils', 'restService', 'Restangular', 'User', '$uibModal', 'dialogs', 'toastr',
    'part', 'criticalDimensions', 'partTypes', 'manufacturers', 'turbos', 'oversizeParts', 'standardParts', 'prices',
    function ($scope, $log, $q, $location, $cookies, $route, $routeParams, Kits, NgTableParams, utils,
    restService, Restangular, User, $uibModal, dialogs, toastr, part, criticalDimensions, partTypes, manufacturers,
    turbos, oversizeParts, standardParts, prices) {
  $scope.partId = part.id;
  $scope.part = part;
  $scope.partTypeOpts = _.map(partTypes, function (pt) {
    return {'id': pt.value, 'title': pt.name};
  });
  $scope.partTypeOpts.unshift({'id': null, 'title': ''});
  $scope.oversizeParts = oversizeParts;
  $scope.standardParts = standardParts;
  $scope.prices = prices;
  $scope.formMode = 'view';
  $scope.criticalDimensions = criticalDimensions;
  $scope.restService = restService;
  // Make sure we're using the correct part type
  $scope.partType = part.partType.name;

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

  $scope.onReload = function() {
    $route.reload();
  };

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

  $scope.onRebuildBom = function() {
      restService.rebuildPartBom($scope.partId).then(
        function success() {
          $route.reload();
          toastr.success('BOMs for the part have been successfully rebuilt.');
        },
        function failure(error) {
          restService.error('The rebuild BOM request failed.', error);
        }
      );
  };

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
      return restService.filterChangelog(null, null, null, null, null, null, $scope.partId,
        sortProperty, sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            params.total(result.total);
            return result.recs;
          },
          function(errorResponse) {
            restService.error('Search in the changelog failed.', errorResponse);
          }
      );
    }
  });

  if ($scope.part.manufacturer.name === 'Turbo International') {
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
            if (result) {
              params.total(result.total);
              return result.recs;
            } else {
              params.total(0);
              return [];
            }
          },
          function(errorResponse) {
            restService.error('Search in the changelog failed.', errorResponse);
          }
        );
      }
    });
  }

  $scope.turbosTableParams = null;

  function _initTurbosTableParams(turbos) {
    $scope.turbosTableParams = new NgTableParams({
      'page': 1,
      'count': 10,
      'sorting': {
        'id': 'asc'
      }
      }, {
        'getData': utils.localPagination(turbos, 'id')
      });
  }

  _initTurbosTableParams(turbos);

  // TODO: Find a better way. Directive?
  if (part.partType.magentoAttributeSet === 'Kit') {
    $scope.kitComponents = Kits.listComponents($scope.partId).then(
      function(components) {
        $scope.kitComponents  = components;
      },
      function (error) {
        restService.error('Can\'t load kits.', error);
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
            _initTurbosTableParams(turbos);
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

  $scope.removeComponent = function(componentToRemove) {

    dialogs.confirm(
      'Remove Common Component Mapping?',
      'Do you want to remove this common component mapping from the kit?').result.then(
      function() {
        // Yes
        restService.removeCommonComponentMapping($scope.partId, componentToRemove.id).then(
          function() {
            // Success
            toastr.success('Component removed.');
            var idx = _.indexOf($scope.kitComponents, componentToRemove);
            $scope.kitComponents.splice(idx, 1);
          },
          function(response) {
            // Error
            restService.error('Could not delete common component mapping.', response);
          });
      },
      function() {
        // No
      });
  };

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

  $scope.oversizePartsTableParams = new NgTableParams({
    'page': 1,
    'count': 10,
    'sorting': {
      'manufacturerPartNumber': 'asc'
    }
  }, {
    'getData': utils.localPagination($scope.oversizeParts, 'manufacturerPartNumber')
  });

  $scope.standardPartsTableParams = new NgTableParams({
    'page': 1,
    'count': 10,
    'sorting': {
      'manufacturerPartNumber': 'asc'
    }
  }, {
    'getData': utils.localPagination($scope.standardParts, 'manufacturerPartNumber')
  });

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
            $scope.oversizePartsTableParams.reload();
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
            $scope.standardPartsTableParams.reload();
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
