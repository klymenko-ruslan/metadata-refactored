'use strict';

angular.module('ngMetaCrudApp').controller('PartFormCtrl',
  ['$scope', '$location', '$log', '$uibModal', 'dialogs', 'toastr', 'restService', 'Restangular', 'User',
   'part', 'partType', 'manufacturers', 'LinkSource', 'services',
  function($scope, $location, $log, $uibModal, dialogs, toastr, restService, Restangular, User,
    part, partType, manufacturers, LinkSource, services)
  {

    $scope.manufacturers = manufacturers;
    $scope.turboTypes = [];
    $scope.turboModels = [];

    $scope.requiredSource = LinkSource.isSourceRequiredForPart(services);

    $scope.turbo ={
      tm: null,
      tt: null
    }

    $scope.mpns = []; // manufacturer parts numbers

    $scope.filters = {
      turboType: '',
      turboModel: ''
    };

    function newPn(idx, val) {
      return {
        id: 'pn' + idx,
        val: val
      };
    };

    $scope.isEditMode = function() {
      return part !== null;
    };

    $scope.onViewPart = function() {
      $location.path('/part/' + $scope.partId);
    };

    $scope.onChangeManufacturer = function() {
      if ($scope.part.partType.magentoAttributeSet == 'Turbo') {
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

    // Setup the create/update workflow
    if ($scope.isEditMode()) {
      $scope.partId = part.id;
      $scope.part = part;
      if ($scope.part.partType.magentoAttributeSet === 'Turbo') {
        $scope.turbo.tm = part.turboModel;
        $scope.turbo.tt = part.turboModel.turboType;
      }
      $scope.mpns.push(newPn(0, part.manufacturerPartNumber));
      part.manufacturerPartNumber = null;
      $scope.oldPart = Restangular.copy(part);
      $scope.onChangeManufacturer();
      $scope.onChangeTurboType();
    } else {
      $scope.partId = null;
      $scope.part = {
        turboModel: {
          turboType: {
          }
        }
      };
      $scope.oldPart = null;
      $scope.mpns.push(newPn(0, null));
    }

    // Set the part type
    if (partType !== null) {
      $scope.part.partType = partType;
    }

    $scope.addPn = function() {
      var newId = $scope.mpns.length;
      $scope.mpns.push(newPn(newId, null));
    };

    $scope.delPn = function(idx) {
      $scope.mpns.splice(idx, 1);
    };

    $scope.revert = function() {
      $scope.part = Restangular.copy($scope.oldPart);
      $scope.partForm.$setPristine(true);
      $scope.$broadcast('revert');
    };

    $scope.$watch('part.manufacturer', function(newVal, oldVal) {
      // Fire validation in 'Manufacturer P/N' fields.
      _.each($scope.mpns, function(o) {
        var element = $scope.partForm[o.id];
        if (angular.isObject(element)) {
          element.$validate();
        }
      });
    });

    $scope.isManufacturerEnabled = function() {
      return !$scope.isEditMode() || User.hasRole('ROLE_ALTER_PART_MANUFACTURER');
    };

    $scope.isPnEnabled = function() {
      return !$scope.isEditMode() || User.hasRole('ROLE_ALTER_PART_NUMBER');
    };

    function cbCreate(srcIds, ratings, description, attachIds) {
      var partNumbers = _.map($scope.mpns, function(o) { return o.val; });
      restService.createPart($scope.part, partNumbers, srcIds, ratings, description, attachIds).then(
        function(response) {
          if (response.results.length === 1) {
            var id = response.results[0].partId;
            $location.path('/part/' + id);
          } else {
            $location.path('/part/list');
          }
        },
        function(response) {
          restService.error('Could not save part(s).', response);
        });
    };

    $scope.save = function() {
      var url = 'part';

      if ($scope.part.partType.magentoAttributeSet === 'Turbo') {
        $scope.part.turboModel = $scope.turbo.tm;
        $scope.part.turboModel.turboType = $scope.turbo.tt;
      }

      if (angular.isObject($scope.oldPart)) {
        part.manufacturerPartNumber = $scope.mpns[0].val;
        restService.updatePart($scope.part, srcIds, ratings, description).then(
          function(part) {
            $location.path('/part/' + $scope.part.id);
          },
          function(response) {
            restService.error('Could not update part', response);
          }
        );
      } else {
        LinkSource.link(cbCreate, $scope.requiredSource, null);
      }
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
            function(response) {
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

  }
])
.controller('CreateTurboTypeDlgCtrl', ['$scope', '$log', '$uibModalInstance', 'restService',
  'create', 'turbo', 'part', 'turboTypes',
  function($scope, $log, $uibModalInstance, restService, create, turbo, part, turboTypes) {
    $scope.create = create;
    $scope.part = part;
    $scope.name = '';
    if (!create) {
      $scope.name = turbo.tt.name;
    }

    $scope.onCreate = function() {
      restService.createTurboType($scope.part.manufacturer.id, $scope.name).then(
        function success(newTurboType) {
          turbo.tt = newTurboType;
          var idx = _.sortedIndex(turboTypes, newTurboType, 'name');
          turboTypes.splice(idx, 0, newTurboType);
          $uibModalInstance.close();
        },
        function failure(response) {
          $uibModalInstance.close();
          restService.error('Creation of a new turbo model failed: ' + $scope.name, response);
        }
      );
    };

    $scope.onRename = function() {
      turbo.tt.name = $scope.name;
      _.each(turboTypes, function(tt) {
        if (tt.id === turbo.tt.id) {
          tt.name = $scope.name;
        };
      });
      restService.renameTurboType(turbo.tt).then(
        function success(updatedTt) {
          $uibModalInstance.close();
        },
        function failure(response) {
          $uibModalInstance.close();
          restService.error('Rename of the turbo model failed.', response);
        }
      );
    };

    $scope.onClose = function() {
      $uibModalInstance.close();
    };

}])
.directive('uniqueTurbotypeName', ['$log', '$q', 'restService', function($log, $q, restService) {
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.uniqueTurbotypeName = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        if ($scope.part.manufacturer === undefined) {
          def.resolve();
        } else {
          restService.findTurboTypeByManufacturerAndName($scope.part.manufacturer.id, viewValue).then(
            function(foundTurboType) {
              if (!angular.isObject(foundTurboType)) {
                def.resolve();
              } else {
                def.reject();
              }
            },
            function(errorResponse) {
              $log.log('Couldn\'t validate name of the turbo type: ' + viewValue);
              def.reject();
            }
          );
        }
        return def.promise;
      };
    }
  };
}])
.controller('CreateTurboModelDlgCtrl', ['$scope', '$log', '$uibModalInstance', 'restService',
  'create', 'turbo', 'part', 'turboModels',
  function($scope, $log, $uibModalInstance, restService, create, turbo, part, turboModels) {
    $scope.create = create;
    $scope.turbo = turbo;
    $scope.part = part;
    $scope.name = '';
    if (!create) {
      $scope.name = turbo.tm.name;
    }

    $scope.onCreate = function() {
      restService.createTurboModel(turbo.tt.id, $scope.name).then(
        function success(newTurboModel) {
          turbo.tm = newTurboModel;
          var idx = _.sortedIndex(turboModels, newTurboModel, 'name');
          turboModels.splice(idx, 0, newTurboModel);
          $uibModalInstance.close();
        },
        function failure(response) {
          $uibModalInstance.close();
          restService.error('Creatation of a new turbo model failed: ' + $scope.name, response);
        }
      );
    };

    $scope.onRename = function() {
      turbo.tm.name = $scope.name;
      restService.renameTurboModel(turbo.tm).then(
        function success(updatedTm) {
          _.each(turboModels, function(tm) {
            if (tm.id === turbo.tm.id) {
              tm.name = $scope.name;
            };
          });
          $uibModalInstance.close();
        },
        function failure(response) {
          $uibModalInstance.close();
          restService.error('Rename of the turbo model failed.', response);
        }
      );
    }

    $scope.onClose = function() {
      $uibModalInstance.close();
    };

}])
.directive('uniquePartNumber', ['$log', '$q', 'restService', function($log, $q, restService) {
  // Validator for uniqueness of the part number.
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.nonUniquePartNumber = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        if ($scope.part.manufacturer === undefined) {
          def.resolve();
        } else {
          restService.findPartByNumber($scope.part.manufacturer.id, viewValue).then(
            function(foundPart) {
              if (!angular.isObject(foundPart) || foundPart.id == $scope.partId) {
                def.resolve();
              } else {
                def.reject();
              }
            },
            function(errorResponse) {
              $log.log('Couldn\'t validate part number: ' + viewValue);
              def.reject();
            }
          );
        }
        return def.promise;
      };
    }
  };
}]);
