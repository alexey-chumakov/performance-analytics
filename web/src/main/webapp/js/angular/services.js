'use strict';

angular.module('common-services', [])

    .factory('GlobalFilter', function () {

        var filter = {
            dateRange: {
                startDate: moment().format("YYYY-MM-DD"),
                endDate: moment().format("YYYY-MM-DD")
            },
            appName: null
        };

        return {
            getFilter: function() {
                return filter;
            },
            setDateRange: function (dateRange) {
                filter.dateRange = dateRange;
            },
            setAppName: function (appName) {
                filter.appName = appName;
            }
        };
    });