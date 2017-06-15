'use strict';

angular.module('ngMetaCrudApp')

.controller('TurboModelsCtrl', ['$scope', 'dialogs', '$log', 'toastr',
  'restService',
  function($scope, dialogs, $log, toastr, restService) {

    $scope.selection = {
      manufacturer: null,
      turboType: null,
      turboModel: null
    };

    $scope.turboTypes = null;
    $scope.turboModels = null;

    // This value is used to create a new turbo type or rename existing one.
    $scope.turbotype = null;

    // This value is used to create a new turbo model or rename existing one.
    $scope.turbomodel = null;

    $scope.displayCreateTurboTypeDlg = function() {
      $scope.turbotype = {
        id: null,
        name: null
      };
      $('#createTurboTypeDlg').modal('show');
    };

    $scope.createTurboType = function() {
      restService.createTurboType($scope.selection.manufacturer.id,
        $scope.turbotype.name).then(
          function() {
            $('#createTurboTypeDlg').modal('hide');
            toastr.success('The turbo type \'' + $scope.turbotype.name +
              '\' has been successfully created.');
            $scope.loadTurboTypes($scope.selection.manufacturer.id);
          },
          function(response) {
            $('#createTurboTypeDlg').modal('hide');
            restService.error('Could not create turbo type.', response);
          }
        ).finally(function() {
          $scope.turbotype = null;
        });
    };

    $scope.displayRenameTurboTypeDlg = function() {
      $scope.turbotype = angular.copy($scope.selection.turboType);
      $('#renameTurboTypeDlg').modal('show');
    };

    $scope.renameTurboType = function() {
      restService.renameTurboType($scope.turbotype).then(
        function() {
          $('#renameTurboTypeDlg').modal('hide');
          toastr.success('The turbo type has been successfully renamed.');
          $scope.selection.turboType = null;
          $scope.loadTurboTypes($scope.selection.manufacturer.id);
        },
        function(response) {
          $('#renameTurboTypeDlg').modal('hide');
          restService.error('Could not rename turbo type.', response);
        }
      ).finally(function() {
        $scope.turbotype = null;
      });
    };

    $scope.deleteTurboType = function(turboType) {
      // An attribute 'windowClass' below is just a marker (there is no class
      // with such name) for e2e tests to find this dialog. Don't remove it.
      dialogs.confirm(
        'Delete Turbo Type?',
        'Do you want to delete this turbo type?',
        { 'windowClass': 'delete-turbo-type-dlg'}).result.then(
        function() {
          // Yes
          restService.deleteTurboType(turboType.id).then(
            function() {
              // Success
              toastr.success('Turbo type deleted.');
              $scope.selection.turboType = null;
              $scope.loadTurboTypes($scope.selection.manufacturer.id);
            },
            function() {
              // Error
              dialogs.error(
                'Could not delete turbo type.',
                'Turbo type must not be used for any parts or turbo models. ' +
                'Check server log for details.',
                { 'windowClass': 'delete-turbo-type-failure-dlg' });
            });
        },
        function() {
          // No
        }
      );
    };

    $scope.displayCreateTurboModelDlg = function() {
      $scope.turbomodel = {
        id: null,
        name: null
      };
      $('#createTurboModelDlg').modal('show');
    };

    $scope.createTurboModel = function() {
      restService.createTurboModel($scope.selection.turboType.id,
        $scope.turbomodel.name).then(
          function() {
            $('#createTurboModelDlg').modal('hide');
            toastr.success('The turbo model \'' + $scope.turbomodel.name +
              '\' has been successfully created.');
            $scope.loadTurboModels($scope.selection.turboType.id);
          },
          function(response) {
            $('#createTurboModelDlg').modal('hide');
            restService.error('Could not create turbo model.', response);
          }
        ).finally(function() {
          $scope.turbomodel = null;
        }
      );
    };

    $scope.displayRenameTurboModelDlg = function() {
      $scope.turbomodel = angular.copy($scope.selection.turboModel);
      $('#renameTurboModelDlg').modal('show');
    };

    $scope.renameTurboModel = function() {
      restService.renameTurboModel($scope.turbomodel).then(
        function() {
          $('#renameTurboModelDlg').modal('hide');
          toastr.success('The turbo model has been successfully renamed.');
          $scope.selection.turboModel = null;
          $scope.loadTurboModels($scope.selection.turboType.id);
        },
        function(response) {
          $('#renameTurboModelDlg').modal('hide');
          restService.error('Could not rename turbo model.', response);
        }
      ).finally(function() {
        $scope.turbomodel = null;
      });
    };

    $scope.deleteTurboModel = function(turboModel) {

      // An attribute 'windowClass' below is just a marker (there is no class
      // with such name) for e2e tests to find this dialog. Don't remove it.
      dialogs.confirm(
        'Delete Turbo Model?',
        'Do you want to delete this turbo model?',
        { 'windowClass': 'delete-turbo-model-dlg' }).result.then(
        function() {
          // Yes
          restService.deleteTurboModel(turboModel.id).then(
            function() {
              // Success
              toastr.success('Turbo model deleted.');
              $scope.selection.turboModel = null;
              $scope.loadTurboModels($scope.selection.turboType.id);
            },
            function(/*response*/) {
              // Error
              dialogs.error(
                'Could not delete turbo model.',
                'Turbo model must not be used for any parts. ' +
                'Check server log for details.',
                { 'windowClass': 'delete-turbo-model-failure-dlg' });
            });
        },
        function() {
          // No
        }
      );

    };

    $scope.loadTurboTypes = function(newMfrId) {
      $scope.turboTypes = null;
      $scope.turboModels = null;
      $scope.selection.turboType = null;
      $scope.selection.turboModel = null;

      if (angular.isDefined(newMfrId)) {
        $log.log('Fetching new turbo types for manufacturer', newMfrId);
        $scope.turboTypes = restService.listTurboTypesForManufacturerId(newMfrId).$object;
      }
    };

    $scope.loadTurboModels = function(newTurboTypeId) {
      $scope.turboModels = null;
      $scope.selection.turboModel = null;

      if (angular.isDefined(newTurboTypeId)) {
        $log.log('Fetching new turbo models for type', newTurboTypeId);
        $scope.turboModels = restService.listTurboModelsForTurboTypeId(newTurboTypeId).$object;
      }
    };

    $scope.$watch('selection.manufacturer.id', $scope.loadTurboTypes);

    $scope.$watch('selection.turboType.id', $scope.loadTurboModels);
  }])
