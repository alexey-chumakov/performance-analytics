'use strict';

angular.module('request-info.controllers', [])

    .controller('RequestController', ['$scope', '$location', 'RequestService', 'GlobalFilter',
        function ($scope, $location, RequestService, GlobalFilter) {
            $scope.filter = GlobalFilter.getFilter();
            $scope.appName = null;

            $scope.requests = [];

            $scope.refresh = function() {
                var filter = angular.extend({
                    appName: $scope.filter.appName
                }, $scope.filter.dateRange);
                $location.search(filter).replace();
                RequestService.getRequests(filter).then(function (response) {
                    $scope.requests = response;
                });
            };

            $scope.$watch('filter', function() {
                $scope.refresh();
            }, true);
        }]);