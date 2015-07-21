'use strict';

angular.module('frequent-requests.controllers', [])

    .controller('FrequentRequestsController', ['$scope', '$location', 'FrequentRequestsService', 'GlobalFilter',
        function ($scope, $location, FrequentRequestsService, GlobalFilter) {
            $scope.filter = GlobalFilter.getFilter();
            $scope.appName = null;

            $scope.requests = [];

            $scope.refresh = function() {
                var filter = angular.extend({
                    appName: $scope.filter.appName
                }, $scope.filter.dateRange);
                $location.search(filter).replace();
                FrequentRequestsService.getRequests(filter).then(function (response) {
                    $scope.requests = response;
                });
            };

            $scope.$watch('filter', function() {
                $scope.refresh();
            }, true);
        }]);