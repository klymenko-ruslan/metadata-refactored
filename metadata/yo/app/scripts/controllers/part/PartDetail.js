'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartDetailCtrl', function ($scope, $log, $q, $location, $routeParams, ngTableParams, restService, Restangular, $dialogs, gToast) {
        $scope.partId = $routeParams.id;

        $scope.part = null;
        $scope.partPromise = restService.findPart($scope.partId).then(
            function (part) {
                $scope.part = part;

                // Make sure we're using the correct part type
                $scope.partType = part.partType.name;

                // Reload the table
                $scope.bomTableParams.reload();
            },
            function (errorResponse) {
                $log.log("Could not get part details", errorResponse);
                restService.error("Could not get part details", errorResponse);
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

        $scope.removeTurboType = function(turboTypeToRemove) {

          $dialogs.confirm(
            "Remove Turbo Type?",
            "Do you want to remove this turbo type from the part?").result.then(
            function() {
              // Yes
              Restangular.setParentless(false);
              Restangular.one('part', $scope.partId).one('turboType', turboTypeToRemove.id).remove().then(
                function() {
                  // Success
                  gToast.open("Turbo type removed.");

                  var idx = _.indexOf($scope.part.turboTypes, turboTypeToRemove);
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

        $scope.removeComponent = function(componentToRemove) {

          $dialogs.confirm(
            "Remove Common Component Mapping?",
            "Do you want to remove this common component mapping from the kit?").result.then(
            function() {
              // Yes
              Restangular.setParentless(false);
              Restangular.one('kit', $scope.partId).one('component', componentToRemove.id).remove().then(
                function() {
                  // Success
                  gToast.open("Component removed.");

                  var idx = _.indexOf($scope.part.components, componentToRemove);
                  $scope.part.components.splice(idx, 1);
                },
                function(response) {
                  // Error
                  restService.error("Could not delete common component mapping.", response);
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

        // Applications

        $scope.addApplication = function() {
          alert("TODO");
        }
    });
