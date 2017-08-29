'use strict';

angular.module('ngMetaCrudApp')
    .controller('BomAlternateSearchCtrl', function ($log, $scope, $location, $routeParams, NgTableParams, utils, 
      restService, dialogs, toastr, partTypes, part, bom, altBom)
    {
        $scope.restService = restService;
        $scope.partTypes = partTypes;
        $scope.part = part;
        $scope.partId = $routeParams.id;
        $scope.bomItemId = $routeParams.bomId;

        $scope.pickedPart = null;

        $scope.bomTableParams = new NgTableParams({
            page: 1,
            count: 10
          }, {
            getData: utils.localPagination(bom, 'partNumber')
          }
        );

        $scope.altBomTableParams = new NgTableParams({
            page: 1,
            count: 10
          }, {
            getData: utils.localPagination(altBom, 'partNumber')
          }
        );

        $scope.save = function () {
          restService.createBomAlternative($scope.bomItem.id, $scope.pickedPart.id).then(
            function success() {
              toastr.success('BOM alternate added.');
              $location.path('/part/' + $scope.partId);
            },
            function failure(response) {
                dialogs.error('Could not add BOM alternate', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
            });
        };

        $scope.pickPart = function (partId) {
            $scope.pickedPart = restService.findPart(partId).then(
                function (pickedPart) {
                    $scope.pickedPart = pickedPart;
                },
                function (errorResponse) {
                    $log.log('Could not pick part', errorResponse);
                    restService.error('Could not pick part.', errorResponse);
                });
        };
        
    });
