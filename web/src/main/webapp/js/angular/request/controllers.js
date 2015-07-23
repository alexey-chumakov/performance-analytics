'use strict';

angular.module('request-info.controllers', [])

    .controller('RequestController', ['$scope', '$location', 'RequestService', 'GlobalFilter',
        function ($scope, $location, RequestService, GlobalFilter) {
            $scope.options = {
                filter: GlobalFilter.getFilter(),
                sort: {
                    field: 'appName',
                    asc: true
                },
                pagination: {
                    page: 1,
                    totalPages: 1,
                    isLastPage: function () {
                        return this.totalPages == 0 || this.page == this.totalPages;
                    }
                }
            };

            $scope.requests = [];

            $scope.refresh = function() {
                var filter = angular.extend({
                    appName: $scope.options.filter.appName,
                    sortField: $scope.options.sort.field,
                    ascending: $scope.options.sort.asc,
                    page: $scope.options.pagination.page
                }, $scope.options.filter.dateRange);
                $location.search(filter).replace();
                RequestService.getRequests(filter).then(function (response) {
                    $scope.requests = response.requests;
                    $scope.options.pagination.totalPages = response.totalPages;
                });
            };

            $scope.$watch('options', function(newVal, oldVal) {
                if (!areFiltersEqual(newVal.filter, oldVal.filter)) {
                    $scope.options.pagination.page = 1;
                }
                $scope.refresh();
            }, true);

            var areFiltersEqual = function(fil1, fil2) {
                return fil1.dateRange.startDate == fil2.dateRange.startDate
                    && fil1.dateRange.endDate == fil2.dateRange.endDate
                    && fil1.appName == fil2.appName;
            };

            $scope.hasMore = function() {
                return !$scope.options.pagination.isLastPage();
            };
        }]);