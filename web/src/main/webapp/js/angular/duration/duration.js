'use strict';

angular.module('request-duration', ['request-duration.controllers', 'request-duration.services'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/duration', {
                templateUrl: _contextPath + '/duration/index.html',
                controller: 'RequestDurationController',
                reloadOnSearch: false
            });
    }]);