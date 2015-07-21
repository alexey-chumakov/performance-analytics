'use strict';

angular.module('url-details.controllers', [])

    .controller('UrlDetailsController', ['$scope', '$location', '$routeParams', '$filter', 'UrlDetailsService',
        function ($scope, $location, $routeParams, $filter, UrlDetailsService) {
            $scope.filter = {
                startDate: $routeParams.startDate,
                endDate: $routeParams.endDate
            };
            $scope.reqUrl = $routeParams.reqUrl;
            $scope.appName = $routeParams.appName;
            $scope.durationFormatter = function(y, data) {
                return $filter('number')(y, 2) + ' ms';
            };
            $scope.xkey = 'timestamp';

            $scope.reports = [];

            $scope.refresh = function() {
                if ($scope.appName.length == 0) {
                    $scope.appName = null;
                }
                var filter = angular.extend({
                    reqUrl: $scope.reqUrl,
                    appName: $scope.appName
                }, $scope.filter);
                $location.search(filter).replace();
                UrlDetailsService.getDetails(filter).then(function (response) {
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
                            url: report.url,
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

            $scope.refresh();
        }]);