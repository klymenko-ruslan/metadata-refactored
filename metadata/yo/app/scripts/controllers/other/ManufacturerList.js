"use strict";

angular.module("ngMetaCrudApp")
.config(["ngTableFilterConfigProvider", function(ngTableFilterConfigProvider) {
    ngTableFilterConfigProvider.setConfig({
        aliasUrls: {
          'manufacturersClearBttn': 'filters/clearbutton.html'
        }
    });
}])
.controller("ManufacturerListCtrl", ["$scope", "$log", "ngTableParams", "$uibModal", "dialogs", "restService", "manufacturerTypes",
  function($scope, $log, ngTableParams, $uibModal, dialogs, restService, manufacturerTypes) {

    $scope.manufacturerTypesOpts = _.map(manufacturerTypes, function (mt) {
      return { "id": mt.id, "title": mt.name };
    });
    $scope.manufacturerTypesOpts.unshift({ "id": null, "title": "" });

    $scope.notExternalOpts = [
      {id: null, title: ""},
      {id: true, title: "yes"},
      {id: false, title: "no"}
    ];

    $scope.manufacturersTableParams = new ngTableParams({
      page: 1,
      count: 25,
      sorting: {
        name: "asc"
      }
    }, {
      getData: function($defer, params) {
        var sortOrder;
        var sorting = params.sorting();
        for (var sortProperty in sorting) break;
        if (sortProperty) {
          sortOrder = sorting[sortProperty];
        }
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();
        var filter = params.filter();
        restService.filterManufacturers(filter.name, filter.typeId, filter.notExternal, sortProperty, sortOrder, offset, limit).then(
          function(result) {
            $defer.resolve(result.recs);
            params.total(result.total);
          },
          function(errorResponse) {
            restService.error("Filtering of the manufacturer list failed.", errorResponse);
            $defer.reject();
          });
      }
    });
    
    $scope.clearFilter = function() {
      var filter = $scope.manufacturersTableParams.filter();
      filter.name = null;
      filter.typeId = null;
      filter.notExternal = null;
    };

    $scope.onCreate = function() {
      $uibModal.open({
        templateUrl: "/views/manufacturer/create.html",
        animation: false,
        controller: "CreateManufacturerDlgCtrl",
        resolve: {
          manufacturerTypes: function() {
            return manufacturerTypes;
          },
          manufacturersTableParams: function() {
            return $scope.manufacturersTableParams;
          }
        }
      });
    };
    
    $scope.onEdit = function(m) {
      alert("TODO");
    };
    
    $scope.onRemove = function(m) {
      dialogs.confirm("Confirmation", "Are you sure? Do you want to remove this manufacturer ([" + m.id + "] - " + 
        m.name + ")?").result.then(
          function yes() {
            restService.deleteManufacturer(m.id).then(
              function(deleteResponse) {
                if (!deleteResponse.removed) {
                  var msg = "The manufacturer [" + m.id + "] - " + m.name +
                    " can't be deleted because it is referenced by ";
                  if (deleteResponse.refParts) {
                    msg += (" " + deleteResponse.refParts + " part(s)");
                  }
                  if (deleteResponse.refTurboTypes) {
                    if (deleteResponse.refParts) {
                      msg += " and";
                    }
                    msg += (" " + deleteResponse.refTurboTypes + " turbo type(s)");
                  }
                  msg += ".";
                  dialogs.error("Operation rejected", msg);
                } else {
                  $scope.manufacturersTableParams.reload();
                  gToast.open("The manufacturer [" + m.id + "] - " + m.name +
                    " has successfully been removed.");
                }
              },
              function(errorResponse) {
                restService.error("Could not remove the manufacturer.", errorResponse);
              }
            );
          },
          function no() {
          }
        );
    };

  }])
.controller("CreateManufacturerDlgCtrl", ["$scope", "$log", "$uibModalInstance", "gToast", "restService",
    "manufacturerTypes", "manufacturersTableParams",
  function($scope, $log, $uibModalInstance, gToast, restService, manufacturerTypes, manufacturersTableParams) {

    $scope.isBttnCreateDisabled = function(form) {
      return form.$invalid || $scope.isCreating;
    };

    $scope.onCreate = function() {
$log.log("manufacturer: " + angular.toJson($scope.manufacturer, 2));
      $scope.isCreating = true;
      $scope.isCreating = false;  // finally
      manufacturersTableParams.reload();
    };

    $scope.onClose = function() {
      $uibModalInstance.close();
    };

    // Initialization.

    $scope.manufacturerTypes = manufacturerTypes;
    
    $scope.isCreating = false;

    $scope.manufacturer = {
      name: null,
      notExternal: false,
      type: manufacturerTypes[0]
    };

  }
]);
