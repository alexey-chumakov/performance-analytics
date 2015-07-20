'use strict';

angular.module('PerformanceAnalytics',
    ['ngRoute', 'request-info', 'request-duration', 'frequent-requests', 'slowest-requests', 'alerts', 'common-directives', 'common-filters'])

    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                otherwise({
                    redirectTo: '/'
                });
        }]);