"use strict";

angular.module("ngMetaCrudApp")
.config(["ngTableFilterConfigProvider", function(ngTableFilterConfigProvider) {
    ngTableFilterConfigProvider.setConfig({
        aliasUrls: {
          'manufacturersClearBttn': 'filters/clearbutton.html'
        }
    });
}])
.controller("ManufacturerListCtrl", ["$scope", "$log", "ngTableParams", "dialogs", "restService", "manufacturerTypes",
  function($scope, $log, ngTableParams, dialogs, restService, manufacturerTypes) {

    $scope.manufacturerTypesOpts = _.map(manufacturerTypes, function (mt) {
      return { "id": mt.id, "title": mt.name };
    });

    $scope.manufacturerTypesOpts.unshift({ "id": null, "title": "" });

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
        restService.filterManufacturers(filter.name, filter.typeId, sortProperty, sortOrder, offset, limit).then(
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
    };

    $scope.onCreate = function() {
      alert("TODO");
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
                    msg += (" " + deleteResponse.refParts + " parts");
                  }
                  if (deleteResponse.refTurboTypes) {
                    if (deleteResponse.refParts) {
                      msg += " and";
                    }
                    msg += (" " + deleteResponse.refTurboTypes + " turbo types");
                  }
                  msg += ".";
                  dialogs.error("Rejected", msg);
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

  }]);
