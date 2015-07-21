'use strict';

angular.module('url-details.services', [])

    .factory('UrlDetailsService', function ($http, $q) {
        return {
            getDetails: function (filter) {
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: _contextPath + '/request/url-details',
                    params: filter
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(deferred.reject);

                return deferred.promise;
            }
        };
    });