"use strict";

angular.module("ngMetaCrudApp")
 .controller("PartListCtrl", ["$scope", "$modal", function ($scope, $modal) {
   $scope.createPart = function () {
     var modalInstance = $modal.open({
       "templateUrl": "/views/part/PartCreateModal.html",
       "controller": "PartCreateModalCtrl"
     });
   };
 }]);
