'use strict';

angular.module('request-duration.services', [])

    .factory('RequestDurationService', function ($http, $q) {
        return {
            getDurationReport: function (filter) {
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: _contextPath + '/request/durationReport',
                    params: filter
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(deferred.reject);

                return deferred.promise;
            }
        };
    });