(function(){

  angular
       .module('menus')
       .controller('MenuController', [
          'menuService', '$mdSidenav', '$mdBottomSheet', '$log', '$q','$location',
          MenuController
       ]);

  /**
   * Main Controller for the Angular Material Starter App
   * @param $scope
   * @param $mdSidenav
   * @param avatarsService
   * @constructor
   */
  function MenuController( menuService, $mdSidenav, $mdBottomSheet, $log, $q,$location) {
    var self = this;

    self.selected     = null;
    self.menus        = [ ];
    self.selectMenu   = selectMenu;
    self.toggleList   = toggleMenusList;
    self.showMenuOptions  = showMenuOptions;

    // Load all registered menus

    menuService
          .loadAllMenus()
          .then( function( menus ) {
            self.menus    = [].concat(menus);
            self.selected = menus[0];
          });

    // *********************************
    // Internal methods
    // *********************************

    /**
     * First hide the bottomsheet IF visible, then
     * hide or Show the 'left' sideNav area
     */
    function toggleMenusList() {
      var pending = $mdBottomSheet.hide() || $q.when(true);

      pending.then(function(){
        $mdSidenav('left').toggle();
      });
    }

    /**
     * Select the current avatars
     * @param menuId
     */
    function selectMenu ( menu ) {
      self.selected = angular.isNumber(menu) ? $scope.menu[menu] : menu;
      console.log("clicked on "+menu.route)
      $location.path(menu.route);
      self.toggleList();
    }

    /**
     * Show the bottom sheet
     */
    function showMenuOptions($event) {
        var menu = self.selected;

        return $mdBottomSheet.show({
          parent: angular.element(document.getElementById('content')),
          templateUrl: './src/menus/view/menuItem.html',
          controller: [ '$mdBottomSheet', MenuPanelController],
          controllerAs: "cp",
          bindToController : true,
          targetEvent: $event
        }).then(function(clickedItem) {
          clickedItem && $log.debug( clickedItem.name + ' clicked!');
        });

        /**
         * Bottom Sheet controller for the Avatar Actions
         */
        function MenuPanelController( $mdBottomSheet ) {
          this.menu = menu;
          this.actions = [
            { name: 'Statistics'       , icon: 'phone'       , icon_url: 'assets/svg/phone.svg'},
            { name: 'Reports'     , icon: 'twitter'     , icon_url: 'assets/svg/twitter.svg'},
            { name: 'Add Report'     , icon: 'google_plus' , icon_url: 'assets/svg/google_plus.svg'},
          ];
          this.submitContact = function(action) {
            $mdBottomSheet.hide(action);
          };
        }
    }

  }

})();
