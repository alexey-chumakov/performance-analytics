'use strict';

angular.module('frequent-requests', ['frequent-requests.controllers', 'frequent-requests.services'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/frequent', {
                templateUrl: _contextPath + '/frequent/index.html',
                controller: 'FrequentRequestsController',
                reloadOnSearch: false
            });
    }]);