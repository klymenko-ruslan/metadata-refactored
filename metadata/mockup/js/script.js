"use strict";

angular.module("mockApp", ["ngRoute", "ngTable", "ui.bootstrap",
    "restangular", /*"dialogs.main",*/ "gToast" ]);


angular.module("mockApp")
.controller("dynColsCtrl", ["$scope", "$log", "NgTableParams",
    function($scope, $log, NgTableParams) {
      $scope.cols = [
      {field: "name", title: "Name"},
      {field: "action", title: "Action"}
      ];
      $scope.tableParams = new NgTableParams({},{
        dataset: [
        {name: "John Rambo", action: "None"},
        {name: "Forrest Gump", action: "None"},
        {name: "Юрий Лоза", action: "None"},
        ]
      });
    }]);

angular.module("mockApp")
.directive("partSearch", ["$log", function($log) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "views/PartSearch.html",
    transclude: true,
    controller: ["$parse", "$sce", "$log", "$q", "$location", "$scope", "ngTableParams",
    function($parse, $sce, $log, $q, $location, $scope, ngTableParams) {
    }]
  };
}]);
