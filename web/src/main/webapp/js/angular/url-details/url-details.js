'use strict';

angular.module('url-details', ['url-details.controllers', 'url-details.services'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/url-details', {
                templateUrl: _contextPath + '/url-details/index.html',
                controller: 'UrlDetailsController',
                reloadOnSearch: false
            });
    }]);