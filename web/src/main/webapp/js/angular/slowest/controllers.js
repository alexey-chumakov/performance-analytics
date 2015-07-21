'use strict';

angular.module('slowest-requests.controllers', [])

    .controller('SlowestRequestsController', ['$scope', '$location', 'SlowestRequestsService', 'GlobalFilter',
        function ($scope, $location, SlowestRequestsService, GlobalFilter) {
            $scope.filter = GlobalFilter.getFilter();
            $scope.appName = null;

            $scope.requests = [];

            $scope.refresh = function() {
                var filter = angular.extend({
                    appName: $scope.filter.appName
                }, $scope.filter.dateRange);
                $location.search(filter).replace();
                SlowestRequestsService.getRequests(filter).then(function (response) {
                    $scope.requests = response;
                });
            };

            $scope.$watch('filter', function() {
                $scope.refresh();
            }, true);
        }]);