'use strict';

angular.module('request-info.controllers', [])

    .controller('RequestController', ['$scope', '$location', 'RequestService', 'GlobalFilter',
        function ($scope, $location, RequestService, GlobalFilter) {
            $scope.filter = GlobalFilter.getFilter();

            $scope.sort= {
                field: 'appName',
                asc: true
            };

            $scope.pagination = {
                page: 1,
                totalPages: 1,
                isLastPage: function(){
                    return this.totalPages == 0 || this.page == this.totalPages;
                }
            };

            $scope.requests = [];

            $scope.refresh = function() {
                var filter = angular.extend({
                    appName: $scope.filter.appName,
                    sortField: $scope.sort.field,
                    ascending: $scope.sort.asc,
                    page: $scope.pagination.page
                }, $scope.filter.dateRange);
                $location.search(filter).replace();
                RequestService.getRequests(filter).then(function (response) {
                    $scope.requests = response.requests;
                    $scope.pagination.totalPages = response.totalPages;
                });
            };

            $scope.$watch('filter', function() {
                $scope.pagination.page = 1;
                $scope.refresh();
            }, true);

            $scope.hasMore = function() {
                return !$scope.pagination.isLastPage();
            };

            $scope.paginationChanged  = function() {
                $scope.refresh();
            };

            $scope.sortingChanged  = function() {
                $scope.refresh();
            };
        }]);