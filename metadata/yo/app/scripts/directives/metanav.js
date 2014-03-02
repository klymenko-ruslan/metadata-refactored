'use strict';

angular.module('ngMetaCrudApp')
  .directive('metanav', function () {
    return {
      transclude: true,
      template:
          '<div class="row">' +

           // Transcluded
          '<ul class="nav nav-pills" ng-transclude>' +
          '</ul>' +

          '<ul class="nav nav-pills">' +

//              // Parts
//
//              <li>
//                <a authorize="ROLE_READ"
//                ng-href="#/part" class="btn btn-default">
//                  <i class="fa fa-bars"></i>
//                Part List
//                </a>
//              </li>

           // Indexing and Cache
          '  <li class="dropdown" authorize="ROLE_ADMIN">' +
          '    <a class="btn btn-warning dropdown-toggle" data-toggle="dropdown" href="#">' +
          '      Indexing and Cache <span class="caret"></span>' +
          '    </a>' +
          '    <ul class="dropdown-menu">' +
          '      <li>' +
          '        <a ng-click="reindexTurbos()"' +
          '           class="btn">' +
          '          <i class="fa fa-cogs"></i>' +
          '          Reindex Turbos' +
          '        </a>' +
          '      </li>' +
          '      <li>' +
          '        <a ng-click="reindexSearch()"' +
          '           class="btn">' +
          '          <i class="fa fa-cogs"></i>' +
          '          Reindex Search' +
          '        </a>' +
          '      </li>' +
          '      <li>' +
          '        <a ng-click="clearHibernate()"' +
          '           class="btn">' +
          '          <i class="fa fa-cogs"></i>' +
          '          Clear Hibernate Cache' +
          '        </a>' +
          '      </li>' +
          '    </ul>' +
          '  </li>' +

           // Security
          '  <li class="dropdown" authorize="ROLE_ADMIN">' +
          '    <a class="btn btn-default dropdown-toggle" data-toggle="dropdown">' +
          '      Security <span class="caret"></span>' +
          '    </a>' +
          '    <ul class="dropdown-menu">' +
          '     <li>' +
          '       <a ng-href="#/security/groups/"' +
          '          class="btn">' +
          '         <i class="fa fa-group"></i>' +
          '         Groups' +
          '       </a>' +
          '     </li>' +
          '     <li>' +
          '       <a ng-href="#/security/users/"' +
          '          class="btn">' +
          '         <i class="fa fa-user"></i>' +
          '         Users' +
          '       </a>' +
          '     </li>' +
          '    </ul>' +
          '  </li>'+

           // My Account
          '  <li>' +
          '    <a ng-href="#/security/me"' +
          '       class="btn btn-primary">' +
          '      <i class="fa fa-smile-o"></i>' +
          '      My Account' +
          '    </a>' +
          '  </li>' +
          '</ul>' +
          '</div>',
      restrict: 'E'
    };
  });
