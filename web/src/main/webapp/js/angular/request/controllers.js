'use strict';

angular.module('request-info.controllers', [])

    .controller('RequestController', ['$scope', '$location', 'RequestService',
        function ($scope, $location, RequestService) {
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
                RequestService.getRequests(filter).then(function (response) {
                    $scope.requests = response;
                });
            };

            $scope.$watch('appName', function() {
                $scope.refresh();
            }, true);
        }]);