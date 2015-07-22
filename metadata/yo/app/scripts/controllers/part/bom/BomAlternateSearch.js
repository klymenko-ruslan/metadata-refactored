'use strict';

angular.module('ngMetaCrudApp')
    .controller('BomAlternateSearchCtrl', function ($log, $scope, $location, $routeParams, BOM, restService, Restangular, dialogs, gToast) {
        $scope.restService = restService;
        $scope.partId = $routeParams.id;
        $scope.bomItemId = $routeParams.bomId;
        

        $scope.pickedPart = null;
        $scope.showPickedPart = false;

        // The part whose bom we're editing
        $scope.part = restService.findPart($scope.partId).then(
            function (parentPart) {
                $scope.part = parentPart;
            }, restService.error);
            
        BOM.listByParentPartId($scope.partId)
            .then(function(bom) {
                $scope.bom = bom;

                $scope.bomItem = _.find(bom, function(bomItem) {
                    return bomItem.id === $scope.bomItemId;
                });
            }, restService.error);

        $scope.save = function () {
          Restangular.one('bom/' + $scope.bomItem.id + '/alt')
                  .post($scope.pickedPart.id, {header: $scope.header})
                  .then(function () {
              // Success
              gToast.open("BOM alternate added.");
              $location.path("/part/" + $scope.partId);
            }, function (response) {
                dialogs.error("Could not add BOM alternate", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
            });
        }

        $scope.pickPart = function (partId) {
            $scope.pickedPart = restService.findPart(partId).then(
                function (pickedPart) {
                    $scope.pickedPart = pickedPart;
                },
                function (errorResponse) {
                    $log.log("Could not pick part", errorResponse);
                    restService.error("Could not pick part.", errorResponse);
                });
        }
    });
