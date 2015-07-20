'use strict';

angular.module('request-duration.controllers', [])

    .controller('RequestDurationController', ['$scope', '$location', '$filter', 'RequestDurationService',
        function ($scope, $location, $filter, RequestDurationService) {
            $scope.filter = {
                startDate: moment().format("YYYY-MM-DD"),
                endDate: moment().format("YYYY-MM-DD")
            };
            $scope.appName = null;
            $scope.durationFormatter = function(y, data) {
                return $filter('number')(y, 2) + ' ms';
            };
            $scope.xkey = 'timestamp';
            $scope.reports = [];

            $scope.refresh = function() {
                var filter = angular.extend({
                    appName: $scope.appName
                }, $scope.filter);
                $location.search(filter).replace();
                RequestDurationService.getDurationReport(filter).then(function (response) {

                    var newReports = [];
                    for (var i = 0; i < response.length; i++) {
                        var report = response[i];
                        var totalDurationReport = report.totalRequestDurations.map(function(totalRequestDuration) {
                            return {
                                label: totalRequestDuration.systemName,
                                value: totalRequestDuration.avgDuration
                            };
                        });
                        var avgRequestTimes = report.totalRequestDurations.map(function(totalRequestDuration) {
                            return {
                                systemName: totalRequestDuration.systemName,
                                duration: totalRequestDuration.avgDuration
                            };
                        });
                        avgRequestTimes.push({systemName: 'Total', duration: report.totalDuration});
                        if (totalDurationReport.length == 0) {
                            totalDurationReport = [{value:"",label:""}];
                        }
                        var dailyDurationReport = report.dailyDurations;
                        var ykeys = new Set();
                        report.dailyDurations.forEach(function(dailyDuration) {
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
                        newReports.push({
                            appName: report.appName,
                            totalDurationReport: totalDurationReport,
                            dailyDurationReport: dailyDurationReport,
                            ykeys: ykeysArr,
                            avgRequestTimes: avgRequestTimes
                        });
                    }

                    $scope.reports = newReports;
                    $scope.avgRequestTimes = avgRequestTimes;
                });
            };

            $scope.$watch('appName', function() {
                $scope.refresh();
            }, true);
        }]);