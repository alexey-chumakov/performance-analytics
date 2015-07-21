'use strict';

angular.module('frequent-requests.controllers', [])

    .controller('FrequentRequestsController', ['$scope', '$location', 'FrequentRequestsService',
        function ($scope, $location, FrequentRequestsService) {
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
                FrequentRequestsService.getRequests(filter).then(function (response) {
                    $scope.requests = response;
                });
            };

            $scope.$watch('appName', function() {
                $scope.refresh();
            }, true);
        }]);