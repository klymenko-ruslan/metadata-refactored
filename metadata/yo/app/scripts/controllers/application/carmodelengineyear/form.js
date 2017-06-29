'use strict';

angular.module('ngMetaCrudApp')
.controller('CarModelEngineYearFormCtrl', ['$scope', '$log', '$location',
  'toastr', 'restService', 'carEngines', 'carMakes', 'carModelEngineYear',
  function($scope, $log, $location, toastr, restService, carEngines, carMakes,
    carModelEngineYear) {

    $scope.carEngines = carEngines;
    $scope.carMakes = carMakes;
    $scope.carModelEngineYear = carModelEngineYear;
    $scope.cmeyId = null;
    if (carModelEngineYear !== null) {
      $scope.cmeyId = carModelEngineYear.id;
    }

    $scope.$on('form:created', function(event, data) {
      if (data.name === 'cmeyForm') {
        $scope.cmeyForm = data.controller;
      }
    });

    $scope.onClickViewCmey = function() {
      $location.path('application/carmodelengineyear/' + $scope.cmeyId);
    };

    $scope.save = function() {
      $scope.$broadcast('cmeyform:save', function(promise) {
        if ($scope.cmeyId === null) {
          promise.then(
            function(newCmeyId) {
              $log.log('Created "car_model_engine_year": ' + newCmeyId);
              toastr.success('A new Model Engine Year has been ' +
                'successfully created.');
              $location.path('/application/carmodelengineyear/list');
            },
            function(errorResponse) {
              restService.error('Could not create "car_model_engine_year".',
                errorResponse);
            }
          );
        } else {
          promise.then(
            function() {
              $log.log('Updated "car_model_engine_year": ' + $scope.cmeyId);
              toastr.success('The Model Engine Year has been successfully ' +
                'updated.');
              $location.path('/application/carmodelengineyear/list');
            },
            function(errorResponse) {
              restService.error('Could not update "car_model_engine_year": ' +
                $scope.cmeyId, errorResponse);
            }
          );
        }
      });
    };

    $scope.revert = function() {
      $scope.$broadcast('cmeyform:revert');
    };

  }

]);
