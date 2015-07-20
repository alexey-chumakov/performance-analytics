'use strict';

angular.module('alerts', ['alerts.controllers', 'alerts.services'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/alerts', {
                templateUrl: _contextPath + '/alerts/index.html',
                controller: 'AlertsController',
                reloadOnSearch: false
            });
    }]);