'use strict';

angular.module('request-info.services', [])

    .factory('RequestService', function ($http, $q) {
        return {
            getRequests: function (filter) {
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: _contextPath + '/request/forPeriod',
                    params: filter
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(deferred.reject);

                return deferred.promise;
            }
        };
    });