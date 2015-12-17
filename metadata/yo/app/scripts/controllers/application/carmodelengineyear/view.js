'use strict';

angular.module('ngMetaCrudApp')
  .controller('CarmodelengineyearViewCtrl', function ($scope, $routeParams, restService) {
    $scope.cmeyId = $routeParams.id;
    restService.findCarmodelengineyear($scope.cmeyId)
      .then(function (carmodelengineyear) {
        $scope.carmodelengineyear = carmodelengineyear;
      }, function (errorResponse) {
        restService.error("Could not get 'car_model_engine_year' details", errorResponse);
      });
  });
