drop table crit_dim;

create table crit_dim (
    id              bigint not null,
    part_type_id    bigint not null,
    seq_num         int not null,
    data_type       enum ('DECIMAL', 'ENUMERATION', 'INTEGER', 'TEXT') not null,
    json_enum       varchar(32) comment 'Name of a property enumeration in serialized to JSON part''s object when daat_type=ENUMERATION.',
    unit            enum ('DEGREES', 'GRAMS', 'INCHES'),
    tolerance       tinyint(1) comment '0 - nominal, 1 - tolerance/limit, null - not a tolerance',
    name            varchar(255) not null,
    json_name       varchar(32) not null comment 'Name of a property in serialized to JSON part''s object.',
    null_allowed    tinyint(1) not null comment 'Validation: Is NULL allowed?',
    null_display    varchar(32) comment 'How to display NULL values.',
    min_val         decimal(15, 6) comment 'Validation: minal (inclusive) allowed value for numeric types (DECIMAL, INTEGER).',
    max_val         decimal(15, 6) comment 'Validation: maximal (inclusive) allowed value for numeric types (DECIMAL, INTEGER).',
    regex           varchar(255) comment 'Validation: JS regular expression',
    parent_id       bigint,
    length          tinyint comment 'Lenth on a web page',
    scale           tinyint comment 'Scale on a web pate.',
    primary key(id),
    unique key(part_type_id, seq_num),
    foreign key (part_type_id) references part_type(id),
    foreign key (parent_id) references crit_dim(id) on delete cascade on update cascade
) engine=innodb;

