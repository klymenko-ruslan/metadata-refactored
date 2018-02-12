'use strict';

angular.module('ngMetaCrudApp')

  .constant('MERGE_OPTIONS', {
    'PICKED_ALONE_TO_PART': 1,  // Add picked part to interchange group of this part and remove picked part from its existing interchange
    'PART_ALONE_TO_PICKED': 2,  // Add this part to interchange group of the picked part and remove this part from its existing interchange
    'PICKED_ALL_TO_PART': 3     // Add the picked part and all its existing interchange parts to interchange group of this part
  })

  .controller('mergeInterchangeablesCtrl', ['$scope', '$uibModalInstance', 'data', 'MERGE_OPTIONS', function($scope, $uibModalInstance, data, MERGE_OPTIONS) {
    $scope.mergeOptions = MERGE_OPTIONS;
    $scope.mergeChoice = data.mergeChoice;
    $scope.cancel = function() {
      $uibModalInstance.dismiss('Canceled');
    };
    $scope.doIt = function() {
      $uibModalInstance.close($scope.mergeChoice);
    };
  }])

  .controller('PartInterchangeSearchCtrl', ['$log', '$scope', '$location', '$routeParams', 'restService',
      'toastr', 'dialogs', 'MERGE_OPTIONS', 'partTypes', 'critDimsByPartTypes', 'critDimEnumVals',
      'services', 'LinkSource',
      function($log, $scope, $location, $routeParams,
        restService, toastr, dialogs, MERGE_OPTIONS, partTypes, critDimsByPartTypes, critDimEnumVals,
        services, LinkSource) {
    $scope.partTypes = partTypes;
    $scope.critDimsByPartTypes = critDimsByPartTypes;
    $scope.critDimEnumVals = critDimEnumVals;
    $scope.partId = $routeParams.id;
    $scope.part = null;
    $scope.pickedPart = null;
    // The part whose interchange we're editing
    $scope.promise = restService.findPart($scope.partId).then(function(part) {
      $scope.part = part;
    });

    $scope.requiredSource = LinkSource.isSourceRequiredForInterchange(services);

    $scope.go = function(path) {
      $location.path(path);
    };

    $scope.pick = function(pickedPartId) {
      // Lookup the picked part
      $scope.pickedPartPromise = restService.findPart(pickedPartId).then(
        function(pickedPart) {
          var partType = $scope.part.partType.name;
          var pickedPartType = pickedPart.partType.name;
          // Check part type and add the picked part
          if (partType !== pickedPartType) {
            dialogs.confirm('Confirm Interchange Part Type',
              'This part and picked one have different types.\n' +
              'Are you sure you want to make the picked ' +
                pickedPartType + ' interchangeable with this ' + partType + '?')
              .result.then(function() {
                $scope.pickedPart = pickedPart;
              });
          } else {
            $scope.pickedPart = pickedPart;
          }
        },
        function(response) {
          dialogs.error('Could not load part details.', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
        });
    };

    /**
     * This is a callback that invoked when an user presses key 'Enter'
     * while focus is in an input field 'Part Number'
     * of a 'part search filter'.
     *
     * See also a view PartInterchangeSearch.html and
     * an attribute 'on-press-enter-callback' in a declaration of a directive
     * 'part-search'.
     */
    $scope.cbPickOnEnter = function(searchResults) {
      // When a result of a search is a single part
      // and this part is suitable to be picked then
      // we pick its.
      if (searchResults && searchResults.hits.total === 1) {
        var rec = searchResults.hits.hits[0]._source;
        var partId = rec.id;
        $scope.pick(partId);
      }
      // Return statement below
      // signals that default behaviour on the pressed 'Enter' key should
      // not be invoked.
      // See function 'onKeyUpInPartNumber' in the partSearch.js.
      return true;
    };

    function cbAddPartToThisInterchangeGroup(srcIds, ratings, description, attachIds) {
      // Add part to this interchange group
      restService.updatePartInterchange($scope.partId, $scope.pickedPart.id, MERGE_OPTIONS.PICKED_ALONE_TO_PART,
          srcIds, ratings, description, attachIds).then(
        function success() {
          toastr.success('Added picked part to interchange.');
          $location.path('/part/' + $scope.partId);
        },
        function failure(response) {
          restService.error('Adding to the interchange failed.', response);
        }
      );
    }

    function cbAskMergeOpt(srcIds, ratings, description, attachIds) {
      // In this case there are several possibilities how interchangeables can be merged.
      // See ticket #484.
      var mergeDialog = dialogs.create('/views/dialog/MergeInterchangeablesDlg.html', 'mergeInterchangeablesCtrl',
        {
          'mergeChoice': MERGE_OPTIONS.PICKED_ALL_TO_PART
        }, {
          'size': 'lg',
          'keyboard': true,
          'backdrop': false
        }
      );
      mergeDialog.result.then(
        function(mergeChoice) {
          restService.updatePartInterchange($scope.partId, $scope.pickedPart.id, mergeChoice,
              srcIds, ratings, description, attachIds).then(
            function() {
              toastr.success('Interchangeable part group changed.');
              $location.path('/part/' + $scope.partId);
            },
            function(response) {
              dialogs.error('Could not change interchangeable part group.', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
            }
          );
        },
        function() {
          $log.log('Cancelled.');
        }
      );
    }

    /* Unused
    function cbMergeAloneToPicked(srcIds, ratings, description, attachIds) {
      // Add this part to the picked part's interchange
      restService.updatePartInterchange($scope.partId, $scope.pickedPart.id, MERGE_OPTIONS.PART_ALONE_TO_PICKED,
          srcIds, ratings, description, attachIds).then(
        function success() {
          toastr.success('Added part to picked part\'s interchanges.');
          $location.path('/part/' + $scope.partId);
        },
        function failure(response) {
          restService.error('Adding to the interchange failed.', response);
        }
      );
    }
    */

    $scope.save = function() {
      if (!$scope.pickedPart.interchange || $scope.pickedPart.interchange.alone) {
        LinkSource.link(cbAddPartToThisInterchangeGroup, $scope.requiredSource, null);
      } else {
        LinkSource.link(cbAskMergeOpt, $scope.requiredSource, null);
      }
    };

    $scope.canSave = function() {
      // No Picked Part
      if ($scope.pickedPart === null) {
        return false;
      }
      if ($scope.part.id === $scope.pickedPart.id) {
        return false;
      }
      // Same interchange
      if ($scope.part.interchange && $scope.pickedPart.interchange && $scope.part.interchange.id === $scope.pickedPart.interchange.id) {
        return false;
      }
      return true;
    };

    $scope.canClear = function() {
      return $scope.part && $scope.part.interchange && !$scope.part.interchange.alone;
    };

    $scope.removeInterchange = function() {
      dialogs.confirm(
          'Remove from interchangeable part group?',
          'Other parts in the group will not be modified.')
        .result.then(
          function() {
            restService.deletePartInterchange($scope.partId, $scope.part.interchangeId).then(
              function() {
                // Success
                toastr.success('Part removed from interchange.');
                $location.path('/part/' + $scope.partId);
              },
              function(response) {
                dialogs.error('Could not remove part from interchange', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
              });
          });
    };
  }]);
