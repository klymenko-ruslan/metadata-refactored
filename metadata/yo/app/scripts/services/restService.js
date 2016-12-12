"use strict";

angular.module("ngMetaCrudApp")
  .service("restService", ["$log", "$http", "Restangular", "dialogs", "$q", "$rootScope", "$filter",
      "METADATA_BASE", "DATE_FORMAT",
      function RestService($log, $http, Restangular, dialogs, $q, $rootScope, $filter, METADATA_BASE, DATE_FORMAT) {

    return new function() { // jshint ignore:line
      var RestService = this;
      var refreshPromise = null;
      this.status = null;

      this.refreshStatus = function() {
        if (refreshPromise !== null) {
          return refreshPromise;
        } else {

          var url = METADATA_BASE + "status/all";

          // We use $http service instead of Restangular because
          // we should hide indication on UI of this call by 'angular-loading-bar' service.
          // That service relies on $http only and know nothing about Restangular.

          refreshPromise = $http.get(url, {
            ignoreLoadingBar: true
          }).then(function(status) {
            RestService.status = status.data;
            return status;
          }).finally(function() {
            refreshPromise = null;
          });

          return refreshPromise;
        }
      };

      this.startBomRebuilding = function(options) {
        // return Restangular.one("bom/rebuild").post("start", options);
        var url = METADATA_BASE + "bom/rebuild/start";
        return $http.post(url, options);
      };

      this.getBomRebuildingStatus = function() {
        // return Restangular.one("bom/rebuild/status").get();
        var url = METADATA_BASE + "bom/rebuild/status";
        return $http.get(url, { ignoreLoadingBar: true });
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

      this.httpServiceError = function(title, response) {
        if (response.status == -1) {
          dialogs.error(title, "Server not responding.");
        } else if (response.status === 401 || response.status === 403) {
          return;
        } else {
          dialogs.error(title, "Server said: <pre>" + angular.toJson(response, 2) + "</pre>");
        }
      }

      this.error = function(title, response) {
        // NOOP on access denied, loginRequiredInterceptor will handle the redirect
        if (response.status === 401 || response.status === 403) {
          return;
        }
        dialogs.error(title, "Server said: <pre>" + angular.toJson(response, 2) + "</pre>");
      };

      this.getCurrentUser = function() {
        return Restangular.one("security/user/me").get();
      };

      this.findActiveUsers = function() {
        return Restangular.all("security/user").getList();
      };

      this.findAllUsers = function() {
        return Restangular.all("security/user/list").getList();
      };

      this.findPart = function(id, params) {
        return Restangular.one("part", id).get(params);
      };

      this.findPartByNumber = function(manufacturerId, pn) {
        return Restangular.one("part/numbers").get({
          "mid": manufacturerId,
          "pn": pn
        });
      };

      this.createPart = function(part, mpns) {
        // Specify class depending on part type.
        var clazz = "com.turbointernational.metadata.entity.part.types.";
        switch (part.partType.id) {
        case 30:
          clazz += "Actuator";
          break;
        case 31:
          clazz += "CompressorCover";
          break;
        case 2:
          clazz += "Cartridge";
          break;
        case 32:
          clazz += "Plug";
          break;
        case 33:
          clazz += "TurbineHousing";
          break;
        case 1:
          clazz += "Turbo";
          break;
        case 34:
          clazz += "Backplate";
          break;
        case 13:
          clazz += "BearingHousing";
          break;
        case 35:
          clazz += "BoltScrew";
          break;
        case 19:
          clazz += "Clamp";
          break;
        case 11:
          clazz += "CompressorWheel";
          break;
        case 36:
          clazz += "Fitting";
          break;
        case 6:
          clazz += "Gasket";
          break;
        case 15:
          clazz += "HeatshieldShroud";
          break;
        case 5:
          clazz += "JournalBearing";
          break;
        case 37:
          clazz += "JournalBearingSpacer";
          break;
        case 16:
          clazz += "NozzleRing";
          break;
        case 38:
          clazz += "Nut";
          break;
        case 18:
          clazz += "OilDeflector";
          break;
        case 17:
          clazz += "ORing";
          break;
        case 39:
          clazz += "Pin";
          break;
        case 4:
          clazz += "PistonRing";
          break;
        case 40:
          clazz += "RetainingRing";
          break;
        case 41:
          clazz += "SealPlate";
          break;
        case 12:
          clazz += "TurbineWheel";
          break;
        case 42:
          clazz += "Spring";
          break;
        case 43:
          clazz += "ThrustBearing";
          break;
        case 44:
          clazz += "ThrustCollar";
          break;
        case 45:
          clazz += "ThrustSpacer";
          break;
        case 46:
          clazz += "ThrustWasher";
          break;
        case 47:
          clazz += "Washer";
          break;
        case 48:
          clazz += "CarbonSeal";
          break;
        case 49:
          clazz += "GasketKit";
          break;
        case 50:
          clazz += "Misc";
          break;
        case 51:
          clazz += "P";
          break;
        case 52:
          clazz += "Shroud";
          break;
        case 3:
          clazz += "Kit";
          break;
        case 7:
          clazz += "BearingSpacer";
          break;
        case 8:
          clazz += "FastWearingComponent";
          break;
        case 9:
          clazz += "MajorComponent";
          break;
        case 10:
          clazz += "MinorComponent";
          break;
        case 14:
          clazz += "BackplateSealplate";
          break;
        case 20:
          clazz += "ThrustPart";
          break;
        case 21:
          clazz += "MiscMinorComponent";
          break;
        default:
          clazz = "com.turbointernational.metadata.entity.part.Part";
        }
        part.class = clazz;
        var request = {
          "origin": part,
          "partNumbers": mpns
        };
        return Restangular.all("part").post(request);
      };

      this.updatePart = function(part) {
        return Restangular.one("part", part.id).customPUT(part);
      };

      this.addTurboTypeToPart = function(partId, turboTypeId) {
        Restangular.setParentless(false);
        return Restangular.one("part", partId).one("turboType", turboTypeId).post();
      };

      this.deletePart = function(part) {
        return Restangular.remove(part);
      };

      this.setGasketKitForPart = function(partId, gasketkitId) {
        Restangular.setParentless(false);
        return Restangular.one("part", partId).one("gasketkit", gasketkitId).put();
      };

      this.clearGasketKitInPart = function(partId) {
        Restangular.setParentless(false);
        return Restangular.one("part", partId).one("gasketkit").remove();
      };

      this.unlinkTurboInGasketKit = function(turboId) {
        Restangular.setParentless(false);
        return Restangular.one("part", turboId).one("gasketkit2").remove();
      };

      this.linkTurbosToGasketKit = function(gasketKitId, pickedTurboIds) {
        Restangular.setParentless(false);
        var request = {
          "gasketKitId": gasketKitId,
          "pickedTurbos": pickedTurboIds
        };
        return Restangular.one("part", gasketKitId).one("gasketkits").customPUT(request);
      };

      this._upload = function(url, bytes) {
        var fd = new FormData();
        fd.append("file", bytes);
        return $http.post(url, fd, {
          transformRequest: angular.identity,
          headers: {"Content-Type": undefined}
        });
      };

      this.uploadPartCritDimsLegend = function(partId, imgBytes) {
        var url = METADATA_BASE + "part/" + partId + "/cdlegend/image";
        return this._upload(url, imgBytes);
      };

      this.deletePartCritdimsLegend = function(partId) {
        return Restangular.one("/image/" + partId + "/cdlegend.jpg").remove();
      };

      this.uploadPartTypeLegend = function(partTypeId, imgBytes) {
        var url = METADATA_BASE + "parttype/" + partTypeId + "/ptlegend/image";
        return this._upload(url, imgBytes);
      };

      this.deletePartTypeLegend = function(partTypeId) {
        return Restangular.one("/image/" + partTypeId + "/ptlegend.jpg").remove();
      };

      this.findCarmodelengineyear = function(cmey_id) {
        return Restangular.one("application/carmodelengineyear", cmey_id).get();
      };

      this.existsCarmodelengineyear = function(carModelId, carEngineId, year) {
        return Restangular.one("application/carmodelengineyear/exists").get({
          carModelId: carModelId,
          carEngineId: carEngineId,
          year: year
        });
      };

      this.createCarmodelengineyear = function(cmey) {
        return Restangular.one("application").post("carmodelengineyear", cmey);
      };

      this.carmodelengineyearBulkCreate = function(pickedModels, pickedEngines, pickedYears) {
        return Restangular.one("application/carmodelengineyear").post("bulkcreate", {
          models: pickedModels,
          engines: pickedEngines,
          years: pickedYears
        });
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

      this.listManufacturers = function() {
        return Restangular.all("other/manufacturer/list").getList();
      };

      this.listTurbosLinkedToGasketKit = function(gasketkit_id) {
        return Restangular.one("part/" + gasketkit_id + "/gasketkit").getList("turbos");
      };

      this.findManufacturer = function(id) {
        return Restangular.one("manufacturer", id).get();
      };

      this.findPartType = function(id) {
        return Restangular.one("parttype/json", id).get();
      };

      this.listPartTypes = function() {
        return Restangular.all("parttype/json/list").getList();
      };

      this.listCoolTypes = function() {
        return Restangular.all("cooltype/list").getList();
      };

      this.listKitTypes = function() {
        return Restangular.all("kittype/list").getList();
      };

      this.genTurboApps = function(partIds, appIds) {
        return Restangular.all("other/appsturbos/generate").post({
          partIds: partIds,
          appIds: appIds
        });
      };

      this.findTurboTypeByManufacturerAndName = function(manufacturerId, name) {
        return Restangular.one("other/turboType").get({"manufacturerId": manufacturerId, "name": name});
      };

      this.listTurboTypesForManufacturerId = function(manufacturerId) {
        return Restangular.one("other/turboType").getList("list", {"manufacturerId": manufacturerId});
      };

      this.listTurboModelsForTurboTypeId = function(turboTypeId) {
        return Restangular.one("other").getList("turboModel", {"turboTypeId": turboTypeId});
      };

      this.createTurboType = function(manufacturerId, name) {
        var turboType = {
          name: name,
          manufacturer: {
            id: manufacturerId
          }
        };
        return Restangular.all("other/turboType").post(turboType);
      };

      this.renameTurboType = function(turboType) {
        return Restangular.all("other/turboType").customPUT(turboType);
      };

      this.deleteTurboType = function(ttId) {
        Restangular.setParentless(false);
        return Restangular.one("other/turboType", ttId).remove();
      };

      this.removeTurboType = function(partId, turboTypeId) {
        Restangular.setParentless(false);
        return Restangular.one("part", partId).one("turboType", turboTypeId).remove();
      };

      this.createTurboModel = function(ttId, name) {
        var turboModel = {
          name: name,
          turboType: {
            id: ttId
          }
        };
        return Restangular.all("other/turboModel").post(turboModel);
      };

      this.renameTurboModel = function(turboModel) {
        return Restangular.all("other/turboModel").customPUT(turboModel);
      };

      this.deleteTurboModel = function(tmId) {
        Restangular.setParentless(false);
        return Restangular.one("other/turboModel", tmId).remove();
      };

      this.findInterchange = function(id) {
        return Restangular.one("interchange", id).get();
      };

      this.createPartInterchange = function(partId, pickedPartId) {
        var interchange = {
          parts: [{
            id: partId
          }, {
            id: pickedPartId
          }]
        };
        return Restangular.all("interchange").post(interchange);
      };

      this.updatePartInterchange = function(partId, pickedPartId, mergeChoice) {
        Restangular.setParentless(false);
        return Restangular.one("interchange", partId).one("part", pickedPartId).put({mergeChoice: mergeChoice});
      };

      this.deletePartInterchange = function(partId, interchangeId) {
        Restangular.setParentless(false);
        return Restangular.one("interchange", interchangeId).one("part", partId).remove();
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
        return Restangular.one("application/carmodel", id).get();
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

      this.findAllCarEnginesOrderedByName = function(detailed) {
        return Restangular.one("application").getList("carengines", {detailed: false}); // TODO: detailed
      };

      this.existsCarengine = function(engineSize, fuelTypeId) {
        return Restangular.one("application/carengine/exists").get({
          engineSize: engineSize,
          fuelTypeId: fuelTypeId
        });
      };

      this.findCarYearByName = function(name) {
        return Restangular.one("application/caryear").get({
          "name": name
        });
      };

      this.findMas90SyncHistory = function(startPosition, maxResults) {
        return Restangular.one("mas90sync/history").get({
          "start": startPosition,
          "max": maxResults
        });
      };

      this.mas90SyncResult = function(id) {
        return Restangular.one("mas90sync/result", id).get();
      };

      this.startMas90Sync = function() {
        return Restangular.one("mas90sync/start").post();
      };

      this.statusMas90Sync = function() {
        return Restangular.one("mas90sync/status").get();
      };

      this.startIndexing = function(toIndex) {
        // return Restangular.one("search/indexing").post("start", toIndex);
        var url = METADATA_BASE + "search/indexing/start";
        return $http.post(url, toIndex);
      };

      this.getIndexingStatus = function() {
        // return Restangular.one("search/indexing/status").get();
        var url = METADATA_BASE + "search/indexing/status";
        return $http.get(url, { ignoreLoadingBar: true });
      };

      this.filterParts = function(searchPartTypeId, searchManufacturerName, searchName, searchPartNumber,
          searchInactive, searchTurboModelName, searchTurboTypeName,
          year, make, model, engine, fuelType, searchCritDims,
          sortProperty, sortOrder, offset, limit) {
        var params = {
          partNumber: searchPartNumber,
          inactive: searchInactive,
          partTypeId: searchPartTypeId,
          manufacturerName: searchManufacturerName,
          turboModelName: searchTurboModelName,
          turboTypeName: searchTurboTypeName,
          name: searchName,
          year: year,
          make: make,
          model: model,
          engine: engine,
          fuelType: fuelType,
          pgSortProperty: sortProperty,
          pgSortOrder: sortOrder,
          pgOffset: offset,
          pgLimit: limit
        };
        _.each(searchCritDims, function(val, key) {
          // Normalize enumerations.
          if (_.isObject(val)) {
            val = val.id;
          }
          params[key] = val;
        });
        return Restangular.one("search/parts").get(params);
      };

      this.filterCarModelEngineYears = function(cmey, year, make, model, engine, fuel, sortProperty, sortOrder,
        offset, limit) {
        return Restangular.one("search/carmodelengineyears").get({
          "carModelEngineYear": cmey,
          "year": year,
          "make": make,
          "model": model,
          "engine": engine,
          "fuel": fuel,
          "sortProperty": sortProperty,
          "sortOrder": sortOrder,
          "offset": offset,
          "limit": limit
        });
      };

      this.filterCarMakes = function(make, sortProperty, sortOrder, offset, limit) {
        return Restangular.one("search/carmakes").get({
          "make": make,
          "sortProperty": sortProperty,
          "sortOrder": sortOrder,
          "offset": offset,
          "limit": limit
        });
      };

      this.filterCarModels = function(model, make, sortProperty, sortOrder, offset, limit) {
        return Restangular.one("search/carmodels").get({
          "model": model,
          "make": make,
          "sortProperty": sortProperty,
          "sortOrder": sortOrder,
          "offset": offset,
          "limit": limit
        });
      };

      this.filterCarEngines = function(engine, fuelType, sortProperty, sortOrder, offset, limit) {
        return Restangular.one("search/carengines").get({
          "engine": engine,
          "fuelType": fuelType,
          "sortProperty": sortProperty,
          "sortOrder": sortOrder,
          "offset": offset,
          "limit": limit
        });
      };

      this.filterCarFuelTypes = function(fuelType, sortProperty, sortOrder, offset, limit) {
        return Restangular.one("search/carfueltypes").get({
          "fuelType": fuelType,
          "sortProperty": sortProperty,
          "sortOrder": sortOrder,
          "offset": offset,
          "limit": limit
        });
      };

      this.filterSalesNotes = function(partNumber, comment, primaryPartId, includePrimary, includeRelated, states,
          sortProperty, sortOrder, offset, limit) {
        return Restangular.one("search/salesnotes").get({
          "partNumber": partNumber,
          "comment": comment,
          "primaryPartId": primaryPartId,
          "includePrimary": includePrimary,
          "includeRelated": includeRelated,
          "states": states,
          "sortProperty": sortProperty,
          "sortOrder": sortOrder,
          "offset": offset,
          "limit": limit
        });
      };

      this.filterChangelog = function(startDate, finishDate, service, userId, description, data,
        sortProperty, sortOrder, offset, limit) {
        if (startDate) {
          startDate = $filter("date")(startDate, DATE_FORMAT);
        }
        if (finishDate) {
          finishDate = $filter("date")(finishDate, DATE_FORMAT);
        }
        return Restangular.one("changelog/list").get({
          "startDate": startDate,
          "finishDate": finishDate,
          "service": service,
          "userId": userId,
          "description": description,
          "data": data,
          "sortProperty": sortProperty,
          "sortOrder": sortOrder,
          "offset": offset,
          "limit": limit
        });
      };

      this.findPrimaryPartIdForThePart = function(id) {
        return Restangular.one("other/salesNote/primarypartidforthepart").get({
          "partId": id
        });
      };

      this.findSalesNote = function(id) {
        return Restangular.one("other/salesNote", id).get();
      };

      this.updateSalesNote = function(id, comment) {
        return Restangular.one("other/salesNote").post(id, { "comment": comment });
      };

      this.getAllAuthProviders = function(sortProperty, sortOrder, offset, limit) {
        return Restangular.one("authprovider/list").get({
          "sortProperty": sortProperty,
          "sortOrder": sortOrder,
          "offset": offset,
          "limit": limit
        });
      };

      this.createAuthProviderLDAP = function(authProviderLDAP) {
        return Restangular.one("authprovider").post("create", authProviderLDAP);
      };

      this.updateAuthProviderLDAP = function(authProviderLDAP) {
        return Restangular.one("authprovider", authProviderLDAP.id).customPUT(authProviderLDAP);
      };

      this.removeAuthenticationProvider = function(id) {
        return Restangular.one("authprovider", id).remove();
      };

      this.findAuthProviderLdapByName = function(name) {
        return Restangular.one("authprovider/findbyname").get({"name": name});
      };

      this.removeSalesNote = function(id) {
        return Restangular.one("other/salesNote", id).remove();
      };

      this.findCriticalDimensionsForThePart = function(id) {
        return Restangular.one("/criticaldimension/part", id).get();
      };

      this.getCritDimsByPartTypes = function(indexBy) {
        if (!indexBy) {
          indexBy = "ID";
        }
        if (indexBy != "ID" && indexBy != "NAME") {
          throw "Unexpected value of 'indexBy': " + angular.toJson(indexBy);
        }
        return Restangular.one("/criticaldimension/byparttypes").get({
          "indexBy": indexBy
        });
      };

      this.getAllCritDimEnums = function() {
        return Restangular.all("/criticaldimension/enum/list").getList();
      };

      this.getAllCritDimEnumVals = function() {
        return Restangular.all("/criticaldimension/enums/vals").getList();
      };

      this.getCritDimEnumVals = function(id) {
        return Restangular.one("/criticaldimension/enum", id).getList("list");
      };

      this.addCritDimEnum = function(newEnum) {
        return Restangular.one("/criticaldimension").post("enum", newEnum);
      };

      this.addCritDimEnumItm = function(enumId, newEnumItm) {
        return Restangular.one("/criticaldimension/enum", enumId).post("item", newEnumItm);
      };

      this.removeCritDimEnum = function(id) {
        return Restangular.one("/criticaldimension/enum", id).remove();
      };

      this.removeCritDimEnumItm = function(id) {
        return Restangular.one("/criticaldimension/enum/item", id).remove();
      };

      this.updateCritDimEnum = function(cde) {
        return Restangular.one("/criticaldimension/enum", cde.id).customPUT(cde);
      };

      this.updateCritDimEnumItm = function(cdev) {
        return Restangular.one("/criticaldimension/enum/item", cdev.id).customPUT(cdev);
      };

      this.findCritDimEnumByName = function(name) {
        return Restangular.one("/criticaldimension/enum").get({"name": name});
      };

      this.findCritDimEnumItmByName = function(enumId, name) {
        return Restangular.one("/criticaldimension/enum/" + enumId + "/items").get({"name": name});
      };

    };
  }]);
