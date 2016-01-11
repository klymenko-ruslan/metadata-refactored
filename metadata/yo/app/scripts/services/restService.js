"use strict";

angular.module("ngMetaCrudApp")
  .service("restService", function RestService($log, Restangular, dialogs, $q, $rootScope) {
    return new function() { // jshint ignore:line
      var RestService = this;
      var refreshPromise = null;
      this.status = null;

      this.refreshStatus = function() {
        if (refreshPromise !== null) {
          return refreshPromise;
        } else {

          refreshPromise = Restangular.one("status/all").get();

          refreshPromise.then(function(status) {
            RestService.status = status;
            return status;
          });

          refreshPromise.finally(function() {
            refreshPromise = null;
          });

          return refreshPromise;
        }
      };

      // Wraps the BOM status logic, resolving when the BOM is not rebuilding.
      this.getBomRebuildingCompletePromise = function() {
        var deferred = $q.defer();

        RestService.refreshStatus().then(function(status) {
          if (status.bomRebuilding) {

            var cancelWatcher = $rootScope.$watch(
              function() {
                return RestService.status.bomRebuilding;
              },
              function(bomRebuilding) {
                if (bomRebuilding === false) {
                  deferred.resolve();
                  cancelWatcher();
                }
              }, true);
          } else {
            deferred.resolve();
          }
        });

        return deferred.promise;
      };
      this.error = function(title, response) {
        // NOOP on access denied, loginRequiredInterceptor will handle the redirect
        if (response.status === 401 || response.status === 403) {
          return;
        }
        $log.log(title, response);
        dialogs.error(title, 'Server said: <pre>' + response.data.message + '</pre>');
      };

      this.findPart = function(id, params) {
        return Restangular.one('part', id).get(params);
      };

      this.findPartByNumber = function(manufacturerId, pn) {
        return Restangular.one('part/numbers').get({
          "mid": manufacturerId,
          "pn": pn
        });
      };

      this.createPart = function(part) {
        // Specify class depending on part type.
        var clazz = "com.turbointernational.metadata.domain.part.types.";
        switch (part.partType.id) {
          case 1:
            clazz += "Turbo";
            break;
          case 2:
            clazz += "Cartridge";
            break;
          case 3:
            clazz += "Kit";
            break;
          case 4:
            clazz += "PistonRing";
            break;
          case 5:
            clazz += "JournalBearing";
            break;
          case 6:
            clazz += "Gasket";
            break;
          case 7:
            clazz += "BearingSpacer";
            break;
          case 11:
            clazz += "CompressorWheel";
            break;
          case 12:
            clazz += "TurbineWheel";
            break;
          case 13:
            clazz += "BearingHousing";
            break;
          case 14:
            clazz += "Backplate";
            break;
          case 15:
            clazz += "Heatshield";
            break;
          case 16:
            clazz += "NozzleRing";
            break;
          default:
            clazz = "com.turbointernational.metadata.domain.part.Part";
        }
        part.class = clazz;
        return Restangular.all('part').post(part);
      };

      this.updatePart = function(part) {
        return Restangular.one("part", part.id).customPUT(part);
      };

      this.deletePart = function(part) {
        return Restangular.remove(part);
      };

      this.findCarmodelengineyear = function(cmey_id) {
        return Restangular.one("application/carmodelengineyear", cmey_id).get();
      };

      this.createCarmodelengineyear = function(cmey) {
        return Restangular.one("application").post("carmodelengineyear", cmey);
      };

      this.updateCarmodelengineyear = function(cmey) {
        return Restangular.one("application/carmodelengineyear", cmey.id).customPUT(cmey);
      };

      this.removeCarmodelengineyear = function(id) {
        return Restangular.one("/application/carmodelengineyear/" + id).remove();
      };

      this.findPartApplications = function(part_id) {
        return Restangular.one("part", part_id).getList("application");
      };

      this.addPartApplications = function(part_id, applications) {
        var ids = [];
        angular.forEach(applications, function(val) {
          ids.push(val.id);
        });
        return Restangular.one("part", part_id).post("application", ids);
      };

      this.removePartApplication = function(part_id, application_id) {
        return Restangular.one("part/" + part_id + "/application/" + application_id).remove();
      };

      this.listManufacturers = function(first, count) {
        return Restangular.all("other/manufacturer", {
          first: first,
          count: count
        }).getList();
      };

      this.listPartTypes = function() {
        return Restangular.all("type/part").getList();
      };

      this.findManufacturer = function(id) {
        return Restangular.one("manufacturer", id).get();
      };

      this.listTurboTypesForManufacturerId = function(manufacturerId) {
        return Restangular.all("other/turboType").getList({
          "manufacturerId": manufacturerId
        });
      };

      this.listTurboModelsForTurboTypeId = function(turboTypeId) {
        return Restangular.all("other/turboModel").getList({
          "turboTypeId": turboTypeId
        });
      };

      this.findInterchange = function(id) {
        return Restangular.one("interchange", id).get();
      };

      this.findCarmakeByName = function(name) {
        return Restangular.one("application/carmake").get({
          "name": name
        });
      };

      this.findAllCarMakesOrderedByName = function() {
        return Restangular.one("application").getList("carmakes");
      };

      this.createCarmake = function(carmake) {
        return Restangular.one("application").post("carmake", carmake);
      };

      this.updateCarmake = function(carmake) {
        return Restangular.one("application/carmake", carmake.id).customPUT(carmake);
      };

      this.removeCarmake = function(id) {
        return Restangular.one("application/carmake", id).remove();
      };

      this.findCarfueltypeByName = function(name) {
        return Restangular.one("application/carfueltype").get({
          "name": name
        });
      };

      this.findAllCarFuelTypesOrderedByName = function() {
        return Restangular.one("application").getList("carfueltypes");
      };

      this.createCarfueltype = function(carfueltype) {
        return Restangular.one("application").post("carfueltype", carfueltype);
      };

      this.updateCarfueltype = function(carfueltype) {
        return Restangular.one("application/carfueltype", carfueltype.id).customPUT(carfueltype);
      };

      this.removeCarfueltype = function(id) {
        return Restangular.one("application/carfueltype", id).remove();
      };

      this.findCarmodel = function(id) {
        return Restangular.one('application/carmodel', id).get();
      };

      this.createCarmodel = function(carmodel) {
        return Restangular.one("application").post("carmodel", carmodel);
      };

      this.updateCarmodel = function(carmodel) {
        return Restangular.one("application/carmodel", carmodel.id).customPUT(carmodel);
      };

      this.removeCarmodel = function(id) {
        return Restangular.one("application/carmodel", id).remove();
      };

      this.findCarModelsOfMake = function(makeId) {
        return Restangular.one("application").getList("carmodels", {
          makeId: makeId
        });
      };

      this.findCarengine = function(id) {
        return Restangular.one("application/carengine", id).get();
      };

      this.createCarengine = function(carengine) {
        return Restangular.one("application").post("carengine", carengine);
      };

      this.updateCarengine = function(carengine) {
        return Restangular.one("application/carengine", carengine.id).customPUT(carengine);
      };

      this.removeCarengine = function(id) {
        return Restangular.one("application/carengine", id).remove();
      };

      this.findAllCarEnginesOrderedByName = function() {
        return Restangular.one("application").getList("carengines");
      };

      this.findCarYearByName = function(name) {
        return Restangular.one("application/caryear").get({
          "name": name
        });
      };

    };
  });
