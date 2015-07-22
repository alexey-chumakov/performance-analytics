'use strict';

angular.module('common-services', [])

    .constant('appConstants', {
        appNamesListURL: _contextPath + '/application/names'
    })

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
    })

    .factory('AppCache', function ($cacheFactory) {
        return $cacheFactory('cache');
    })

    .factory('ApplicationService', function (AppCache, appConstants, $q, $http) {

        return {
            getAppNames: function() {
                var result = AppCache.get(appConstants.appNamesListURL);
                if (result == null || result.length == 0) {
                    jQuery.ajax({
                        method: 'GET',
                        url: appConstants.appNamesListURL,
                        dataType: 'json',
                        async: false,
                        success: function (data) {
                            result = data;
                        },
                        error: function (data) {
                            result = [];
                        }
                    });
                    AppCache.put(appConstants.appNamesListURL, result);
                }
                return result;
            }
        };
    })
;