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

      $scope.pickRecord = function(row) {
        alert("Picked: " + row.name);
      };

    }]);

angular.module("mockApp")
.controller("changelogDlgCtrl",["$scope", "$log", "$uibModalInstance", function($scope, $log, $uibModalInstance) {

  $scope.onCloseChangelogDetails = function() {
    $uibModalInstance.close();
  };

}]);

angular.module("mockApp")
.directive('ngHtmlCompile', function($compile) {
  /**
   * The directive was copy-pasted from:
   *  * https://github.com/francisbouvier/ng_html_compile
   * Thank you Francis Bouvier for your perfect work :)
   */
  return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(attrs.ngHtmlCompile, function(newValue, oldValue) {
          element.html(newValue);
          $compile(element.contents())(scope);
        });
      }
  }
})
.directive("partSearch", ["$log", function($log) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "views/PartSearch.html",
    transclude: true,
    controller: ["$parse", "$sce", "$log", "$q", "$scope", "NgTableParams",
      "$transclude",
    function($parse, $sce, $log, $q, $scope, NgTableParams, $transclude) {
      //var transcludedHtml = $transclude();
      var transcludedHtml = "";
      $transclude(function(clone) {
        clone.each(function(idx, node) {
          if (node.outerHTML) {
            transcludedHtml += node.outerHTML;
          }
        });
        //transcludedHtml = clone[0].outerHTML;
      });
      $scope.tableDataSet = [
        {name: "John Rambo", action: "None"},
        {name: "Forrest Gump", action: "None"},
        {name: "Юрий Лоза", action: "None"}
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
            //var dataSet = $scope.tableDataSet;
            var dataSet = angular.copy($scope.tableDataSet);
            _.each(dataSet, function(rec) {
              rec.action = transcludedHtml;
            });
            $defer.resolve(dataSet);
            params.total(dataSet.length);
          }
        });
    }]
  };
}]);
