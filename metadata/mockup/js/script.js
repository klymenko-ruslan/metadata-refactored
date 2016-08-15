"use strict";

angular.module("mockApp", ["ngRoute", "ngTable", "ui.bootstrap",
    "restangular", /*"dialogs.main",*/ "gToast" ]);


angular.module("mockApp")
.controller("indexCtrl", ["$scope", "$log", "NgTableParams",
    "$uibModal", /*"$uibModalInstance",*/
    function($scope, $log, NgTableParams, $uibModal /*, $uibModalInstance*/) {
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

      $scope.onOpenChangelogDetails = function() {
        $log.log("onOpenChangelogDetais");
        $uibModal.open({
          templateUrl: "views/ChangelogDlg.html",
          size: "lg",
          controller: "changelogDlgCtrl"
          /*controller: "indexCtrl"*/
        });
      };

      $scope.onCloseChangelogDetails = function() {
        $uibModalInstance.close();
      };

    }]);

angular.module("mockApp")
.controller("changelogDlgCtrl",["$scope", "$log", "$uibModalInstance", function($scope, $log, $uibModalInstance) {

  $scope.onCloseChangelogDetails = function() {
    $uibModalInstance.close();
  };

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
