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

    .directive('appSelector', function(ApplicationService) {

        return {
            restrict: 'EA',
            replace: true,
            scope: {
                value: '='
            },
            template: '<select class="form-control" ng-model="value" ng-options="app.value as app.key for app in apps"></select>',
            link: function (scope, element, attr) {
                scope.apps = [
                    { key: '-All Applications-', value: null }
                ].concat(ApplicationService.getAppNames().map(function(appName) {
                        return {
                            key: appName,
                            value: appName
                        };
                    }));
            }
        };
    })

    .directive('urlStatistics', function() {

        return {
            restrict: 'EA',
            replace: true,
            scope: {

                startDate: '=',
                endDate: '=',
                appName: '=',
                url: '='
            },
            template: '<a ng-href="#/url-details?startDate={{startDate}}&endDate={{endDate}}&appName={{appName}}&reqUrl={{url}}">{{url}}</a>',
            link: function (scope, element, attr) {
            }
        };
    })

    .directive('panel', function (GlobalFilter) {
        return {
            restrict: 'EA',
            replace: true,
            template: '<div><div class="col-lg-3 pull-right"><span app-selector value="filter.appName"></span></div><div class="col-lg-3 pull-right"><span date-picker filter="filter.dateRange"></span> </div></div>',
            scope: {
                'filter': '=',
                'app': '=',
                'refresh': '&'
            },
            link: function (scope, element, attr) {
                scope.filter = GlobalFilter.getFilter();
                scope.$watch('filter', function() {
                    GlobalFilter.setDateRange(scope.filter.dateRange);
                    GlobalFilter.setAppName(scope.filter.appName);
                }, true);
            }
        }
    })

    .directive('sortField', function () {
        return {
            restrict: 'EA',
            replace: true,
            template: '<a href="" ng-click="setOrder(field)">{{title}}<span ng-show="isDescending(field)" class="fa fa-caret-down"></span><span ng-show="isAscending(field)" class="fa fa-caret-up"></span> </a>',
            scope: {
                sort: '=',
                field: '@',
                title: '@'
            },
            link: function (scope, element, attr) {
                scope.setOrder = function(field){
                    if (scope.sort.field == field) {
                        scope.sort.asc = !scope.sort.asc;
                    } else {
                        scope.sort.field = field;
                        scope.sort.asc = true;
                    }
                };

                scope.isAscending = function(field){
                    return scope.sort.field == field && scope.sort.asc;
                };

                scope.isDescending = function(field){
                    return scope.sort.field == field && !scope.sort.asc;
                };
            }
        }
    })

    .directive('pagination', function () {
        return {
            restrict: 'EA',
            replace: true,
            template: '<ul class="pagination">' +
            '<li ng-class="{disabled: !hasPrev()}" ng-click="previous()"><a href>&lsaquo;</a></li> ' +
            '<li ng-repeat="p in pagesArr" ng-class="{active: isActive(p)}" ng-click="setPage(p)"><a href>{{ p }}</a></li> ' +
            '<li ng-show="showDots()" ng-click="next()"><a href>...</a></li>' +
            '<li ng-class="{disabled: !hasNext()}" ng-click="next()"><a href>&rsaquo;</a></li> ' +
            '</ul>',
            scope: {
                pagination: '=',
                hasMore: '&'
            },
            link: function (scope, element, attr) {
                var pagesArray = function() {
                    var pagesArr=[];
                    var lastPage;
                    if (scope.pagination.totalPages) {
                        lastPage = scope.pagination.totalPages;
                    } else {
                        lastPage = scope.pagination.page;
                    }
                    for (var i = 1; i <= lastPage; i++) {
                        pagesArr.push(i);
                    }
                    return pagesArr;
                };

                scope.setPage = function(page){
                    scope.pagination.page = page;
                };

                scope.previous = function() {
                    if (scope.hasPrev()) {
                        scope.pagination.page--;
                    }
                };
                scope.hasPrev = function() {
                    return scope.pagination.page > 1;
                };

                scope.next = function() {
                    if (scope.hasNext()) {
                        scope.pagination.page++;
                        if (scope.pagesArr[scope.pagesArr.length - 1] < scope.pagination.page) {
                            scope.pagesArr.push(scope.pagination.page);
                        }
                    }
                };

                scope.hasNext = function() {
                    if (typeof scope.hasMore === 'function') {
                        return scope.hasMore();
                    }
                    return false;
                };

                scope.isActive = function(page) {
                    return scope.pagination.page == page;
                };

                scope.showDots = function() {
                    return !scope.pagination.totalPages && scope.hasMore();
                };

                scope.$watch('pagination', function() {
                    scope.pagesArr = pagesArray();
                }, true);
            }
        }
    })
;
