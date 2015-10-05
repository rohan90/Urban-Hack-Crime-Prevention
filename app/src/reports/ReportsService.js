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
    // var url = "http://localhost:3000/api/v1";
    var url = "http://urbanhack-be.herokuapp.com/api/v1";

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
            $http.get(url+"/articles/0/0")
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
