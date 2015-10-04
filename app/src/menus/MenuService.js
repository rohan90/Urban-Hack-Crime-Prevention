(function(){
  'use strict';

  angular.module('menus')
         .service('menuService', ['$q', menuService]);

  /**
   * menus DataService
   * Uses embedded, hard-coded data model; acts asynchronously to simulate
   * remote data service call(s).
   *
   * @returns {{loadAll: Function}}
   * @constructor
   */
  function menuService($q){
    var menus = [
      {
        name: 'Statistics',
        avatar: 'svg-1',
        content: 'Contains analytics UI',
        route: 'stats'
      },
      {
        name: 'Reports',
        avatar: 'svg-2',
        content: 'Fetch all Reports',
        route: 'reports'
      },
      {
        name: 'Create Report',
        avatar: 'svg-3',
        content: 'Create a report',
        route: 'create'
      },
      
    ];

    // Promise-based API
    return {
      loadAllMenus : function() {
        // Simulate async nature of real remote calls
        return $q.when(menus);
      }
    };
  }

})();
