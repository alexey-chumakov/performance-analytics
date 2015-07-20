'use strict';

angular.module('alerts.services', [])

    .factory('AlertsService', function ($http, $q) {
        return {
            getRequests: function (filter) {
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: _contextPath + '/alerts',
                    params: filter
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(deferred.reject);

                return deferred.promise;
            }
        };
    });