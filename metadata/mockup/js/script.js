"use strict";

angular.module("mockApp", ["ngRoute", "ngTable", "ui.bootstrap",
    "restangular", /*"dialogs.main",*/ "gToast" ]);


angular.module("mockApp")
.controller("indexCtrl", ["$scope", "$log", "$uibModal",
    function($scope, $log, $uibModal) {
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

      $scope.pickRecord = function() {
        alert("The record has been picket.");
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
    controller: ["$parse", "$sce", "$log", "$q", "$scope", "NgTableParams",
      "$transclude",
    function($parse, $sce, $log, $q, $scope, NgTableParams, $transclude) {

      var transcludedHtml = null;
      $transclude(function(clone) {
        transcludedHtml = clone[0].outerHTML;
      });
      $log.log("transcludedHtml: " + transcludedHtml);
      $scope.tableDataSet = [
        {name: $sce.trustAsHtml("John Rambo"), action: $sce.trustAsHtml("None")},
        {name: $sce.trustAsHtml("Forrest Gump"), action: $sce.trustAsHtml("None")},
        {name: $sce.trustAsHtml("Юрий Лоза"), action: $sce.trustAsHtml("None")}
      ];
      $scope.columns = [
        {field: "name", title: "Name"},
        {field: "action", title: "Action"}
      ];
      $scope.tableParams = new NgTableParams({
          page: 1,
          count: 10,
          sorting: {} 
        },{
          getData: function($defer, params) {
            var dataSet = angular.copy($scope.tableDataSet);
            _.each(dataSet, function(rec) {
              rec.action = $sce.trustAsHtml(transcludedHtml);
            });
            $defer.resolve(dataSet);
            params.total(dataSet.length);
          }
        });
    }]
  };
}]);
