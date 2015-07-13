'use strict';

angular.module('common-filters', [])

    .filter('millisecondsToHHMMSS', function() {
        return function (data) {
            var d = moment.duration(data);
            return Math.floor(d.asHours()) + moment.utc(data).format(":mm:ss");
        }
    })
;
