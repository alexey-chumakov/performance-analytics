'use strict';

angular.module('PerformanceAnalytics',
    ['ngRoute', 'request-info', 'common-directives', 'common-filters'])

    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                otherwise({
                    redirectTo: '/'
                });
        }]);