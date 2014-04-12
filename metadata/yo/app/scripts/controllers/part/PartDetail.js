'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartDetailCtrl', function ($scope, $log, $q, $location, $routeParams, ngTableParams, restService, Restangular, $dialogs, gToast) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

        $scope.part = null;
        $scope.partPromise = restService.findPart($scope.partId).then(
            function (part) {
                $scope.part = part;

                // Make sure we're using the correct part type
                $scope.partType = part.partType.typeName;

                // Reload the table
                $scope.bomTableParams.reload();
            },
            function (errorResponse) {
                $log.log("Could not get part details", errorResponse);
                alert("Could not get part details");
            });


        // Turbo Types
        $scope.addTurboType = function() {
          $dialogs.create(
            '/views/part/dialog/AddTurboType.html',
            'AddTurboTypeDialogCtrl',
            {partId: $scope.partId}
          ).result.then(function(turboType) {
            $scope.part.turboTypes.push(turboType);
          });
        }

        $scope.removeTurboType = function(turboTypeId) {

          $dialogs.confirm(
            "Remove Turbo Type?",
            "Do you want to remove this turbo type from the part?").result.then(
            function() {
              // Yes
              Restangular.setParentless(false);
              Restangular.one('part', $scope.partId).one('turboType', turboTypeId).remove().then(
                function() {
                  // Success
                  gToast.open("Turbo type removed.");

                  var idx = _.find($scope.part.turboTypes, function(turboType) {
                    return turboType.id = turboTypeId;
                  });
                  $scope.part.turboTypes.splice(idx, 1);
                },
                function(response) {
                  // Error
                  restService.error("Could not delete image.", response);
                });
            },
            function() {
              // No
            });
        };


        // Images

        $scope.deleteImage = function(image) {

          $dialogs.confirm(
                  "Delete image?",
                  "Do you want to remove this image from the part?").result.then(
              function() {
                // Yes
                Restangular.one('image', image.id).remove().then(
                    function() {
                      // Success
                      gToast.open("Image removed.");

                      var idx = _.indexOf($scope.part.productImages, image);
                      $scope.part.productImages.splice(idx, 1);
                    },
                    function(response) {
                      // Error
                      restService.error("Could not delete image.", response);
                    });
              },
              function() {
                // No
              });

        }

        $scope.addImage = function() {
          $dialogs.create(
            '/views/part/dialog/AddImage.html',
            'AddPartImageCtrl',
            {part: $scope.part}
          ).result.then(function(image) {
            $scope.part.productImages.push(image);
          });
        }
    });
