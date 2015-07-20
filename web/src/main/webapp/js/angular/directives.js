'use strict';

angular
    .module('common-directives', [])

    .directive('datePicker', function () {
        return {
            restrict: 'EA',
            replace: true,
            template: '<div class="input-group"><input type="text" name="daterange" class="form-control" value="{{dateRangeText}}"/><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></div>',
            scope: {
                'filter': '='
            },
            link: function (scope, element, attr) {
                var updateText = function (start, end) {
                    scope.dateRangeText = start + ' - ' + end;
                };
                var dateRangeInput = $('input[name="daterange"]');
                dateRangeInput.daterangepicker(
                    {
                        ranges: {
                            'Today': [moment(), moment()],
                            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                            'This Month': [moment().startOf('month'), moment().endOf('month')],
                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                        },
                        timePicker: true,
                        format: 'YYYY-MM-DD',
                        timePickerIncrement: 60,
                        timePickerSeconds: false
                    },
                    function (start, end) {
                        scope.$apply(function () {
                            scope.filter.startDate = moment(start).format('YYYY-MM-DD');
                            scope.filter.endDate = moment(end).format('YYYY-MM-DD');
                        });
                    }
                ).on('show.daterangepicker', function () {
                        dateRangeInput.data('daterangepicker').showCalendars();
                    });

                scope.$watch('filter', function () {
                    dateRangeInput.data('daterangepicker').setStartDate(scope.filter.startDate);
                    dateRangeInput.data('daterangepicker').setEndDate(scope.filter.endDate);

                    updateText(scope.filter.startDate, scope.filter.endDate);
                }, true);
            }
        }
    })

    .directive('controlPanel', function () {
        return {
            restrict: 'EA',
            replace: true,
            template: '<div class="row well"><div class="col-lg-3"><span date-picker filter="filter"></span> </div><div class="col-lg-3"><span app-selector value="app"></span></div><div class="col-lg-3"><button type="button" class="btn btn-default" ng-click="refresh()"><span class="glyphicon glyphicon-refresh"></span> </button> </div> </div>',
            scope: {
                'filter': '=',
                'app': '=',
                'refresh': '&'
            },
            link: function (scope, element, attr) {
                scope.$watch('filter', function() {
                    scope.refresh();
                }, true);
            }
        }
    })

    .directive('pieChart', function() {

        return {
            restrict: 'E',
            template: '<div style="height: 230px"></div>',
            scope: {
                data: "=",
                formatter: "="
            },
            replace: true,
            link: function (scope, element, attrs) {

                var chart = Morris.Donut({
                    element: element,
                    data: scope.data,
                    formatter: scope.formatter
                });

                scope.$watch('data', function() {
                    chart.setData(scope.data);
                }, true);
            }
        };
    })

    .directive('lineChart', function() {

        return {
            restrict: 'E',
            template: '<div style="height: 230px"></div>',
            scope: {
                data: "=",
                xkey: "=",
                ykeys: "=",
                labels: "=",
                postUnits: "@",
                xLabels: "@"
            },
            replace: true,
            link: function (scope, element, attrs) {

                var chart = Morris.Line({
                    element: element,
                    data: scope.data,
                    xkey: scope.xkey,
                    ykeys: scope.ykeys,
                    labels: scope.labels,
                    postUnits: scope.postUnits,
                    xLabels: 'day',
                    dateFormat: function(millis) {
                        return moment(millis).format("YYYY-MM-DD")
                    }
                });

                scope.$watch('data', function() {
                    chart.options.ykeys = scope.ykeys;
                    chart.options.labels = scope.labels;
                    chart.setData(scope.data);
                }, true);
            }
        };
    })

    .directive('appSelector', function() {

        return {
            restrict: 'EA',
            replace: true,
            scope: {
                value: '='
            },
            template: '<select class="form-control" ng-model="value" ng-options="app.value as app.key for app in apps"></select>',
            link: function (scope, element, attr) {
                scope.apps = [
                    {key: '--All apps--',
                        value: null},
                    {key: '/heimdall',
                        value: '/heimdall'}
                ];
            }
        };
    })
;
