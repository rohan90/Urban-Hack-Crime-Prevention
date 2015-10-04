(function(){

  angular
       .module('create')
       .controller('CreateController', [
          '$scope','menuService', '$mdSidenav', '$mdBottomSheet', '$log', '$q','$location','$mdDialog',
          CreateController
       ]);

  /**
   * Main Controller for the Angular Material Starter App
   * @param $scope
   * @param $mdSidenav
   * @param avatarsService
   * @constructor
   */
  function CreateController( $scope,menuService, $mdSidenav, $mdBottomSheet, $log, $q,$location,$mdDialog) {
    var self = this;

    
    $scope.showAlert = function(ev) {
    // Appending dialog to document.body to cover sidenav in docs app
    // Modal dialogs should fully cover application
    // to prevent interaction outside of dialog
    $mdDialog.show(
      $mdDialog.alert()
        .parent(angular.element(document.querySelector('#popupContainer')))
        .clickOutsideToClose(true)
        .title('Feature not released')
        .content('This feature is not available because there is <br> no authorization implementations for this UI')
        .ariaLabel('Alert Dialog')
        .ok('Got it!')
        .targetEvent(ev)
    );
  };

  $scope.showAlert();
}
})();


