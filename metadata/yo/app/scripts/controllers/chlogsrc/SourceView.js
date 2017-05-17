"use strict";

angular.module("ngMetaCrudApp")

.controller("ChangelogSourcesViewCtrl", [
    "$scope", "$log", "$location", "toastr", "NgTableParams", "$uibModal", "utils", "restService", "source",
  function($scope, $log, $location, toastr, NgTableParams, $uibModal, utils, restService, source) {

    $scope.source = source;
    
    $scope.attachmentsTableParams = new NgTableParams(
      {
        page: 1,
        count: 10,
        sorting: {}
      },
      {
        getData: utils.localPagination($scope.source.attachments)
      }
    );

    $scope.onViewList = function() {
      $location.path("/changelog/source/list");
    };

    $scope.onEdit = function() {
      $location.path("/changelog/source/" + $scope.source.id + "/form");
    };

    $scope.onRemove = function() {
      $uibModal.open({
        templateUrl: "/views/chlogsrc/ConfirmSourceDeleteDlg.html",
        animation: false,
        size: "lg",
        controller: "ConfirmSourceDeleteDlgCtrl",
        resolve: {
          "source": function() {
            return $scope.source;
          },
          "numExistedLinks": function() {
            return restService.getNumLinksForChangelogSource($scope.source.id);
          }
        }
      });
    };

  }

])
.controller("ConfirmSourceDeleteDlgCtrl",
  ["$scope", "$log", "$location", "toastr", "restService", "$uibModalInstance", "numExistedLinks", "source",
  function($scope, $log, $location, toastr, restService, $uibModalInstance, numExistedLinks, source) {

    $scope.data = {
      numExistedLinks: numExistedLinks
    };

    $scope.onCancel = function() {
        $uibModalInstance.close();
    };

    $scope.onDelete = function() {
        restService.removeChangelogSource(source.id).then(
          function success() {
            $uibModalInstance.close();
            toastr.success("The changelog source has been successfully removed.");
            $location.path("/changelog/source/list");
          },
          function failure(errorResponse) {
            $uibModalInstance.close();
            restService.error("Could not remove source.", errorResponse);
          }
        );
    };

    $scope.isDeleteBttnDisabled = function() {
      return false;
    };

  }
]);
