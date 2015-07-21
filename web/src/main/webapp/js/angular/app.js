'use strict';

angular.module('PerformanceAnalytics',
    ['ngRoute', 'request-info', 'request-duration', 'frequent-requests', 'slowest-requests', 'url-details', 'alerts', 'common-services', 'common-directives', 'common-filters'])

    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                otherwise({
                    redirectTo: '/'
                });
        }]);