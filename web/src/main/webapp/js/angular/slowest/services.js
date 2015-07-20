'use strict';

angular.module('slowest-requests.services', [])

    .factory('SlowestRequestsService', function ($http, $q) {
        return {
            getRequests: function (filter) {
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: _contextPath + '/request/slowest',
                    params: filter
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(deferred.reject);

                return deferred.promise;
            }
        };
    });