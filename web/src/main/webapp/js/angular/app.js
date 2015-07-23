'use strict';

angular.module('PerformanceAnalytics',
    ['ngRoute', 'request-info', 'request-duration', 'url-details', 'alerts', 'common-services', 'common-directives', 'common-filters'])

    .config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                otherwise({
                    redirectTo: '/'
                });
            $routeProvider.
                when('/', {
                    templateUrl: _contextPath + '/main.html',
                    reloadOnSearch: false
                });
        }]);