'use strict';

angular.module('request-info.controllers', [])

    .controller('RequestController', ['$scope', '$location', 'RequestService',
        function ($scope, $location, RequestService) {
            $scope.filter = {
                startDate: moment().format("YYYY-MM-DD"),
                endDate: moment().format("YYYY-MM-DD")
            };

            $scope.requests = [];

            $scope.refresh = function() {
                $location.search($scope.filter).replace();
                RequestService.getRequests($scope.filter).then(function (response) {
                    $scope.requests = response;
                });
            };
        }]);