'use strict';

angular.module('request-info', ['request-info.controllers', 'request-info.services'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/request', {
                templateUrl: _contextPath + '/request/index.html',
                controller: 'RequestController',
                reloadOnSearch: false
            });
    }]);