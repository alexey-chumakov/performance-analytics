'use strict';

angular.module('PerformanceAnalytics',
    ['ngRoute', 'request-info', 'request-duration', 'common-directives', 'common-filters'])

    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                otherwise({
                    redirectTo: '/'
                });
        }]);