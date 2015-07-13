'use strict';

angular.module('ngMetaCrudApp')
  .directive('salesNoteActions', function ($compile) {
    return {
      replace: false,
      restrict: 'A',
      scope: {
          salesNote: '=salesNoteActions'
      },
      link: function postLink(scope, element, attrs) {
        element.append(
        $compile('<a ng-click="SalesNotes.submit(salesNote)"'
            + ' authorize="ROLE_SALES_NOTE_SUBMIT"'
            + ' ng-show="salesNote.state === \'draft\' || salesNote.state === \'rejected\'"'
            + ' class="btn btn-info text-center btn-sm">'
            + ' <span class="fa fa-check-square"></span> Submit'
            + '</a> '
            + '<a ng-click="SalesNotes.publish(salesNote)"'
            + '    authorize="ROLE_SALES_NOTE_PUBLISH"'
            + '    ng-show="salesNote.state === \'approved\'"'
            + '    class="btn btn-primary text-center btn-sm">'
            + '   <span class="fa fa-cloud-upload"></span> Publish'
            + '</a> '
            + '<a ng-click="SalesNotes.approve(salesNote)"'
            + '    authorize="ROLE_SALES_NOTE_APPROVE"'
            + '    ng-show="salesNote.state === \'submitted\'"'
            + '    class="btn btn-success text-center btn-sm">'
            + '   <span class="fa fa-thumbs-up"></span> Approve'
            + '</a> '
            + '<a ng-click="SalesNotes.reject(salesNote)"'
            + '    authorize="ROLE_SALES_NOTE_REJECT"'
            + '    ng-show="salesNote.state === \'submitted\' || salesNote.state === \'approved\'"'
            + '    class="btn btn-danger text-center btn-sm">'
            + '   <span class="fa fa-thumbs-down"></span> Reject'
            + '</a> '
            + '<a ng-click="SalesNotes.retract(salesNote)"'
            + '    authorize="ROLE_SALES_NOTE_RETRACT"'
            + '    ng-show="salesNote.state === \'published\'"'
            + '    class="btn btn-warning text-center btn-sm">'
            + '   <span class="fa fa-cloud-download"></span> Retract'
            + '</a> ')(scope)
        );
      }
    };
  });