insert into crit_dim
(id, part_type_id, seq_num,     data_type,     unit, tolerance, name,                      json_name,              json_enum,              null_allowed, null_display, min_val, max_val, regex, parent_id, length, scale)
values
-- Bearing housing
(  1,          13,       1, 'ENUMERATION',     null,      null, 'WATER COOLED',            'waterCooled',          'waterCooledEnum',                 1,         'No',    null,    null,  null,      null,   null,  null),
(  2,          13,       2,     'DECIMAL', 'INCHES',         0, 'C/E DIA "A"',             'ceDiaA',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
(  3,          13,       3,     'DECIMAL', 'INCHES',         1, 'C/E DIA "A" TOL',         'ceDiaATol',                         null,                 1,         null,       0,    null,  null,         2,      6,     3),
(  4,          13,       4,     'DECIMAL', 'INCHES',         0, 'C/E DIA "B"',             'ceDiaB',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
(  5,          13,       5,     'DECIMAL', 'INCHES',         1, 'C/E DIA "B" TOL',         'ceDiaBTol',                         null,                 1,         null,       0,    null,  null,         4,      6,     3),
(  6,          13,       6,     'DECIMAL', 'INCHES',         0, 'C/E DIA "C"',             'ceDiaC',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
(  7,          13,       7,     'DECIMAL', 'INCHES',         1, 'C/E DIA "C" TOL',         'ceDiaCTol',                         null,                 1,         null,       0,    null,  null,         6,      6,     3),
( 10,          13,      10,     'DECIMAL', 'INCHES',      null, 'BORE DIA MAX',            'boreDiaMax',                        null,                 1,         null,       0,    null,  null,      null,      6,     4),
( 11,          13,      11,     'DECIMAL', 'INCHES',      null, 'BORE DIA MIN',            'boreDiaMin',                        null,                 1,         null,       0,    null,  null,      null,      6,     4),
( 12,          13,      12,     'DECIMAL', 'INCHES',         0, 'PR BORE DIA',             'prBoreDia',                         null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 13,          13,      13,     'DECIMAL', 'INCHES',         1, 'PR BORE DIA TOL',         'prBoreDiaTol',                      null,                 1,         null,       0,    null,  null,        12,      6,     3),
( 14,          13,      14, 'ENUMERATION',     null,      null, 'OIL FEED',                'oilFeed',                           null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 15,          13,      15, 'ENUMERATION',     null,      null, 'SPINNING BEARING',        'spinningBearing',      'yesNoEnum',                       1,         null,    null,    null,  null,      null,   null,  null),
( 16,          13,      16, 'ENUMERATION',     null,      null, 'OIL INLET THREAD',        'oilInletThread',                    null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 17,          13,      17, 'ENUMERATION',     null,      null, 'OIL DRAIN THREAD',        'oilDrainThread',                    null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 18,          13,      18, 'ENUMERATION',     null,      null, 'OIL DRAIN FLANGE THREAD', 'oilDrainFlangeThread',              null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 19,          13,      19, 'ENUMERATION',     null,      null, 'COOLANT PORT THREAD 1',   'coolantPortThread1',                null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 20,          13,      20, 'ENUMERATION',     null,      null, 'COOLANT PORT THREAD 2',   'coolantPortThread2',                null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 21,          13,      21,     'DECIMAL', 'INCHES',         0, 'T/E DIA "D"',             'teDiaD',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 22,          13,      22,     'DECIMAL', 'INCHES',         1, 'T/E DIA "D" TOL',         'teDiaDTol',                         null,                 1,         null,       0,    null,  null,        21,      6,     3),
( 23,          13,      23,     'DECIMAL', 'INCHES',         0, 'T/E DIA "E"',             'teDiaE',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 24,          13,      24,     'DECIMAL', 'INCHES',         1, 'T/E DIA "E" TOL',         'teDiaETol',                         null,                 1,         null,       0,    null,  null,        23,      6,     3),
( 25,          13,      25,     'DECIMAL', 'INCHES',         0, 'T/E DIA "F"',             'teDiaF',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 26,          13,      26,     'DECIMAL', 'INCHES',         1, 'T/E DIA "F" TOL',         'teDiaFTol',                         null,                 1,         null,       0,    null,  null,        25,      6,     3),
( 27,          13,      27,     'DECIMAL', 'DEGREES',        0, 'ARM ANGLE',               'armAngle',                          null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 28,          13,      28, 'ENUMERATION',     null,      null, 'QUADRANT',                'quadrant',                          null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 29,          13,      29,     'DECIMAL', 'INCHES',         0, 'OAL',                     'oal',                               null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 30,          13,      30,     'DECIMAL', 'INCHES',         1, 'OAL TOL',                 'oalTol',                            null,                 1,         null,       0,    null,  null,        29,      6,     3),
( 31,          13,      31,     'DECIMAL',  'GRAMS',      null, 'WEIGHT',                  'weight',                            null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 32,          13,      32,     'DECIMAL',     null,      null, 'DIAGRAM #',               'diagramNum',                        null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 33,          13,      33, 'ENUMERATION',     null,      null, 'OIL INLET FLANGE THREAD', 'oil_inlet_glange_thread',           null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 34,          13,      34,     'DECIMAL',     null,      null, 'LEAD IN CHMFR ½-ANGLE',   'leadInChmfr05Angle',                null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 35,          13,      35,     'DECIMAL',     null,      null, 'LEAD IN CHMFR LEN',       'leadInChmfrLen',                    null,                 1,         null,       0,    null,  null,      null,      6,     1);


/*
create table crit_dim_enum (
    id bigint not null,
    crit_dim_id bigint not null,
    name varchar(32) not null,
    val varchar(32) not null,
    primary key (id),
    unique key (id, crit_dim_id),
    foreign key (crit_dim_id) references crit_dim(id)
) comment='Enumeration values for critical dimensions.' engine=innodb;

insert into crit_dim_enum
(id, crit_dim_id, name, val)
values
( 1,           1, 'OIL',    'OIL'),
( 2,           1, 'WATER',  'WATER'),
( 3,          14, 'DUAL',   'DUAL'),
( 4,          14, 'SINGLE', 'SINGLE'),
-- TODO: OIL INLET THREAD, OIL DRAIN THREAD, OIL DRAIN FLANGE THREAD, COOLANT PORT THREAD 1, COOLANT PORT THREAD 2, OIL INLET FLANGE THREAD
( 5,          28, 'I', 'I'),
( 6,          28, 'II', 'II'),
( 7,          28, 'III', 'III'),
( 8,          28, 'IV', 'IV'),
( 9,          28, 'no', 'No');
*/


alter table bearing_housing add column water_cooled enum('OIL', 'WATER');
alter table bearing_housing add column ce_dia_a decimal(6,3);
alter table bearing_housing add column ce_dia_a_tol decimal(6,3);
alter table bearing_housing add column ce_dia_b decimal(6,3);
alter table bearing_housing add column ce_dia_b_tol decimal(6,3);
alter table bearing_housing add column ce_dia_c decimal(6,3);
alter table bearing_housing add column ce_dia_c_tol decimal(6,3);
alter table bearing_housing add column cwc_dia decimal(6,3);
alter table bearing_housing add column cwc_dia_tol decimal(6,3);
alter table bearing_housing add column bore_dia_max decimal(6,3);
alter table bearing_housing add column bore_dia_min decimal(6,3);
alter table bearing_housing add column pr_bore_dia decimal(6,3);
alter table bearing_housing add column pr_bore_dia_tol decimal(6,3);
alter table bearing_housing add column spinning_bearing tinyint(1);
alter table bearing_housing add column te_dia_d decimal(6,3);
alter table bearing_housing add column te_dia_d_tol decimal(6,3);
alter table bearing_housing add column te_dia_e decimal(6,3);
alter table bearing_housing add column te_dia_e_tol decimal(6,3);
alter table bearing_housing add column te_dia_f decimal(6,3);
alter table bearing_housing add column te_dia_f_tol decimal(6,3);
alter table bearing_housing add column arm_angle decimal(6,1);
alter table bearing_housing add column oal decimal(6,3);
alter table bearing_housing add column oal_tol decimal(6,3);
alter table bearing_housing add column weight decimal(6,1);
alter table bearing_housing add column diagram_num int;
alter table bearing_housing add column led_in_chmfr_angle decimal(6,1);
alter table bearing_housing add column led_in_chmfr_len decimal(6,3);

update bearing_housing set
    water_cooled = 'WATER',
    ce_dia_a = 3.125,
    ce_dia_a_tol = 0.005,
    ce_dia_b = 3.650,
    ce_dia_b_tol = 0.005,
    ce_dia_c = 2.95,
    ce_dia_c_tol = 0.005,
    cwc_dia = 2.235,
    cwc_dia_tol = 0.005,
    bore_dia_max = 0.5495,
    bore_dia_min = 0.5445,
    pr_bore_dia = 0.617,
    pr_bore_dia_tol = 0.001,
    spinning_bearing = 1,
    te_dia_d = 4.382,
    te_dia_d_tol = 0.005,
    te_dia_e = 3.088,
    te_dia_e_tol = 0.005,
    te_dia_f = 2.030,
    te_dia_f_tol = 0.005,
    arm_angle = 22.5,
    oal = 2.708,
    oal_tol = 0.005,
    weight = 1500,
    diagram_num = 6,
    led_in_chmfr_angle = 22,
    led_in_chmfr_len = .015
where part_id=44024;









<form class="form-horizontal">
  <div class="form-group">
    <div class="col-xs-3">
      <div class="checkbox">
        <label>
          <input type="checkbox" data-ng-model="opts.hideBlank" />Hide blank
        </label>
      </div>
    </div>
    <div class="col-xs-3">
      <div class="checkbox">
        <label>
          <input type="checkbox" data-ng-model="opts.rawView" />Raw view
        </label>
      </div>
    </div>
    <div class="col-xs-6">
      <input type="text" class="form-control" data-ng-model="opts.filter" data-ng-model-options="{debounce:300}" data-ng-trim="true" placeholder="Filter" />
    </div>

  </div>
</form>
<table class="table table-striped table-condensed">
  <tr data-ng-repeat="d in toDisplay">
    <td>{{d.name}}</td>
    <td>{{d.value}}</td>
    <td>
      <button class="btn btn-warning btn-xs pull-right" onclick="alert('TODO')">
        <i class="fa fa-cog"></i> Modify
      </button>
    </td>
  </tr>
  <tr ng-hide="toDisplay.length > 0">
    <td colspan="3">No rows to display.</td>
  </tr>
</table>



(function(angular) {
  angular.module("myApp", [])
    .directive("transform-view", ["$log", function($log) {
      return {
        restrict: "E",
        replace: false,
        transclude: true,
      };
    }])
    .directive("criticalDimensions", ["$log", function($log) {
      return {
        restrict: "E",
        replace: false,
        transclude: true,
        templateUrl: "criticaldimensions.html",
        scope: {
          part: "=",
          descriptors: "="
        },
        controller: ["$scope", "$log", function($scope, $log) {

          // A list which will be actually displayed on the web.          
          // See function _copyDescriptorsToDisplay() below to
          // know how this list is filled in.
          $scope.toDisplay = null;

          // Options bar's values.
          $scope.opts = {
            hideBlank: true,
            rawView: false,
            filter: ""
          };

          // Track changes on the options bar.
          $scope.$watchCollection("opts", function(newOpts, oldOpts) {
            $scope._copyDescriptorsToDisplay();
          });

          // This is a heart function of this directive.
          // It filters critical dimensions desctiptors according to
          // option's bar values and transrofms them to
          // a simple DTO object with formatted values prepared to
          // display on a web view.
          $scope._copyDescriptorsToDisplay = function() {
            var normalizedFilter = null;
            if ($scope.opts.filter) {
              normalizedFilter = $scope.opts.filter.toLowerCase();
            }
            $scope.toDisplay = _.filter($scope.descriptors,
              function(d) {
                return $scope.opts.rawView || !$scope.opts.rawView && !d.tolerance;
              }
            ).map(
              function(d) {
                return {
                  name: d.name,
                  value: $scope._getValue(d)
                };
              }
            ).filter(
              function(dto) {
                return (!$scope.opts.hideBlank || $scope.opts.hideBlank && dto.value)
                  && (!normalizedFilter ||  normalizedFilter && (
                    dto.name.toLowerCase().indexOf(normalizedFilter) > -1
                    ||
                    dto.value && dto.value.toLowerCase().indexOf(normalizedFilter) > -1
                  ));
              }
            );
          };
          
          $scope._getValue = function(d) {
            var retVal = $scope.part[d.jsonName];
            if (retVal === undefined || retVal === null) {
              retVal = "";
            } else {
              retVal = retVal.toString();
            }
            return retVal;
          };

        }]
      };
    }])
    .controller("criticalDimensionCtrl", ["$scope", "$log", function($scope, $log) {

      $scope.part = {
        "class": "com.turbointernational.metadata.domain.part.types.BearingHousing",
        "id": 44024,
        "manufacturer": {
          "id": 6,
          "name": "KKK",
          "type": {
            "id": 1,
            "name": "turbo"
          }
        },
        "manufacturerPartNumber": "5304-150-0006",
        "name": "Bearing Housing",
        "partType": {
          "id": 13,
          "name": "Bearing Housing",
          "value": "bearing_housing",
          "magentoAttributeSet": "Bearing Housing",
          "parent": {
            "id": 9,
            "name": "Major Component",
            "value": "major_component",
            "magentoAttributeSet": "Part"
          }
        },
        "inactive": false,
        "turboTypes": [{
          "id": 413,
          "manufacturer": {
            "id": 6,
            "name": "KKK",
            "type": {
              "id": 1,
              "name": "turbo"
            }
          },
          "name": "KO3"
        }, {
          "id": 415,
          "manufacturer": {
            "id": 6,
            "name": "KKK",
            "type": {
              "id": 1,
              "name": "turbo"
            }
          },
          "name": "KO4"
        }],
        "interchange": {
          "id": 19,
          "alone": false
        },
        "version": 1,
        "coolType": {
          "id": 2,
          "name": "Water"
        },
        "oilInlet": "M12X1.5",
        "oilOutlet": "16 mm",
        "outletFlangeHoles": "M6X1.0",
        "waterPorts": "M14X1.5",
        "designFeatures": "Single",
        "bearingType": "Pinned",
        "waterCooled": "WATER",
        "ceDiaA": 3.125,
        "ceDiaATol": 0.005,
        "ceDiaB": 3.65,
        "ceDiaBTol": 0.005,
        "ceDiaC": 2.95,
        "ceDiaCTol": 0.005,
        "cwcDia": 2.235,
        "cwcDiaTol": 0.005,
        "boreDiaMax": 0.55,
        "prBoreDia": 0.617,
        "prBoreDiaTol": 0.001,
        "spinningBearing": true,
        "teDiaD": 4.382,
        "teDiaDTol": 0.005,
        "teDiaE": 3.088,
        "teDiaETol": 0.005,
        "teDiaF": 2.03,
        "teDiaFTol": 0.005,
        "armAngle": 22.5,
        "oal": 2.708,
        "oalTol": 0.005,
        "weight": 1500.0,
        "diagramNum": 6,
        "ledInChmfrAngle": 22.0,
        "ledInChmfrLen": 0.015
      };

      $scope.critDims = [{
        "id": 1,
        "seqNum": 1,
        "dataType": "ENUMERATION",
        "name": "WATER COOLED",
        "jsonName": "waterCooled",
        "nullAllowed": true,
        "nullDisplay": "no"
      }, {
        "id": 2,
        "seqNum": 2,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": false,
        "name": "C/E DIA \"A\"",
        "jsonName": "ceDiaA",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 3,
        "seqNum": 3,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": true,
        "name": "C/E DIA \"A\" TOL",
        "jsonName": "ceDiaAtol",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 4,
        "seqNum": 4,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": false,
        "name": "C/E DIA \"B\"",
        "jsonName": "ceDiaB",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 5,
        "seqNum": 5,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": true,
        "name": "C/E DIA \"B\" TOL",
        "jsonName": "ceDiaBtol",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 6,
        "seqNum": 6,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": false,
        "name": "C/E DIA \"C\"",
        "jsonName": "ceDiaC",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 7,
        "seqNum": 7,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": true,
        "name": "C/E DIA \"C\" TOL",
        "jsonName": "ceDiaCtol",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 10,
        "seqNum": 10,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "name": "BORE DIA MAX",
        "jsonName": "boreDiaMax",
        "nullAllowed": true,
        "length": 6,
        "scale": 4
      }, {
        "id": 11,
        "seqNum": 11,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "name": "BORE DIA MIN",
        "jsonName": "boreDiaMin",
        "nullAllowed": true,
        "length": 6,
        "scale": 4
      }, {
        "id": 12,
        "seqNum": 12,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": false,
        "name": "PR BORE DIA",
        "jsonName": "prBoreDia",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 13,
        "seqNum": 13,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": true,
        "name": "PR BORE DIA TOL",
        "jsonName": "prBoreDiaTol",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 14,
        "seqNum": 14,
        "dataType": "ENUMERATION",
        "name": "OIL FEED",
        "jsonName": "oilFeed",
        "nullAllowed": true
      }, {
        "id": 15,
        "seqNum": 15,
        "dataType": "BOOLEAN",
        "name": "SPINNING BEARING",
        "jsonName": "spinningBearing",
        "nullAllowed": true
      }, {
        "id": 16,
        "seqNum": 16,
        "dataType": "ENUMERATION",
        "name": "OIL INLET THREAD",
        "jsonName": "oilInletThread",
        "nullAllowed": true
      }, {
        "id": 17,
        "seqNum": 17,
        "dataType": "ENUMERATION",
        "name": "OIL DRAIN THREAD",
        "jsonName": "oilDrainThread",
        "nullAllowed": true
      }, {
        "id": 18,
        "seqNum": 18,
        "dataType": "ENUMERATION",
        "name": "OIL DRAIN FLANGE THREAD",
        "jsonName": "oilDrainFlangeThread",
        "nullAllowed": true
      }, {
        "id": 19,
        "seqNum": 19,
        "dataType": "ENUMERATION",
        "name": "COOLANT PORT THREAD 1",
        "jsonName": "coolantPortThread1",
        "nullAllowed": true
      }, {
        "id": 20,
        "seqNum": 20,
        "dataType": "ENUMERATION",
        "name": "COOLANT PORT THREAD 2",
        "jsonName": "coolantPortThread2",
        "nullAllowed": true
      }, {
        "id": 21,
        "seqNum": 21,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": false,
        "name": "T/E DIA \"D\"",
        "jsonName": "teDiaD",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 22,
        "seqNum": 22,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": true,
        "name": "T/E DIA \"D\" TOL",
        "jsonName": "teDiaDtol",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 23,
        "seqNum": 23,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": false,
        "name": "T/E DIA \"E\"",
        "jsonName": "teDiaE",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 24,
        "seqNum": 24,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": true,
        "name": "T/E DIA \"E\" TOL",
        "jsonName": "teDiaEtol",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 25,
        "seqNum": 25,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": false,
        "name": "T/E DIA \"F\"",
        "jsonName": "teDiaF",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 26,
        "seqNum": 26,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": true,
        "name": "T/E DIA \"F\" TOL",
        "jsonName": "teDiaFtol",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 27,
        "seqNum": 27,
        "dataType": "DECIMAL",
        "unit": "DEGREES",
        "tolerance": false,
        "name": "ARM ANGLE",
        "jsonName": "armAngle",
        "nullAllowed": true,
        "length": 6,
        "scale": 1
      }, {
        "id": 28,
        "seqNum": 28,
        "dataType": "ENUMERATION",
        "name": "QUADRANT",
        "jsonName": "quadrant",
        "nullAllowed": true
      }, {
        "id": 29,
        "seqNum": 29,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": false,
        "name": "OAL",
        "jsonName": "oal",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 30,
        "seqNum": 30,
        "dataType": "DECIMAL",
        "unit": "INCHES",
        "tolerance": true,
        "name": "OAL TOL",
        "jsonName": "oalTol",
        "nullAllowed": true,
        "length": 6,
        "scale": 3
      }, {
        "id": 31,
        "seqNum": 31,
        "dataType": "DECIMAL",
        "unit": "GRAMS",
        "name": "WEIGHT",
        "jsonName": "weight",
        "nullAllowed": true,
        "length": 6,
        "scale": 1
      }, {
        "id": 32,
        "seqNum": 32,
        "dataType": "DECIMAL",
        "name": "DIAGRAM #",
        "jsonName": "diagramNum",
        "nullAllowed": true,
        "length": 6,
        "scale": 1
      }, {
        "id": 33,
        "seqNum": 33,
        "dataType": "ENUMERATION",
        "name": "OIL INLET FLANGE THREAD",
        "jsonName": "oil_inlet_glange_thread",
        "nullAllowed": true
      }, {
        "id": 34,
        "seqNum": 34,
        "dataType": "DECIMAL",
        "name": "LEAD IN CHMFR ½-ANGLE",
        "jsonName": "leadInChmfr05Angle",
        "nullAllowed": true,
        "length": 6,
        "scale": 1
      }, {
        "id": 35,
        "seqNum": 35,
        "dataType": "DECIMAL",
        "name": "LEAD IN CHMFR LEN",
        "jsonName": "leadInChmfrLen",
        "nullAllowed": true,
        "length": 6,
        "scale": 1
      }];
    }]);

})(window.angular);
