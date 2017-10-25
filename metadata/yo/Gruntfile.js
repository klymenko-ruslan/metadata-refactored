// Generated on 2013-09-02 using generator-angular 0.4.0
'use strict';

// # Globbing
// for performance reasons we're only matching one level down:
// 'test/unit/spec/{,*/}*.js'
// use this if you want to recursively match all subfolders:
// 'test/unit/spec/**/*.js'

module.exports = function (grunt) {

  // Time how long tasks take. Can help when optimizing build times
  require('time-grunt')(grunt);

  // Automatically load required Grunt tasks
  require('jit-grunt')(grunt, {
    useminPrepare: 'grunt-usemin',
//    ngtemplates: 'grunt-angular-templates',
//    cdnify: 'grunt-google-cdn'
  });


  grunt.loadNpmTasks('grunt-connect-proxy');

  // Configurable paths for the application
  var appConfig = {
    app: require('./bower.json').appPath || 'app',
    dist: 'dist'
  };

  // Define the configuration for all the tasks
  grunt.initConfig({

    // Project settings
    yeoman: appConfig,

    // Watches files for changes and runs tasks based on the changed files
    watch: {
      bower: {
        files: ['bower.json'],
        tasks: ['wiredep']
      },
      js: {
        files: ['<%= yeoman.app %>/scripts/**/*.js'],
        tasks: ['newer:jshint:all', 'newer:jscs:all'],
        options: {
          livereload: '<%= connect.options.livereload %>'
        }
      },
      jsTest: {
        files: ['test/unit/spec/**.js', 'test/e2e/spec/**.js'],
        tasks: ['newer:jshint:test', 'newer:jscs:test', 'karma']
      },
      compass: {
        files: ['<%= yeoman.app %>/styles/**/*.{scss,sass}'],
        tasks: ['compass:server', 'postcss:server']
      },
      gruntfile: {
        files: ['Gruntfile.js']
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [
          '<%= yeoman.app %>/**/*.html',
          '.tmp/styles/**/*.css',
          '<%= yeoman.app %>/images/**/*.{png,jpg,jpeg,gif,webp,svg}'
        ]
      }
    },

    // The actual grunt server settings
    connect: {
      options: {
        port: 9000,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: 'localhost',
        livereload: 35729
      },
      proxies: [
        {
          context: [
            '/metadata'
          ],
          host: '127.0.0.1',
          port: 8080
        }
      ],
      livereload: {
        options: {
          open: true,
          base: [
            '.tmp',
            appConfig.app
          ],
          middleware: function (connect, options) {
            var indexHtml = connect.static('./app/index.html');
            var middlewares = [
              //connect().use(indexHtml),
              require('grunt-connect-proxy/lib/utils').proxyRequest,
              connect().use('/bower_components', connect.static('./bower_components')),
              connect().use('/app/styles', connect.static('./app/styles')),
              connect().use(
                '/styles/ng-table.min.css',
                connect.static('./node_modules/ng-table/bundles/ng-table.min.css')
              ),
              connect().use(
                '/scripts/ng-table.min.js',
                connect.static('./node_modules/ng-table/bundles/ng-table.min.js')
              ),
              connect().use(
                '/styles/fonts/',
                connect.static('./bower_components/fontawesome/fonts')
              ),
              connect().use('/', indexHtml),
              connect().use('/part/list', indexHtml),
              connect().use('/service/list', indexHtml),
              connect().use('/changelog/list', indexHtml),
              connect().use('/parttype', indexHtml),
              connect().use('/other', indexHtml),
              connect().use('/security', indexHtml),
              connect().use('/password', indexHtml),
              connect().use('/application', indexHtml),
              connect().use('/bom', indexHtml),
              connect().use('/changelog', indexHtml),
              connect().use('/changelog/source', indexHtml),
              connect().use('/changelog/source/58', indexHtml),
              connect().use('/mas90/sync/status', indexHtml),
              connect().use('/manufacturer/list', indexHtml),
              connect().use('/changelog/source/name/list', indexHtml),
              connect().use('/changelog/source/list', indexHtml),
              connect().use('/changelog/source/create', indexHtml),
              connect().use('/changelog/source', indexHtml),
              connect().use('/criticaldimension/enums', indexHtml),
              connect().use('/search/indexing/status', indexHtml),
            ];

            var partIdsUnderDebug = [
              1, 2, 3,
              289,    // 'Where used (ancestors)'
              1449, 2318,
              6751,
              6233,   // Many BOMs; Tabs: 'Prices', 'Also Bought'
              6246,
              6681,   // Turbo
              17415, 25493,  // Tabs: 'Applications'
              29878,  // Tabs: 'Applications'
              33284,
              42768,  // Tabs: 'Critical Dimensions'
              43754, 43889,
              45456, 45328 /* 'Where used (ancestors)'*/, // Tabs: 'Prices', 'Also Bought' + tab 'Turbo Types' has record
              45495,  // Tabs: 'Non Standard'
              45524,  // Tabs: 'Non Standard', many records 'non standard parts'
              46722,  // Tabs: 'Non Standard', many records 'standard parts'
              46598,  // Tabs: 'Turbo Types'
              46730,  // Tabs: 'Turbo Types'
              47842,  // 'Where used (ancestors)'
              10756, 10757,
              63398, 64449,
              69690, 70079, 70090   // Tabs: 'Turbos'
            ];
            var urlSuffixes = ['', '/bom/search', '/ancestors', '/parentbom/search', '/oversize/add', '/application/search'];
            for(let partId of partIdsUnderDebug) {
              for(let s of urlSuffixes) {
                var url = '/part/' + partId + s;
                var entry = connect().use(url, indexHtml);
                middlewares.push(entry);
              }
            }
            middlewares.push(connect().use('/part/1/bom/4/alt/31726739', indexHtml));
            middlewares.push(connect().use('/part/1/bom/4/alt/31722278', indexHtml));

            if (!Array.isArray(options.base)) {
              options.base = [options.base];
            }
            // Setup the proxy
            // middlewares.push(require('grunt-connect-proxy/lib/utils').proxyRequest);
            // Serve static files
            options.base.forEach(function(base) {
              middlewares.push(connect.static(base));
            });
            return middlewares;
          }
        }
      },
      test: {
        options: {
          port: 9001,
          middleware: function (connect) {
            return [
              connect.static('.tmp'),
              connect.static('test'),
              connect().use(
                '/bower_components',
                connect.static('./bower_components')
              ),
              connect.static(appConfig.app)
            ];
          }
        }
      },
      dist: {
        options: {
          open: true,
          base: '<%= yeoman.dist %>'
        }
      }
    },

    // Make sure there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        reporter: require('jshint-stylish')
      },
      all: {
        src: [
          'Gruntfile.js',
          '<%= yeoman.app %>/scripts/**/*.js',
          '!<%= yeoman.app %>/scripts/highlight.min.js'
        ]
      },
      test: {
        options: {
          jshintrc: 'test/.jshintrc'
        },
        // , 'test/e2e/spec/**/*.js'
        src: ['test/unit/spec/**/*.js']
      }
    },

    // Make sure code styles are up to par
    jscs: {
      options: {
        config: '.jscsrc',
        verbose: true
      },
      all: {
        src: [
          'Gruntfile.js',
          '<%= yeoman.app %>/scripts/**/*.js',
          '!<%= yeoman.app %>/scripts/highlight.min.js'
        ]
      },
      test: {
        src: ['test/unit/spec/{**/*.js', 'test/e2e/spec/**/*.js']
      }
    },

    // Empties folders to start fresh
    clean: {
      dist: {
        files: [{
          dot: true,
          src: [
            '.tmp',
            '<%= yeoman.dist %>/**/*',
            '!<%= yeoman.dist %>/.git{,*/}*'
          ]
        }]
      },
      server: '.tmp'
    },

    // Add vendor prefixed styles
    postcss: {
      options: {
        processors: [
          require('autoprefixer-core')({browsers: ['last 1 version']})
        ]
      },
      server: {
        options: {
          map: true
        },
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      },
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      }
    },

    // Automatically inject Bower components into the app
    wiredep: {
      app: {
        src: ['<%= yeoman.app %>/index.html'],
        exclude: [
            'bower_components/lodash/lodash.js',
            'bower_components/bootstrap-sass/assets/javascripts/bootstrap.js'
        ],
        ignorePath:  /\.\.\//
      },
      test: {
        devDependencies: true,
        src: '<%= karma.unit.configFile %>',
        exclude: [
            'bower_components/lodash/lodash.js',
            'bower_components/bootstrap-sass/assets/javascripts/bootstrap.js'
        ],
        ignorePath:  /\.\.\//,
        fileTypes:{
          js: {
            block: /(([\s\t]*)\/{2}\s*?bower:\s*?(\S*))(\n|\r|.)*?(\/{2}\s*endbower)/gi,
              detect: {
                js: /'(.*\.js)'/gi
              },
              replace: {
                js: '\'{{filePath}}\','
              }
            }
          }
      },
      sass: {
        src: ['<%= yeoman.app %>/styles/{,*/}*.{scss,sass}'],
        ignorePath: /(\.\.\/){1,2}bower_components\//
      }
    },

    // Compiles Sass to CSS and generates necessary files if requested
    compass: {
      options: {
        sassDir: '<%= yeoman.app %>/styles',
        cssDir: '.tmp/styles',
        generatedImagesDir: '.tmp/images/generated',
        imagesDir: '<%= yeoman.app %>/images',
        javascriptsDir: '<%= yeoman.app %>/scripts',
        fontsDir: '<%= yeoman.app %>/styles/fonts',
        importPath: './bower_components',
        httpImagesPath: '/images',
        httpGeneratedImagesPath: '/images/generated',
        httpFontsPath: '/styles/fonts',
        relativeAssets: false,
        assetCacheBuster: false,
        raw: 'Sass::Script::Number.precision = 10\n'
      },
      dist: {
        options: {
          generatedImagesDir: '<%= yeoman.dist %>/images/generated'
        }
      },
      server: {
        options: {
          sourcemap: true
        }
      }
    },

    // Renames files for browser caching purposes
    filerev: {
      dist: {
        src: [
          '<%= yeoman.dist %>/scripts/{,*/}*.js',
          '<%= yeoman.dist %>/styles/{,*/}*.css',
          '<%= yeoman.dist %>/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}',
          '<%= yeoman.dist %>/styles/fonts/*'
        ]
      }
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      html: '<%= yeoman.app %>/index.html',
      options: {
        dest: '<%= yeoman.dist %>',
        flow: {
          html: {
            steps: {
              js: ['concat', 'uglifyjs'],
              css: ['cssmin']
            },
            post: {}
          }
        }
      }
    },

    // Performs rewrites based on filerev and the useminPrepare configuration
    usemin: {
      html: ['<%= yeoman.dist %>/**/*.html'],
      css: ['<%= yeoman.dist %>/styles/{,*/}*.css'],
      js: ['<%= yeoman.dist %>/scripts/{,*/}*.js'],
      options: {
        assetsDirs: [
          '<%= yeoman.dist %>',
          '<%= yeoman.dist %>/images',
          '<%= yeoman.dist %>/styles',
          '<%= yeoman.dist %>/styles/fonts'
        ],
        patterns: {
          js: [[/(images\/[^''""]*\.(png|jpg|jpeg|gif|webp|svg))/g, 'Replacing references to images']]
        }
      }
    },

    imagemin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.{png,jpg,jpeg,gif}',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },

    svgmin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.svg',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },

    htmlmin: {
      dist: {
        options: {
          collapseWhitespace: true,
          conservativeCollapse: true,
          collapseBooleanAttributes: true,
          removeCommentsFromCDATA: true
        },
        files: [{
          expand: true,
          cwd: '<%= yeoman.dist %>',
          src: ['*.html'],
          dest: '<%= yeoman.dist %>'
        }]
      }
    },

