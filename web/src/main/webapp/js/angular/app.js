'use strict';

angular.module('PerformanceAnalytics',
    ['ngRoute', 'request-info', 'request-duration', 'frequent-requests', 'slowest-requests', 'common-directives', 'common-filters'])

    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                otherwise({
                    redirectTo: '/'
                });
        }]);