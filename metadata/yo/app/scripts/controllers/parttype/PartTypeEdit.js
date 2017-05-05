'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartTypeEditCtrl', ['$scope', 'restService', 'METADATA_BASE', 'partType',
    function ($scope, restService, METADATA_BASE, partType) {
      $scope.partType = partType;
      $scope.METADATA_BASE = METADATA_BASE;
      $scope.legendImage = null;

      $scope.onSelectLegendImage = function(files) {
        $scope.legendImage = files[0];
      };

      $scope.uploadLegend = function() {
        $('#dlgUploadLegend').modal('hide');
        if ($scope.legendImage === null || $scope.legendImage === undefined) {
          $('#dlgFileNotSelected').modal('show');
          return;
        }
        restService.uploadPartTypeLegend($scope.partType.id, $scope.legendImage).then(
          function success(httpResponse) {
            $scope.partType.legendImgFilename = httpResponse.data.legendImgFilename;
          },
          function failure(response) {
            restService.error('Uploading of a part type legend failed.', response);
          }
        ).finally(function() {
          $scope.legendImage = null;
        });
      };

      $scope.showDeleteLegendDlg = function() {
        if ($scope.partType.legendImgFilename) {
          $('#dlgDeleteLegend').modal('show');
        }
      };

      $scope.deleteLegend = function() {
        $('#dlgDeleteLegend').modal('hide');
        restService.deletePartTypeLegend($scope.partType.id).then(
          function success() {
            $scope.partType.legendImgFilename = null;
          },
          function failure(response) {
            restService.error('Deletion of a part type legend failed.', response);
          }
        );
      };

    }
  ]);
