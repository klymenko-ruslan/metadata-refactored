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
          $dialogs.create('/views/part/AddImage.html', 'AddPartImageCtrl',
              {part: $scope.part, callback: $scope.imageAddedCallback});
        }

        $scope.imageAddedCallback = function(image) {
          $scope.part.productImages.push(image);
        }
    });