.directive('uniqueTurbomodelsTurbotypename', ['$log', '$q', 'restService',
  function($log, $q, restService) {
    return {
      require: 'ngModel',
      link: function($scope, elm, attr, ctrl) {
        ctrl.$asyncValidators.uniqueTurbotypeName = function(modelValue,
          viewValue)
        {
          var def = $q.defer();
          if (ctrl.$isEmpty(modelValue)) {
            return $q.when();
          }
          var mnfrId = $scope.selection.manufacturer.id;
          restService.findTurboTypeByManufacturerAndName(mnfrId, viewValue).then(
            function(foundTurboType) {
              var unique = !angular.isObject(foundTurboType) ||
                $scope.turbotype.id === foundTurboType.id;
              if (unique) {
                def.resolve();
              } else {
                def.reject();
              }
            },
            function(/*errorResponse*/) {
              $log.log('Couldn\'t validate name of the turbo type: ' + viewValue);
              def.reject();
            }
          );
          return def.promise;
        };
      }
    };
}])
.directive('uniqueTurbomodelsTurbomodelname', ['$log', '$q', 'restService',
  function($log, $q, restService) {
    return {
      require: 'ngModel',
      link: function($scope, elm, attr, ctrl) {
        ctrl.$asyncValidators.uniqueTurbomodelName = function(modelValue,
          viewValue)
        {
          var def = $q.defer();
          if (ctrl.$isEmpty(modelValue)) {
            return $q.when();
          }
          var ttId = $scope.selection.turboType.id;
          restService.findTurboModelByTurboTypeAndName(ttId, viewValue).then(
            function(foundTurboModel) {
              var unique = !angular.isObject(foundTurboModel) ||
                $scope.turbomodel.id === foundTurboModel.id;
              if (unique) {
                def.resolve();
              } else {
                def.reject();
              }
            },
            function(/*errorResponse*/) {
              $log.log('Couldn\'t validate name of the turbo model: ' +
                viewValue);
              def.reject();
            }
          );
          return def.promise;
        };
      }
    };
}]);
