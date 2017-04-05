"use strict";

angular.module("ngMetaCrudApp")

.controller("PartListCtrl", ["$scope", "$log", "$uibModal", "partTypes", "critDimsByPartTypes", "critDimEnumVals",
  function ($scope, $log, $uibModal, partTypes, critDimsByPartTypes, critDimEnumVals) {
    $scope.createPart = function () {
      var modalInstance = $uibModal.open({
        "templateUrl": "/views/part/PartCreateModal.html",
        "controller": "PartCreateModalCtrl"
      });
    };

    $scope.partTypes = partTypes;
    $scope.critDimsByPartTypes = critDimsByPartTypes;
    $scope.critDimEnumVals = critDimEnumVals;

    $scope.editor1 = "Hello world xxx2!";
    $scope.markdownEditorOpts = {
      iconlibrary: "fa",
      addExtraButtons: true,
      resize: "vertical",
      fullscreen: {enable: false},
      hiddenButtons: "Preview",
      dropZoneOptions: {
        url: "http://localhost:8080/metadata/file/post",
        maxFilesize:20,
        createImageThumbnails: false,
        maxFiles: 100,
        parallelUploads: 1,
        previewsContainer: "#descriptionUploads",
        previewTemplate: "<li><div class='dz-preview'><div class='dz-details'><span class='dz-filename' data-dz-name></span>, <span class='dz-size' data-dz-size></span><button class='btn btn-danger btn-xs' style='margin-left:4px' data-dz-remove><i class='fa fa-trash-o'></i> Remove</button><span style='padding-left:4px;' data-dz-errormessage></span></div></div></li>",
        autoProcessQueue: false,
        init: function() {
          this.on("addedfile", function(file) {
            alert("Added file: " + file.name + ", " + file.size + ", " + file.type);
          });
          this.on("removedfile", function(file) {
            alert("Removed file: " + file.name + ", " + file.size + ", " + file.type);
          });
        }
      }
    };

  }
])
.controller("PartCreateModalCtrl", ["$scope", "$uibModalInstance", "$log", "$location", "PartTypes",
  function ($scope, $uibModalInstance, $log, $location, PartTypes) {
    $scope.PartTypes = PartTypes;
    $scope.selection = {};

    $scope.create = function () {
      $uibModalInstance.close("cancel");
      $location.path("/part/createByPartTypeId/" + $scope.selection.partType.id);
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss("cancel");
    };

    $scope.refresh = function () {
      $scope.selection = {};
      PartTypes.refresh();
    };

  }
]);
