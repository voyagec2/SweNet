/* global: angular */

(function(){
    'use strict';

    angular.module('SweNet.tpl', []);
    angular.module('SweNet.controller', []);
    angular.module('SweNet.service', []);
    angular.module('SweNet.resource', []);
    angular.module('SweNet.filter', []);
    angular.module('SweNet.directive', []);
    
    var app = angular.module('SweNet', [
        'ngResource', 
        'ngRoute',
        'btford.markdown',
        'angular-loading-bar',
        'SweNet.tpl',
        'SweNet.controller',
        'SweNet.service',
        'SweNet.resource',
        'SweNet.filter',
        'SweNet.directive'
    ]);

    app.config(['cfpLoadingBarProvider', 
        function(cfpLoadingBarProvider) {
            cfpLoadingBarProvider.includeSpinner = false;
        }
    ]);

    app.config(['$locationProvider',
        function($locationProvider){
            $locationProvider.html5Mode(true);
        }
    ]);

    app.config(['$routeProvider', 
        function($routeProvider){
            $routeProvider
                .when('/ui/main', {
                    templateUrl: 'tpl/main.html',
                    controller: 'MainCtrl'
                })
                .otherwise({
                        redirectTo: '/ui/main'
                });
        }
    ]);
})();