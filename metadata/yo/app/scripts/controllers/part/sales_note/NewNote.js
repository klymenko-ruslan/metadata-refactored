'use strict';

angular.module('ngMetaCrudApp')
	.controller('PartSalesNewNoteCtrl', function($scope, $log, $q, $location,
		$routeParams, ngTableParams, restService, Restangular, $dialogs, gToast) {
		$scope.partId = $routeParams.id;

		$scope.part = null;

	});