//    ngtemplates: {
//      dist: {
//        options: {
//          module: 'mainApp',
//          htmlmin: '<%= htmlmin.dist.options %>',
//          usemin: 'scripts/scripts.js'
//        },
//        cwd: '<%= yeoman.app %>',
//        src: 'views/{,*/}*.html',
//        dest: '.tmp/templateCache.js'
//      }
//    },

    // ng-annotate tries to make the code safe for minification automatically
    // by using the Angular long form for dependency injection.
    ngAnnotate: {
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/concat/scripts',
          src: '*.js',
          dest: '.tmp/concat/scripts'
        }]
      }
    },

//    // Replace Google CDN references
//    cdnify: {
//      dist: {
//        html: ['<%= yeoman.dist %>/*.html']
//      }
//    },

    // Copies remaining files to places other tasks can use
    copy: {
      dist: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>',
          src: [
            '*.{ico,png,txt}',
            '*.html',
            'views/**/*.html',
            'images/{,*/}*.{webp}',
            'styles/fonts/{,*/}*.*',
            'styles/highlight.min.css'
          ]
        }, {
          expand: true,
          cwd: 'node_modules/ng-table/bundles',
          dest: '<%= yeoman.dist %>/styles',
          src: ['ng-table.min.css']
        }, {
          expand: true,
          cwd: 'node_modules/ng-table/bundles',
          dest: '<%= yeoman.dist %>/scripts',
          src: ['ng-table.min.js']
        }, {
          expand: true,
          cwd: '.tmp/images',
          dest: '<%= yeoman.dist %>/images',
          src: ['generated/*']
        }, {
          expand: true,
          cwd: 'bower_components/bootstrap-sass/assets/fonts/bootstrap',
          src: '**',
          dest: '<%= yeoman.dist %>/styles/fonts/'
        }, {
          expand: true,
          cwd: 'bower_components/fontawesome/fonts',
          src: '**',
          dest: '<%= yeoman.dist %>/styles/fonts/'
        }]
      },
      styles: {
        expand: true,
        cwd: '<%= yeoman.app %>/styles',
        dest: '.tmp/styles/',
        src: '{,*/}*.css'
      }
    },

    // Run some tasks in parallel to speed up the build process
    concurrent: {
      server: [
        'compass:server'
      ],
      test: [
        'compass'
      ],
      dist: [
        'compass:dist',
        'imagemin',
        'svgmin'
      ]
    },

    // Test settings
    karma: {
      options: {
        configFile: 'test/unit/karma.conf.js',
      },
      unit: {
        singleRun: true
      },
      'unit-dev': {
        singleRun: false
      }
    },

  });

  grunt.registerTask('serve', 'Compile then start a connect web server', function (target) {
    if (target === 'dist') {
      return grunt.task.run(['build', 'connect:dist:keepalive']);
    }

    grunt.task.run([
      'clean:server',
      'wiredep',
      'concurrent:server',
      'postcss:server',
      'configureProxies:server',
      'connect:livereload',
      'watch'
    ]);
  });

  grunt.registerTask('server', 'DEPRECATED TASK. Use the "serve" task instead', function (target) {
    grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
    grunt.task.run(['serve:' + target]);
  });

  grunt.registerTask('test', [
    'clean:server',
    'wiredep',
    'concurrent:test',
    'postcss',
    'connect:test',
    'karma:unit'
  ]);

  grunt.registerTask('test-dev', [
    'clean:server',
    'wiredep',
    'concurrent:test',
    'postcss',
    'connect:test',
    'karma:unit-dev'
  ]);

  grunt.registerTask('build', [
    'clean:dist',
    'wiredep',
    'useminPrepare',
    'concurrent:dist',
    'postcss',
//    'ngtemplates',
    'concat',
    'ngAnnotate',
    'copy:dist',
//    'cdnify',
    'cssmin',
    'uglify',
    'filerev',
    'usemin',
    'htmlmin'
  ]);

  grunt.registerTask('default', [
    'newer:jshint',
    'newer:jscs',
    'test',
    'build'
  ]);
};
