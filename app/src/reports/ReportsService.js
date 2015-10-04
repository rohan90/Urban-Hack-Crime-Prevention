(function(){
  'use strict';

  angular.module('reports')
         .service('reportsService', ['$http','$q', ReportsService]);

  /**
   * Users DataService
   * Uses embedded, hard-coded data model; acts asynchronously to simulate
   * remote data service call(s).
   *
   * @returns {{loadAll: Function}}
   * @constructor
   */
  function ReportsService($http,$q){
    // interface
        var service = {
            reports: [],
            getReports: getReports
        };
        return service;

        // implementation
        function getReports() {
          console.log("fethcing reports inside reportService")
            var def = $q.defer();
//TODO make url constants field
            $http.get("http://localhost:3000/api/v1/articles")
                .success(function(data) {
                    console.log("recieved.."+data);
                    service.reports = data;
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get reports");
                });
            return def.promise;
            }
    };
})();
