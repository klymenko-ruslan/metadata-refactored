// Karma configuration
// Generated on 2017-05-04

module.exports = function(config) {
  'use strict';

  config.set({
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,

    // base path, that will be used to resolve files and exclude
    basePath: '../../',

    // testing framework to use (jasmine/mocha/qunit/...)
    // as well as any additional frameworks (requirejs/chai/sinon/...)
    frameworks: [
      'jasmine'
    ],

    // list of files / patterns to load in the browser
    files: [
      // bower:js
      'bower_components/es5-shim/es5-shim.js',
      'bower_components/jquery/dist/jquery.js',
      'bower_components/json3/lib/json3.js',
      'bower_components/underscore/underscore.js',
      'bower_components/angular/angular.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-cookies/angular-cookies.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
      'bower_components/bootstrap/dist/js/bootstrap.js',
      'bower_components/angular-sanitize/angular-sanitize.js',
      'bower_components/angular-translate/angular-translate.js',
      'bower_components/angular-dialog-service/dist/dialogs.js',
      'bower_components/angular-dialog-service/dist/dialogs-default-translations.js',
      'bower_components/restangular/dist/restangular.js',
      'bower_components/angucomplete-alt/angucomplete-alt.js',
      'bower_components/json-formatter/dist/json-formatter.js',
      'bower_components/angular-loading-bar/build/loading-bar.js',
      'bower_components/angular-highlightjs/build/angular-highlightjs.js',
      'bower_components/marked/lib/marked.js',
      'bower_components/angular-marked/dist/angular-marked.js',
      'bower_components/bootstrap-markdown/js/bootstrap-markdown.js',
      'bower_components/angular-markdown-editor-ghiscoding/src/angular-markdown-editor.js',
      'bower_components/dropzone/dist/min/dropzone.min.js',
      'bower_components/filereader.js/filereader.js',
      'bower_components/angular-toastr/dist/angular-toastr.tpls.js',
      'bower_components/jsondiffpatch/public/build/jsondiffpatch-full.js',
      'bower_components/jsondiffpatch/public/build/jsondiffpatch-formatters.js',
      'bower_components/angularjs-dropdown-multiselect/dist/angularjs-dropdown-multiselect.min.js',
      'bower_components/moment/moment.js',
      'bower_components/bootstrap-daterangepicker/daterangepicker.js',
      'bower_components/angular-daterangepicker/js/angular-daterangepicker.js',
      'bower_components/angular-mocks/angular-mocks.js',
      // endbower
      'node_modules/ng-table/bundles/ng-table.min.js',
      'app/scripts/**/*.js',
      'test/unit/spec/**/*.js',
      //'test/unit/spec/controllers/other/SalesNoteList.js',
      //'test/unit/spec/controllers/application/carengine/form.js',
      //'test/unit/spec/controllers/chlogsrc/*.js'
    ],

    // list of files / patterns to exclude
    exclude: [
    ],

    // web server port
    port: 8080,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: [
      'PhantomJS'
    ],

    // Which plugins to enable
    plugins: [
      'karma-phantomjs-launcher',
      'karma-jasmine'
    ],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    colors: true,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,

    // Uncomment the following lines if you are using grunt's server to run the tests
    // proxies: {
    //   '/': 'http://localhost:9000/'
    // },
    // URL root prevent conflicts with the site root
    // urlRoot: '_karma_'

    getPageTimeout: 10000
  });
};
