'use strict';

angular.module('slowest-requests.controllers', [])

    .controller('SlowestRequestsController', ['$scope', '$location', 'SlowestRequestsService',
        function ($scope, $location, SlowestRequestsService) {
            $scope.filter = {
                startDate: moment().format("YYYY-MM-DD"),
                endDate: moment().format("YYYY-MM-DD")
            };
            $scope.appName = null;

            $scope.requests = [];

            $scope.refresh = function() {
                var filter = angular.extend({
                    appName: $scope.appName
                }, $scope.filter);
                $location.search(filter).replace();
                SlowestRequestsService.getRequests(filter).then(function (response) {
                    $scope.requests = response;
                });
            };

            $scope.$watch('appName', function() {
                $scope.refresh();
            }, true);
        }]);