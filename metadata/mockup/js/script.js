"use strict";

angular.module("mockApp", ["ngRoute", "ngTable", "ui.bootstrap",
        "restangular", /*"dialogs.main",*/ "gToast" ]);



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

 
