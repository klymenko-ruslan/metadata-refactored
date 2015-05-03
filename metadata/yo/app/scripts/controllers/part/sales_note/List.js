'use strict';

angular.module('ngMetaCrudApp')
	.controller('PartSalesNoteListCtrl', function($scope, $log, $q, $location,
		$routeParams, ngTableParams, restService, Restangular, $dialogs, gToast) {
		$scope.partId = $routeParams.id;

		$scope.part = null;

		// $scope.partPromise = restService.findPart($scope.partId).then(
		// 	function(part) {
		// 		$scope.part = part;
		//
		// 		// Make sure we're using the correct part type
		// 		$scope.partType = part.partType.name;
		//
		// 		// Reload the table
		// 		$scope.bomTableParams.reload();
		// 	},
		// 	function(errorResponse) {
		// 		$log.log("Could not get part details", errorResponse);
		// 		restService.error("Could not get part details", errorResponse);
		// 	});
	});
