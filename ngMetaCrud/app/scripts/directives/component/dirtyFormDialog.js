//'use strict';
//
//angular.module('ngMetaCrudApp')
//  .directive('dirtyFormDialog', function ($modal) {
//    return {
//        restrict: 'A',
//        scope: {
//            bom: "&dirtyFormDialog"
//        },
//        controller: function($scope, $element, $attrs, $transclude, otherInjectables) {
//
//        },
//        link: function($scope, elem, attrs, controller) {
//
//            $scope.$on('$locationChangeStart', function(event, next, current) {
//                if (controller.$scope.isAccepted) {
//                    return;
//                }
//
//                if ($scope.bom.$dirty) {
//                    $modal.open({
//                        templateUrl: 'myModalContent.html',
//                        controller: controller,
//                        resolve: {
//                            items: function () {
//                                return $scope.items;
//                            }
//                        }
//                    });
//
//                    event.preventDefault();
//                }
//            });
//        }
//    }
//  });
