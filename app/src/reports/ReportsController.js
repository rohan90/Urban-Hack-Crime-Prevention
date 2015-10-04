(function(){

  angular
       .module('reports')
       .controller('ReportsController', ['$scope',
          'reportsService', '$mdSidenav', '$mdBottomSheet', '$log', '$q',
          ReportsController
       ]);

  /**
   * Main Controller for the Angular Material Starter App
   * @param $scope
   * @param $mdSidenav
   * @param avatarsService
   * @constructor
   */
  function ReportsController( $scope,reportsService, $mdSidenav, $mdBottomSheet, $log, $q) {
    var self = this;

    self.selected     = null;
    self.reports        = null;
    self.selectUser   = selectUser;
    self.heading   = "showing stats";
    //self.toggleList   = toggleUsersList;
    //self.showContactOptions  = showContactOptions;

    // Load all registered users

    reportsService
          .getReports()
          .then( function( reports ) {
            console.log('reports fetched ...size '+reports.size)
            self.reports    = reports.data;
            self.selected = reports[0];
          });

    
    /**
     * Select the current avatars
     * @param menuId
     */
    function selectUser ( user ) {
      self.selected = angular.isNumber(user) ? $scope.users[user] : user;
      self.toggleList();
    }

  }

})();
