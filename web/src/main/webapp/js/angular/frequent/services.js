'use strict';

angular.module('frequent-requests.services', [])

    .factory('FrequentRequestsService', function ($http, $q) {
        return {
            getRequests: function (filter) {
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: _contextPath + '/request/frequent',
                    params: filter
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(deferred.reject);

                return deferred.promise;
            }
        };
    });