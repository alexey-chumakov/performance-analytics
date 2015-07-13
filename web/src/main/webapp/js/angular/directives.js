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
            template: '<div class="row well"><div class="col-lg-3"><span date-picker filter="filter"></span> </div><div class="col-lg-3"><button type="button" class="btn btn-default" ng-click="refresh()"><span class="glyphicon glyphicon-refresh"></span> </button> </div> </div>',
            scope: {
                'filter': '=',
                'refresh': '&'
            },
            link: function (scope, element, attr) {
                scope.$watch('filter', function() {
                    scope.refresh();
                }, true);
            }
        }
    })
;