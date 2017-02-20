"use strict";

angular.module("ngMetaCrudApp")
  .controller("SalesNoteCreateCtrl", ["$location", "$scope", "$routeParams", "restService", "services",
      "LinkSource",
      function($location, $scope, $routeParams, restService, services, LinkSource) {
    $scope.partId = $routeParams.id;

    $scope.requiredSource = LinkSource.isSourceRequiredForSalesNote(services);

    $scope.salesNote = {
      primaryPartId: $scope.partId,
      comment:       "Enter your notes here"
    };

    // Load the part
    $scope.part = null;
    $scope.partPromise = restService.findPart($scope.partId).then(
      function (part) {
        $scope.part = part;
        // Make sure we're using the correct part type
        $scope.partType = part.partType.name;
      },
      function (errorResponse) {
        restService.error("Could not get part details", errorResponse);
      }
    );

    function cbSave(srcIds, ratings, description) {
      $scope.savePromise = restService.createSalesNote($scope.salesNote.primaryPartId, $scope.salesNote.comment,
        srcIds, ratings, description).then(
          function (salesNoteResponse) {
            $location.path("/part/" + $scope.partId + "/sales_note/" + salesNoteResponse.id);
            return salesNoteResponse;
          },
          function (errorResponse) {
            restService.error("Couldn't save for sales note.", errorResponse);
          }
      );
    };

    $scope.saveAndEdit = function() {
      LinkSource.link($scope.partId, cbSave, $scope.requiredSource, "/part/" + $scope.partId + "/sales_note/create");
    };

  }]);


