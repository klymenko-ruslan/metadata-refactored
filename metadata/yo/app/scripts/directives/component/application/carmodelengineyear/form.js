'use strict';

angular.module('ngMetaCrudApp')
  .directive('cmeyForm', function() {
    return {
      scope: {
        carEngines: '=',
        carMakes: '=',
        carModelEngineYear: '='
      },
      restrict: 'E',
      replace: false,
      templateUrl: '/views/application/carmodelengineyear/form.html',
      controller: ['restService', '$q', '$scope', '$location', '$parse', '$log', '$routeParams', 'toastr',
        '$uibModal', 'NgTableParams',
        function(restService, $q, $scope, $location, $parse, $log, $routeParams, toastr, $uibModal,
          NgTableParams)
        {

          $scope.$on('form:created', function(event, data) {
            if (data.name === 'cmeyForm') {
              $scope.cmeyForm = data.controller;
            }
          });

          $scope.filters = {
            carMake: '',
            carModel: '',
            carEngine: ''
          };

          var makeIdGetter = $parse('cmey.model.make.id');

          $scope.onChangeMake = function() {
            //$scope.carmodels = [];
            var makeId = makeIdGetter($scope);
            if (makeId === undefined || makeId === null) {
              return;
            }
            restService.findCarModelsOfMake(makeId).then(
              function(carmodels) {
                $scope.carmodels = carmodels;
              },
              function(errorResponse) {
                restService.error('Could not load car models for car make: ' + makeId, errorResponse);
              }
            );
          };

          $scope.carYearExists = null;

          // Caveat.
          // Don't use null for the initialization below
          // because binding will not work.
          $scope.cmey = {
            'model': {
              'id': null,
              'make': {
                'id': null
              }
            },
            'engine': {
              'id': null
            },
            'year': {
              'name': null
            }
          };

          $scope.origCmey = null;

          $scope.carengines = [];

          $scope._carengine2item = function(ce) {
            var name = ce.engineSize;
            var fuelType = ce.fuelType;
            if (angular.isObject(fuelType)) {
              name += (', ' + fuelType.name);
            }
            var item = {
              'id': ce.id,
              'name': name
            };
            return item;
          };

          angular.forEach($scope.carEngines, function(ce) {
            var item = $scope._carengine2item(ce);
            $scope.carengines.push(item);
          });

          $scope.carmakes = $scope.carMakes;
          $scope.carmodels = [];
          $scope.cmeyId = $routeParams.id;

          if ($scope.cmeyId === undefined) { // create
            $scope.titleHead = 'Create';
            $scope.cmey = {
              model: {
                id: null,
                make: {
                  id: null
                }
              },
              engine: {
                id: null
              },
              year: {
                name: null
              }
            };
            $scope.carYearExists = false;
          } else { // edit
            $scope.titleHead = 'Edit';
            $scope.origCmey = $scope.carModelEngineYear;
            angular.copy($scope.origCmey, $scope.cmey);
            $scope.onChangeMake();
            $scope.carYearExists = true;
          }

          $scope._revert = function() {
            if (angular.isObject($scope.origCmey)) {
              angular.copy($scope.origCmey, $scope.cmey);
              $scope.onChangeMake();
            }
          };

          $scope._merge = function() {
            var cmey2 = {};
            if ($scope.cmeyId !== null && $scope.cmeyId !== undefined) {
              cmey2.id = $scope.cmeyId;
            }
            if (angular.isObject($scope.cmey.model) && $scope.cmey.model.id) {
              cmey2.model = {
                'id': $scope.cmey.model.id,
              };
            }
            if (angular.isObject($scope.cmey.engine) && $scope.cmey.engine.id) {
              cmey2.engine = {
                'id': $scope.cmey.engine.id
              };
            }
            if (angular.isObject($scope.cmey.year) && $scope.cmey.year.name) {
              cmey2.year = {
                'name': $scope.cmey.year.name
              };
            }
            return cmey2;
          };

          $scope._save = function() {
            var cmey2 = $scope._merge();
            if (_.isEmpty(cmey2)) {
              // Ignore.
              toastr.info('Nothing to save. Ignored.');
              return null;
            }
            if ($scope.cmeyId === undefined) {
              return restService.createCarmodelengineyear(cmey2);
            } else {
              return restService.updateCarmodelengineyear(cmey2);
            }
          };

          $scope.$on('cmeyform:save', function(event, callback) {
            var promise = $scope._save();
            if (promise !== null) {
              callback(promise);
            }
          });

          $scope.$on('cmeyform:revert', function() {
            $scope._revert();
          });

          $scope.onClearMM = function() {
            $scope.filters.carMake = '';
            $scope.filters.carModel = '';
            $scope.cmey.model.make.id = null;
            $scope.cmey.model.id = null;
            $scope.carmodels.splice(0, $scope.carmodels.length);
            $scope.cmeyForm.$setDirty();
          };

          $scope.onClearEngine = function() {
            $scope.filters.carEngine = '';
            $scope.cmey.engine.id = null;
            $scope.cmeyForm.$setDirty();
          };

          $scope.onClearYear = function() {
            $scope.cmey.year.id = null;
            $scope.cmey.year.name = null;
            $scope.cmeyForm.$setDirty();
          };

          function clearAllInputs() {
            $scope.onClearMM();
            $scope.onClearEngine();
            $scope.onClearYear();
          }

          $scope.onChangeYear = function() {
            var year = $scope.cmey.year;
            if (!year) {
                return;
            }
            var yearName = year.name;
            if (!yearName) {
                return;
            }
            restService.findCarYearByName(yearName).then(
              function(caryear) {
                $scope.carYearExists = angular.isObject(caryear);
              },
              function(errorResponse) {
                $log.log('Cat\'t validate the "car year". Error: ' + angular.toJson(errorResponse));
              }
            );
          };

          $scope.validateForm = function() {
            var modelId = $scope.$eval('cmey.model.id');
            var engineId = $scope.$eval('cmey.engine.id');
            var yearName = $scope.$eval('cmey.year.name');
            restService.existsCarmodelengineyear(modelId, engineId, yearName).then(
                function success(exists) {
                  if (exists) {
                    $scope.cmeyForm.$valid = false;
                    $scope.cmeyForm.$invalid = true;
                    $scope.cmeyForm.$error.nonUniqueApp = true;
                  } else {
                    delete $scope.cmeyForm.$error.nonUniqueApp;
                    if (jQuery.isEmptyObject($scope.cmeyForm.$error)) {
                      $scope.cmeyForm.$valid = true;
                      $scope.cmeyForm.$invalid = false;
                    }
                  }
                },
                function failure(response) {
                  restService.error('Could not validate application.', response);
                }
              );
          };

          $scope.$watch('cmey', function() {
            $scope.validateForm();
          }, true);

          $scope.pickedModels = [];
          $scope.pickedModelIds = {};

          $scope.pickedModelsTableParams = new NgTableParams(
            {
              page: 1,
              count: 10,
              sorting: {}
            },
            {
              dataset: $scope.pickedModels
            }
          );

          $scope.pickCarModel = function() {
            var carModel = {};
            var modelId = $scope.cmey.model.id;
            if (modelId === null || $scope.pickedModelIds[modelId]) {
              return;
            }
            var pickedModel = _.find($scope.carmodels, function(cmd) {
              return cmd.id === modelId;
            });
            if (pickedModel !== undefined) {
              angular.copy(pickedModel, carModel);
              var carMake = {};
              var makeId = makeIdGetter($scope);
              if (makeId) {
                var pickedMake = _.find($scope.carmakes, function(cmk) {
                  return cmk.id === makeId;
                });
                angular.copy(pickedMake, carMake);
                carModel.make = carMake;
              }
              $scope.pickedModels.push(carModel);
              $scope.pickedModelIds[modelId] = true;
              $scope.pickedModelsTableParams.settings({dataset: $scope.pickedModels});
            }
          };

          $scope.unpickCarModel = function(carModelId) {
            var idx = _.findIndex($scope.pickedModels, function(cm) {
              return cm.id === carModelId;
            });
            var carModel = $scope.pickedModels[idx];
            delete $scope.pickedModelIds[carModel.id];
            $scope.pickedModels.splice(idx, 1);
            $scope.pickedModelsTableParams.settings({dataset: $scope.pickedModels});
          };

          $scope.unpickAllCarModels = function() {
            _.each($scope.pickedModels, function(carModel) {
              delete $scope.pickedModelIds[carModel.id];
            });
            $scope.pickedModels.splice(0, $scope.pickedModels.length);
            $scope.pickedModelsTableParams.settings({dataset: $scope.pickedModels});
          };

          $scope.pickedEngines = [];
          $scope.pickedEngineIds = {};

          $scope.pickedEnginesTableParams = new NgTableParams(
            {
              page: 1,
              count: 10,
              sorting: {}
            },
            {
              dataset: $scope.pickedEngines
            }
          );

          $scope.pickCarEngine = function() {
            var carEngine = {};
            var engineId = $scope.cmey.engine.id;
            if (engineId === null || $scope.pickedEngineIds[engineId]) {
              return;
            }
            var pickedEngine = _.find($scope.carEngines, function(ce) {
              return ce.id === engineId;
            });
            if (pickedEngine !== undefined) {
              angular.copy(pickedEngine, carEngine);
              $scope.pickedEngines.push(carEngine);
              $scope.pickedEngineIds[engineId] = true;
              $scope.pickedEnginesTableParams.settings({dataset: $scope.pickedEngines});
            }
          };

          $scope.unpickCarEngine = function(carEngineId) {
            var idx = _.findIndex($scope.pickedEngines, function(ce) {
              return ce.id === carEngineId;
            });
            var carEngine = $scope.pickedEngines[idx];
            delete $scope.pickedEngineIds[carEngine.id];
            $scope.pickedEngines.splice(idx, 1);
            $scope.pickedEnginesTableParams.settings({dataset: $scope.pickedEngines});
          };

          $scope.unpickAllCarEngines = function() {
            _.each($scope.pickedEngines, function(carEngine) {
              delete $scope.pickedEngineIds[carEngine.id];
            });
            $scope.pickedEngines.splice(0, $scope.pickedEngines.length);
            $scope.pickedEnginesTableParams.settings({dataset: $scope.pickedEngines});
          };

          $scope.pickedYears = [];
          $scope.pickedYearNames = {};

          $scope.pickedYearsTableParams = new NgTableParams(
            {
              page: 1,
              count: 10,
              sorting: {}
            },
            {
              dataset: $scope.pickedYears
            }
          );

          $scope.pickCarYear = function() {
            var carYear = {};
            var yearName = $scope.cmey.year.name;
            angular.copy($scope.cmey.year, carYear);
            $scope.pickedYears.push(carYear);
            $scope.pickedYearNames[yearName] = true;
            $scope.pickedYearsTableParams.settings({dataset: $scope.pickedYears});
          };

          $scope.unpickCarYear = function(carYearName) {
            var idx = _.findIndex($scope.pickedYears, function(cy) {
              return cy.name === carYearName;
            });
            var carYear = $scope.pickedYears[idx];
            delete $scope.pickedYearNames[carYear.name];
            $scope.pickedYears.splice(idx, 1);
            $scope.pickedYearsTableParams.settings({dataset: $scope.pickedYears});
          };

          $scope.unpickAllCarYears = function() {
            _.each($scope.pickedYears, function(carYear) {
              delete $scope.pickedYearNames[carYear.name];
            });
            $scope.pickedYears.splice(0, $scope.pickedYears.length);
            $scope.pickedYearsTableParams.settings({dataset: $scope.pickedYears});
          };

          $scope.bulkGeneration = function() {
            restService.carmodelengineyearBulkCreate($scope.pickedModels, $scope.pickedEngines, $scope.pickedYears).then(
              function success(result) {

                // Clear input filter and selections.
                clearAllInputs();
                $scope.cmeyForm.$setPristine();

                // Clear picked items.
                _.each($scope.pickedModels, function(cm) {
                  delete $scope.pickedModelIds[cm.id];
                });
                $scope.pickedModels.splice(0, $scope.pickedModels.length);
                $scope.pickedModelsTableParams.settings({dataset: $scope.pickedModels});

                _.each($scope.pickedEngines, function(ce) {
                  delete $scope.pickedEngineIds[ce.id];
                });
                $scope.pickedEngines.splice(0, $scope.pickedEngines.length);
                $scope.pickedEnginesTableParams.settings({dataset: $scope.pickedEngines});

                _.each($scope.pickedYears, function(cy) {
                  delete $scope.pickedYearNames[cy.name];
                });
                $scope.pickedYears.splice(0, $scope.pickedYears.length);
                $scope.pickedYearsTableParams.settings({dataset: $scope.pickedYears});
                toastr.success('Created ' + result.created + ' applications. ' + result.ignored + ' ignored.');
              },
              function failure(errorResponse) {
                restService.error('Bulk creation of application failed.', errorResponse);
              }
            );
          };

          $scope.quickCreateCarMake = function() {
            $uibModal.open({
              templateUrl: '/views/application/carmodelengineyear/createCarMakeDlg.html',
              animation: false,
              size: 'lg' ,
              controller: 'createCarMakeDlgCtrl',
              resolve: {
                addCarMakeCallback: function() {
                  return function(newCarMake) {
                    if (_.isArray($scope.carmakes)) {
                      var pos = _.sortedIndex($scope.carmakes, newCarMake, 'name');
                      $scope.carmakes.splice(pos, 0, newCarMake);
                      /*
                      if ($scope.cmey.model === null || $scope.cmey.model === undefined) {
                        $scope.cmey.model = {};
                      }
                      */
                      $scope.cmey.model.make = newCarMake;
                      $scope.cmeyForm.$setDirty();
                    }
                  };
                }
              }
            });
          };

          $scope.quickCreateCarModel = function() {
            var makeId = makeIdGetter($scope);
            $uibModal.open({
              templateUrl: '/views/application/carmodelengineyear/createCarModelDlg.html',
              animation: false,
              size: 'lg' ,
              controller: 'createCarModelDlgCtrl',
              resolve: {
                makeId: function() { return makeId; },
                carMakes: ['restService', function (restService) {
                  return restService.findAllCarMakesOrderedByName();
                }],
                addCarModelCallback: function() {
                  return function(newCarModel) {
                    if (!_.isArray($scope.carmodels)) { // null or undefined
                      $scope.carmodels = [];
                    }
                    var pos = _.sortedIndex($scope.carmodels, newCarModel, 'name');
                    $scope.carmodels.splice(pos, 0, newCarModel);
                    $scope.cmey.model = newCarModel;
                    $scope.cmeyForm.$setDirty();
                  };
                }
              }
            });
          };

          $scope.quickCreateCarEngine = function() {
            $uibModal.open({
              templateUrl: '/views/application/carmodelengineyear/createCarEngineDlg.html',
              animation: false,
              size: 'lg' ,
              controller: 'createCarEngineDlgCtrl',
              resolve: {
                carFuelTypes: ['restService', function(restService) {
                  return restService.findAllCarFuelTypesOrderedByName();
                }],
                addCarEngineCallback: function() {
                  return function(newCarEngine) {
                    if (!_.isArray($scope.carengines)) { // null or undefined
                      $scope.carengines = [];
                    }
                    var item = $scope._carengine2item(newCarEngine);
                    var pos = _.sortedIndex($scope.carengines, item, 'engineSize');
                    $scope.carengines.splice(pos, 0, item);
                    $scope.cmey.engine = item;
                    $scope.cmeyForm.$setDirty();
                  };
                }
              }
            });
          };

        }
      ]

    };
  })
  .controller('createCarMakeDlgCtrl',['$scope', '$log', 'toastr', '$uibModalInstance', 'addCarMakeCallback', 'restService',
    function($scope, $log, toastr, $uibModalInstance, addCarMakeCallback,
        restService)
    {

    $scope.$on('form:created', function(event, data) {
      if (data.name === 'carmakeForm') {
        $scope.carmakeForm = data.controller;
      }
    });

    $scope.save = function() {
      $scope.$broadcast('carmakeform:save', function(promise) {
        promise.then(
          function(carMake) {
            $log.log('Carmake has been successfully created: ' + carMake.id);
            toastr.success('Carmake [' + carMake.id + '] - "' + carMake.name + '" has been successfully created.');
            addCarMakeCallback(carMake);
            $scope.close ();
          },
          function (response) {
            $scope.close ();
            restService.error('Could not create carmake.', response);
          }
        );
      });
    };

    $scope.close = function() {
      $uibModalInstance.close();
    };

  }])
  .controller('createCarModelDlgCtrl',['$scope', '$log', 'toastr', '$uibModalInstance', 'makeId', 'carMakes',
      'addCarModelCallback', 'restService',
    function($scope, $log, toastr, $uibModalInstance, makeId, carMakes, addCarModelCallback, restService) {

    $scope.makeId = makeId;
    $scope.carMakes = carMakes;

    $scope.$on('form:created', function(event, data) {
      if (data.name === 'carmodelForm') {
        $scope.carmodelForm = data.controller;
      }
    });

    $scope.save = function() {
      $scope.$broadcast('carmodelform:save', function(promise) {
        promise.then(
          function(carModel) {
            $log.log('Carmodel has been successfully created: ' + carModel.id);
            toastr.success('Car model [' + carModel.id + '] - "' + carModel.name + '" has been successfully created.');
            addCarModelCallback(carModel);
            $scope.close ();
          },
          function (response) {
            restService.error('Could not create car model.', response);
          }
        );
      });
    };

    $scope.close = function() {
      $uibModalInstance.close();
    };

  }])
  .controller('createCarEngineDlgCtrl',['$scope', '$log', 'toastr', '$uibModalInstance', 'carFuelTypes',
      'addCarEngineCallback', 'restService',
    function($scope, $log, toastr, $uibModalInstance, carFuelTypes, addCarEngineCallback, restService) {

    $scope.carFuelTypes = carFuelTypes;

    $scope.$on('form:created', function(event, data) {
      if (data.name === 'carengineForm') {
        $scope.carengineForm = data.controller;
      }
    });

    $scope.save = function() {
      $scope.$broadcast('carengineform:save', function(promise) {
        promise.then(
          function(carEngine) {
            $log.log('Carengine has been successfully created: ' + carEngine.id);
            toastr.success('Car model [' + carEngine.id + '] - "' + carEngine.engineSize + '" has been successfully created.');
            addCarEngineCallback(carEngine);
            $scope.close ();
          },
          function (response) {
            restService.error('Could not create car engine.', response);
          }
        );
      });
    };

    $scope.close = function() {
      $uibModalInstance.close();
    };

  }]);
