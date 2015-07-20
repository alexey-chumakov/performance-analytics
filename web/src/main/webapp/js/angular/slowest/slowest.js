'use strict';

angular.module('slowest-requests', ['slowest-requests.controllers', 'slowest-requests.services'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/slowest', {
                templateUrl: _contextPath + '/slowest/index.html',
                controller: 'SlowestRequestsController',
                reloadOnSearch: false
            });
    }]);