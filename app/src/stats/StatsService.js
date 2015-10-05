(function(){
  'use strict';

  angular.module('stats')
         .service('statsService', ['$http','$q', StatsService]);

  /**
   * Users DataService
   * Uses embedded, hard-coded data model; acts asynchronously to simulate
   * remote data service call(s).
   *
   * @returns {{loadAll: Function}}
   * @constructor
   */
  function StatsService($http,$q){
        //TODO make url constants field
        // var url = "http://localhost:3000/api/v1";
        var url = "http://urbanhack-be.herokuapp.com//api/v1";

        // interface
        var service = {
            getReportsByCount: getReportsByCount,
            getReportsCount: getReportsCount,
            getAllReports: getAllReports
        };
        return service;

        // implementation
        function getReportsByCount(key,value) {
            var def = $q.defer();
            $http.get(url+"/reports/count/"+key+"/"+value)
                .success(function(data) {
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get reports");
                });
            return def.promise;
            }

        function getReportsCount() {
            var def = $q.defer();
            $http.get(url+"/reports/count")
                .success(function(data) {
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get reports");
                });
            return def.promise;
            }    

         function getAllReports(lat,long,radius) {
            var def = $q.defer();
            $http.get(url+"/articles/"+lat+"/"+long+"/"+radius)
                .success(function(data) {
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get reports");
                });
            return def.promise;
            }       
    };
})();
