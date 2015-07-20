'use strict';

angular.module('alerts.controllers', [])

    .controller('AlertsController', ['$scope', '$location', 'AlertsService',
        function ($scope, $location, AlertsService) {
            $scope.filter = {
                startDate: moment().format("YYYY-MM-DD"),
                endDate: moment().format("YYYY-MM-DD")
            };

            $scope.appName = null;

            $scope.alerts = [];

            $scope.refresh = function() {
                AlertsService.getRequests().then(function (response) {
                    $scope.alerts = response;
                });
            };

            $scope.$watch('appName', function() {
                $scope.refresh();
            }, true);
        }]);