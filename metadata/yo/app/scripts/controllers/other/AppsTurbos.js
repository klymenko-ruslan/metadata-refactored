"use strict";

angular.module("ngMetaCrudApp")
.controller("AppsTurbosCtrl", ["$scope", "$log", "ngTableParams", "$uibModal", "gToast", "utils", "restService", "partTypes",
  function($scope, $log, ngTableParams, $uibModal, gToast, utils, restService, partTypes) {

    $scope.partTypes = partTypes;

    $scope.pickedParts = [];
    $scope.pickedPartsIds = {};

    $scope.pickedApps = [];
    $scope.pickedAppsIds = {};

    $scope.pickedPartsTableParams = new ngTableParams(
      {
        page: 1,
        count: 10,
        sorting: {}
      },
      {
        getData: utils.localPagination($scope.pickedParts)
      }
    );

    $scope.pickedAppsTableParams = new ngTableParams(
      {
        page: 1,
        count: 10,
        sorting: {}
      },
      {
        getData: utils.localPagination($scope.pickedApps)
      }
    );

    $scope.pickPart = function(part) {
      $scope.pickedParts.push(part);
      $scope.pickedPartsIds[part.id] = true;
      $scope.pickedPartsTableParams.reload();
    };

    $scope.unpickPart = function(id) {
      var idx = _.findIndex($scope.pickedParts, function(pt) {
        return pt.id === id;
      });
      var t = $scope.pickedParts[idx];
      $scope.pickedParts.splice(idx, 1);
      delete $scope.pickedPartsIds[t.id];
      $scope.pickedPartsTableParams.reload();
    };

    $scope.unpickAllParts = function() {
      _.each($scope.pickedParts, function(pt) {
        delete $scope.pickedPartsIds[pt.id];
      });
      $scope.pickedParts.splice(0, $scope.pickedParts.length);
      $scope.pickedPartsTableParams.reload();
    };

    $scope.pickApp = function(app) {
      $scope.pickedApps.push(app);
      $scope.pickedAppsIds[app.id] = true;
      $scope.pickedAppsTableParams.reload();
    };

    $scope.unpickApp = function(id) {
      var idx = _.findIndex($scope.pickedApps, function(pa) {
        return pa.id === id;
      });
      var a = $scope.pickedApps[idx];
      $scope.pickedApps.splice(idx, 1);
      delete $scope.pickedAppsIds[a.id];
      $scope.pickedAppsTableParams.reload();
    };

    $scope.unpickAllApps = function() {
      _.each($scope.pickedApps, function(pa) {
        delete $scope.pickedAppsIds[pa.id];
      });
      $scope.pickedApps.splice(0, $scope.pickedApps.length);
      $scope.pickedAppsTableParams.reload();
    };

    $scope.doIt = function() {
      var partIds = _.map($scope.pickedParts, function(pp) {
        return pp.id;
      });
      var appIds = _.map($scope.pickedApps, function(pa) {
        return pa.id;
      });
      restService.genTurboApps(partIds, appIds).then(
        function success(response) {
          $scope.unpickAllParts();
          $scope.unpickAllApps();
          if (response.failures.length > 0) {
            $uibModal.open({
              templateUrl: "/views/other/appsturbos/FailuresDlg.html",
              animation: false,
              size: "lg",
              controller: "FailuresDlgCtrl",
              resolve: {
                response: function() {
                  return response;
                }
              }
            });
          } else {
            gToast.open("Generated " + response.generated + " associations.");
          }
        },
        function failure(response) {
          restService.error("Association of the turbos and applications failed.", response);
        }
      );
    };

  }
])
.controller("FailuresDlgCtrl", ["$scope", "$log", "$location", "$uibModalInstance", "ngTableParams",
    "utils", "response",
  function($scope, $log, $location, $uibModalInstance, ngTableParams, utils, response) {

    $scope.response = response;

    $scope.failuresTableParams = new ngTableParams({
      page: 1,
      count: 10
    }, {
      getData: utils.localPagination(response.failures, "manufacturerPartNumber")
    });

    $scope.onClose = function() {
      $uibModalInstance.close();
    };

    $scope.showPart = function(partId) {
      $scope.onClose();
      $location.path("/part/" + partId);
    };

    $scope.showApp = function(appId) {
      $scope.onClose();
      $location.path("/application/carmodelengineyear/" + appId);
    };

}]);
