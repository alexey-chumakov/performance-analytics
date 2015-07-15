'use strict';

angular.module('request-duration.controllers', [])

    .controller('RequestDurationController', ['$scope', '$location', 'RequestDurationService',
        function ($scope, $location, RequestDurationService) {
            $scope.filter = {
                startDate: moment().format("YYYY-MM-DD"),
                endDate: moment().format("YYYY-MM-DD")
            };

            $scope.durationFormatter = function(y, data) {
                return y + ' ms';
            };

            $scope.totalDurationReport = [{value:"",label:""}];
            $scope.dailyDurationReport = [];

            $scope.xkey = 'timestamp';
            $scope.ykeys = [];

            $scope.refresh = function() {
                $location.search($scope.filter).replace();
                RequestDurationService.getDurationReport($scope.filter).then(function (response) {
                    $scope.totalDurationReport = response.totalRequestDurations.map(function(totalRequestDuration) {
                        return {
                            label: totalRequestDuration.systemName,
                            value: totalRequestDuration.avgDuration
                        };
                    });
                    $scope.dailyDurationReport = response.dailyDurations;
                    var ykeys = new Set();
                    response.dailyDurations.forEach(function(dailyDuration) {
                        for (var system in dailyDuration) {
                            if (dailyDuration.hasOwnProperty(system)) {
                                ykeys.add(system);
                            }
                        }
                    });
                    ykeys.delete($scope.xkey);
                    var ykeysArr = [];
                    ykeys.forEach(function(key){
                        ykeysArr.push(key);
                    });
                    $scope.ykeys = ykeysArr;
                });
            };
        }]);