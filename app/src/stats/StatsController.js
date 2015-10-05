(function(){

  angular
  .module('stats')
  .controller('StatsController', ['$scope','$interval',
    'statsService', '$mdSidenav', '$mdBottomSheet', '$log', '$q',
    StatsController
    ]);

  /**
   * Main Controller for the Angular Material Starter App
   * @param $s
   * @param $mdSidenav
   * @param avatarsService
   * @constructor
   */
   function StatsController($s, $interval,statsService, $mdSidenav, $mdBottomSheet, $log, $q) {
    var self = this;
    $s.totalReportsCount = 0;
    $s.totalReportsCountToday = 0;
    $s.totalReportsOpenCount = 0;
    $s.totalReportsSolvedCount = 0;
    $s.crimesAgainstMales = 0;
    $s.crimesAgainstFemales = 0;



    $interval(function(){
      statsService
      .getReportsCount()
      .then( function( data ) {
        $s.totalReportsCount = data.count;
      });
    },5000);

    $interval(function(){
      statsService
      .getReportsByCount("firStatus","Detected")
      .then( function( data ) {

        $s.totalReportsOpenCount = data.count;
      });
    },5000);

    $interval(function(){
      statsService
      .getReportsByCount("firStatus","Undetected")
      .then( function( data ) {

        $s.totalReportsOpenCount = data.count;
      });
    },5000);

    $interval(function(){
      statsService
      .getReportsByCount("gender","MALE")
      .then( function( data ) {

        $s.crimesAgainstMales = data.count;
      });
    },5000);

    $interval(function(){
      statsService
      .getReportsByCount("gender","FEMALE")
      .then( function( data ) {
        $s.crimesAgainstFemales = data.count;
      });
    },5000);


    $s.allReports=[];
    $interval(function(){
      statsService
            .getAllReports(13.01561604,77.678186,500)//todo remove hardcode radius and Blore values
            .then( function( data ) {
              console.log("fetched data making markers "+data.size)
              $s.allReports = data.data;
              populateMarkers($s.allReports);
            });
          },5000);

    function populateMarkers(data){
//TODO simplfy this
reportMarkers = $s.allReports;
initGoogleMap();
for (i = 0; i < reportMarkers.length; i++){
  createMarker(reportMarkers[i]);
}
}

$s.isMapLoaded = false;
$s.markers = [];    
var reportMarkers = [];

function initGoogleMap(){
// MAPS
if(!$s.isMapLoaded){
  var mapOptions = {
    zoom: 10,
    center: new google.maps.LatLng(13.01561604,77.678186),
    mapTypeId: google.maps.MapTypeId.TERRAIN
  }

  $s.map = new google.maps.Map(document.getElementById('map'), mapOptions);
  $s.isMapLoaded = true;
}
}    
var createMarker = function (info){
  var infoWindow = new google.maps.InfoWindow();

  var marker = new google.maps.Marker({
    map: $s.map,
    position: new google.maps.LatLng(info.lattitude, info.longitude),
    desc: info.role,
    title: info.title
  });
  marker.content = '<div class="infoWindowContent">' + info.desc + '</div>';

  google.maps.event.addListener(marker, 'click', function(){
    infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
    infoWindow.open($s.map, marker);
  });

  $s.markers.push(marker);

}  


$s.openInfoWindow = function(e, selectedMarker){
  e.preventDefault();
  google.maps.event.trigger(selectedMarker, 'click');
}

}

})();
