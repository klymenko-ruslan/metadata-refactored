'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartFormCtrl', function ($q, $scope, $location, $log, $routeParams, ngTableParams, restService, Restangular, PartTypes) {

        // Setup the create/update workflow
        if ($routeParams.id) {
            $scope.partId = $routeParams.id;

            $scope.oldPartPromise = restService.findPart($scope.partId).then(
                function (part) {
                    console.log("Part data loaded.");

                    // Save the part
                    $scope.part = part;
                    $scope.oldPart = Restangular.copy(part);
                },
                function (response) {
                    alert("Could not get part data from the server.");
                });
        } else {
            $scope.partId = null;
            $scope.part = {};
        }

        // Set the part type
        if ($routeParams.typeId) {
          $scope.part.partType = PartTypes.getById($routeParams.typeId);
          $log.log('Got part type by ID', $routeParams.typeId, $scope.part.partType);
        }

        $scope.revert = function () {
            $scope.part = Restangular.copy($scope.oldPart);
            $scope.partForm.$setPristine(true);
            $scope.$broadcast("revert");
        }

        $scope.save = function () {
            if ($scope.oldPart == null) {
                Restangular.all('part').post($scope.part).then(
                    function (id) {
                        $location.path('/part/' + id);
                    },
                    function () {
                        alert("Could not save part.");
                    })
            } else {
                $scope.part.put().then(
                    function (part) {
                        $location.path('/part/' + $scope.part.id);
                    },
                    function () {
                        alert("Could not update part");
                    }
                );
            }
        }

    });
