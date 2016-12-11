"use strict";

angular.module("ngMetaCrudApp")
.controller("PartDetailCtrl", ["$scope", "$log", "$q", "$location", "$routeParams", "Kits", "ngTableParams", "utils",
    "restService", "Restangular", "User", "$uibModal", "dialogs", "gToast", "part", "criticalDimensions",
    "manufacturers", "turbos",
    function ($scope, $log, $q, $location, $routeParams, Kits, ngTableParams, utils,
    restService, Restangular, User, $uibModal, dialogs, gToast, part, criticalDimensions, manufacturers,
    turbos) {
  $scope.partId = part.id;
  $scope.part = part;
  $scope.formMode = "view";
  $scope.criticalDimensions = criticalDimensions;
  // Make sure we're using the correct part type
  $scope.partType = part.partType.name;
  $scope.turbosTableParams = new ngTableParams({
    "page": 1,
    "count": 10,
    "sorting": {
      "id": "asc"
    }
  }, {
    "getData": utils.localPagination(turbos, "id")
  });
  // TODO: Find a better way. Directive?
  if (part.partType.magentoAttributeSet == "Kit") {
    $scope.kitComponents = Kits.listComponents($scope.partId).then(
      function(components) {
        $scope.kitComponents  = components;
      },
      function (error) {
        restService.error("Can't load kits.", error);
      }
    );
  }

  // =============

  $scope.manufacturers = manufacturers;
  $scope.turboTypes = [];
  $scope.turboModels = [];

  $scope.turbo ={
    tm: null,
    tt: null
  };

  $scope.filters = {
    turboType: "",
    turboModel: ""
  };

  if ($scope.part.partType.magentoAttributeSet === "Turbo") {
    $scope.turbo.tm = part.turboModel;
    $scope.turbo.tt = part.turboModel.turboType;
  }

  $scope.oldPart = Restangular.copy(part);

  $scope.onViewPart = function() {
    $location.path("/part/" + $scope.partId);
  };

  $scope.onChangeManufacturer = function() {
    if ($scope.part.partType.magentoAttributeSet == "Turbo") {
      var mnfrId = $scope.part.manufacturer.id;
      restService.listTurboTypesForManufacturerId(mnfrId).then(
        function success(turboTypes) {
          $scope.turboTypes.splice(0, $scope.turboTypes.length);
          _.each(turboTypes, function(tt) {
            $scope.turboTypes.push(tt);
          });
        },
        function failure(response) {
          restService.error("Loading of turbo types for the manufacturer [" + mnfrId + "] - " +
                            $scope.part.manufacturer.name + " failed.", response);
        }
      );
    }
  };

  $scope.onChangeTurboType = function() {
    if ($scope.part.partType.magentoAttributeSet !== "Turbo") {
      return;
    }
    var ttId = $scope.turbo.tt.id;
    if (ttId !== undefined) {
      restService.listTurboModelsForTurboTypeId(ttId).then(
        function success(turboModels) {
          $scope.turboModels.splice(0, $scope.turboModels.length);
          _.each(turboModels, function(tm) {
            $scope.turboModels.push(tm);
          });
        },
        function failure(response) {
          restService.error("Loading of turbo models for the turbo type [" + ttId + "] - " +
                            $scope.part.turboModel.turboType.name + " failed.", response);
        }
      );
    }
  };

  $scope.onChangeManufacturer();
  $scope.onChangeTurboType();

  $scope.revert = function() {
    $scope.part = Restangular.copy($scope.oldPart);
    $scope.partForm.$setPristine(true);
    $scope.$broadcast("revert");
  };

  $scope.isManufacturerEnabled = function() {
    return User.hasRole("ROLE_ALTER_PART_MANUFACTURER");
  };

  $scope.isPnEnabled = function() {
    return User.hasRole("ROLE_ALTER_PART_NUMBER");
  };

  $scope.onEditSave = function() {

    var url = "part";

    if ($scope.part.partType.magentoAttributeSet === "Turbo") {
      $scope.part.turboModel = $scope.turbo.tm;
      $scope.part.turboModel.turboType = $scope.turbo.tt;
    }

    restService.updatePart($scope.part).then(
      function(part) {
        $scope.part = part;
        $scope.onEditCancel(); // close form
      },
      function(response) {
        restService.error("Could not update part", response);
      }
    );

  };

  $scope.createTurboType = function() {
    $uibModal.open({
      templateUrl: "/views/part/TurboTypeCreateDlg.html",
      animation: false,
      size: "lg",
      controller: "CreateTurboTypeDlgCtrl",
      resolve: {
        create: function() {
          return true;
        },
        turbo: function() {
          return $scope.turbo;
        },
        part: function() {
          return $scope.part;
        },
        turboTypes: function() {
          return $scope.turboTypes;
        }
      }
    });
  };

  $scope.renameTurboType = function() {
    $uibModal.open({
      templateUrl: "/views/part/TurboTypeCreateDlg.html",
      animation: false,
      size: "lg",
      controller: "CreateTurboTypeDlgCtrl",
      resolve: {
        create: function() {
          return false;
        },
        turbo: function() {
          return $scope.turbo;
        },
        part: function() {
          return $scope.part;
        },
        turboTypes: function() {
          return $scope.turboTypes;
        }
      }
    });
  };

  $scope.deleteTurboType = function() {
    var ttId = $scope.turbo.tt.id;
    dialogs.confirm(
      "Delete Turbo Type?",
      "Do you want to delete this turbo type?").result.then(
      function() {
        // Yes
        restService.deleteTurboType(ttId).then(
          function() {
            // Success
            gToast.open("Turbo type deleted.");
            $scope.turbo.tt = {};
            var idx = _.findIndex($scope.turboTypes, function(tt) {
              return tt.id === ttId;
            });
            if (idx !== -1) {
              $scope.turboTypes.splice(idx, 1);
            }
          },
          function() {
            // Error
            dialogs.error(
              "Could not delete turbo type.",
              "Turbo type must not be used for any parts or turbo models. Check server log for details.");
          });
      },
      function() {
        // No
      }
    );
  };

  $scope.createTurboModel = function() {
    $uibModal.open({
      templateUrl: "/views/part/TurboModelCreateDlg.html",
      animation: false,
      size: "lg",
      controller: "CreateTurboModelDlgCtrl",
      resolve: {
        create: function() {
          return true;
        },
        turbo: function() {
          return $scope.turbo;
        },
        part: function() {
          return $scope.part;
        },
        turboModels: function() {
          return $scope.turboModels;
        }
      }
    });
  };

  $scope.renameTurboModel = function() {
    $uibModal.open({
      templateUrl: "/views/part/TurboModelCreateDlg.html",
      animation: false,
      size: "lg",
      controller: "CreateTurboModelDlgCtrl",
      resolve: {
        create: function() {
          return false;
        },
        turbo: function() {
          return $scope.turbo;
        },
        part: function() {
          return $scope.part;
        },
        turboModels: function() {
          return $scope.turboModels;
        }
      }
    });
  };

  $scope.deleteTurboModel = function() {
    var tmId = $scope.turbo.tm.id;
    dialogs.confirm(
      "Delete Turbo Model?",
      "Do you want to delete this turbo model?").result.then(
      function() {
        // Yes
        restService.deleteTurboModel(tmId).then(
          function() {
            // Success
            gToast.open("Turbo model deleted.");
            $scope.turbo.tm = null;
            var idx = _.findIndex($scope.turboModels, function(tm) {
              return tm.id === tmId;
            });
            if (idx !== -1) {
              $scope.turboModels.splice(idx, 1);
            }
          },
          function(response) {
            // Error
            dialogs.error(
              "Could not delete turbo model.",
              "Turbo model must not be used for any parts. Check server log for details.");
          });
      },
      function() {
          // No
      }
    );
  };

  // =============

  $scope.onSetGasketKit = function() {
    $location.path("/part/" + $scope.partId + "/gasketkit/search");
  };

  $scope.onClearGasketKit = function() {
    dialogs.confirm(
      "Unlink the Gasket Kit?",
      "Do you want to unlink the Gasket Kit [" + $scope.part.gasketKit.id + "] - " +
        $scope.part.gasketKit.manufacturerPartNumber + " from this part [" +  $scope.part.id + "] - " +
        $scope.part.manufacturerPartNumber + "?"
    ).result.then(
      function () {
        restService.clearGasketKitInPart($scope.partId).then(
          function success(updatedPart) {
            $scope.part = updatedPart;
            gToast.open("The gasket kit unlinked.");
          },
          function failure(result) {
            restService.error("Can't unlink the gasket kit.", response);
          }
        );
      },
      function () {
        // No
      }
    );
  };

  $scope.onClearTurbo = function(turboId, partNumber) {
    dialogs.confirm(
      "Unlink the Turbo?",
      "Do you want to unlink this Gasket Kit [" + $scope.part.id + "] - " +
        $scope.part.manufacturerPartNumber + " from the turbo [" +  turboId + "] - " + partNumber + "?"
    ).result.then(
      function () {
        restService.clearGasketKitInPart($scope.partId).then(
          function success(updatedPart) {
            $scope.part = updatedPart;
            gToast.open("The gasket kit unlinked.");
          },
          function failure(result) {
            restService.error("Can't unlink the gasket kit.", response);
          }
        );
      },
      function () {
        // No
      }
    );
  };

  $scope.onEditStart = function() {
    $scope.formMode = "edit";
  };

  $scope.onEditCancel = function() {
    $scope.formMode = "view";
  };

  $scope.removeComponent = function(componentToRemove) {

    dialogs.confirm(
      "Remove Common Component Mapping?",
      "Do you want to remove this common component mapping from the kit?").result.then(
      function() {
        // Yes
        Restangular.setParentless(false);
        Restangular.one('kit', $scope.partId).one('component', componentToRemove.id).remove().then(
          function() {
            // Success
            gToast.open("Component removed.");

            var idx = _.indexOf($scope.kitComponents, componentToRemove);
            $scope.kitComponents.splice(idx, 1);
          },
          function(response) {
            // Error
            restService.error("Could not delete common component mapping.", response);
          });
      },
      function() {
        // No
      });
  };


  // Images

  $scope.deleteImage = function(image) {

    dialogs.confirm(
            "Delete image?",
            "Do you want to remove this image from the part?").result.then(
        function() {
          // Yes
          Restangular.one('image', image.id).remove().then(
              function() {
                // Success
                gToast.open("Image removed.");

                var idx = _.indexOf($scope.part.productImages, image);
                $scope.part.productImages.splice(idx, 1);
              },
              function(response) {
                // Error
                restService.error("Could not delete image.", response);
              });
        },
        function() {
          // No
        });

  };

  $scope.addImage = function() {
    dialogs.create(
      '/views/part/dialog/AddImage.html',
      'AddPartImageCtrl',
      {part: $scope.part}
    ).result.then(function(image) {
      $scope.part.productImages.push(image);
    });
  };

}]);
