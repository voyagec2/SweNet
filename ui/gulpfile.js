(function(){
    'use strict';

    var stream = require('stream-series');
    var gulp = require('gulp');
    var $ = require('gulp-load-plugins')({
      pattern: ['gulp-*' ]
    });

    gulp.task('wiredep', function () {
        var libJs = gulp.src('web/js/lib/*.js', { read: false });
        var appJs = gulp.src(['web/js/**/*.js', '!web/js/lib/*.js'], { read: false });
        
      gulp.src('web/index.html')
        .pipe($.inject(stream(libJs, appJs), { relative: true}))
        .pipe($.inject(gulp.src(['web/css/**/*.css', 'web/js/lib/*.css' ]), { read: false, relative: true}))
        .pipe(gulp.dest('web'))
        .pipe($.size());
    });
    
    // export css for dist
    gulp.task('css', function () {
      return gulp.src('web/css/*.css')
        .pipe(gulp.dest('.tmp/css'))
        .pipe($.size());
    });

    // check js
    gulp.task('js', function () {
      return gulp.src(['web/js/*.js', '*.js'])
        .pipe($.jshint())
        .pipe($.jshint.reporter('jshint-stylish'))
        .pipe($.jshint.reporter('fail'))
        .pipe($.size());
    });

    // export views for dist
    gulp.task('view', function () {
      return gulp.src('web/tpl/**/*.html')
        .pipe($.minifyHtml({
          empty: true,
          spare: true,
          quotes: true
        }))
        .pipe($.ngHtml2js({
          moduleName: 'qqiangwu.tpl',
          prefix: 'tpl/'
        }))
        .pipe(gulp.dest(".tmp/tpl"))
        .pipe($.size());
    });

    // prepare resource for dist
    gulp.task('resource', function(){
        gulp.src('web/music/**').pipe(gulp.dest('dist/music')).pipe($.size());
        gulp.src('web/img/**').pipe(gulp.dest('dist/img')).pipe($.size());
        gulp.src('web/favicon.ico').pipe(gulp.dest('dist')).pipe($.size());
    });

    // prepare all for dist
    gulp.task('html', [ 'view' ], function () {
      var assets = $.useref.assets();

      return gulp.src('web/*.html')
        .pipe($.inject(gulp.src('.tmp/tpl/**/*.js'), {
              read: false,
              starttag: '<!-- inject:tpl -->',
              addRootSlash: false,
              addPrefix: '../'
         }))
        .pipe(assets)
        .pipe($.rev())
        .pipe($.if('*.js', $.uglify()))
        .pipe($.if('*.css', $.csso()))
        .pipe(assets.restore())
        .pipe($.useref())
        .pipe($.revReplace())
        .pipe($.if('*.html', $.minifyHtml({
            empty: true,
            spare: true,
            quotes: true
        })))
        .pipe(gulp.dest('dist'))
        .pipe($.size());
    });

    gulp.task('clean', function () {
      return gulp.src(['.tmp', 'dist'], { read: false }).pipe($.clean());
    });

    gulp.task('build', [ 'wiredep', 'js', 'css', 'html', 'resource' ]);
    gulp.task('dev', [ 'wiredep', 'js' ]);

    gulp.task('default', ['clean'], function() {
        gulp.start('build');
    });
})();