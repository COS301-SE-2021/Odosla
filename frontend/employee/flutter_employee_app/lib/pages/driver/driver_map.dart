
import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/GeoPoint.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:flutter_employee_app/services/DeliveryService.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_polyline_points/flutter_polyline_points.dart';
import 'package:get_it/get_it.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:location/location.dart';
import 'package:provider/provider.dart';
class DriverMapScreen extends StatefulWidget {
  @override
  _DriverMapScreenState createState() =>  _DriverMapScreenState();
}

class  _DriverMapScreenState extends State<DriverMapScreen> {

  UserService _userService=GetIt.I.get();
  bool _updatingOnline=true;
  Delivery _delivery=new Delivery("", new GeoPoint(0.0, 0.0, ""),new GeoPoint(0.0, 0.0, ""), "","", "", "", "", 0.0, false);

  static const double CAMERA_ZOOM = 15;
  static const double CAMERA_TILT = 80;
  static const double CAMERA_BEARING = 30;

  LatLng SOURCE_LOCATION = LatLng(-25.754618, 28.233255);
  LatLng DEST_LOCATION = LatLng(-25.788660, 28.242833);


  Completer<GoogleMapController> _controller = Completer();
  Set<Marker> _markers = Set<Marker>();
  Map<PolylineId, Polyline> polylines = {};
  List<LatLng> polylineCoordinates = [];
  late PolylinePoints polylinePoints;
  Location _location=Location();

  String googleAPIKey = "AIzaSyBJOzTh0UALMl2qEb-c4YKU62_1s_sQHtU";
// for my custom marker pins
  late BitmapDescriptor sourceIcon;
  late BitmapDescriptor destinationIcon;
  late LocationData currentLocation=LocationData.fromMap({
    "latitude": DEST_LOCATION.latitude,
    "longitude": DEST_LOCATION.longitude
  });

// a reference to the destination location
  late LocationData destinationLocation=LocationData.fromMap({
    "latitude": DEST_LOCATION.latitude,
    "longitude": DEST_LOCATION.longitude
  });
// wrapper around the location API
  late Location location;

  @override
  void initState() {
    super.initState();

    // create an instance of Location
    //location = new Location();
    polylinePoints = PolylinePoints();
    // location.onLocationChanged().listen((LocationData cLoc) {
    //   currentLocation = cLoc;
    //   updatePinOnMap();
    // });
    // set custom marker pins
    setSourceAndDestinationIcons();
    // set the initial location
    setInitialLocation();
  }
  @override
  void dispose(){
    _timer?.cancel();
    _timer=null;
    super.dispose();
  }

  Widget _popUpSuccessfulEndLocation(BuildContext context){
    return new AlertDialog(
      title: const Text('Looks like you are at your destination Location'),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Update status of delivery on work page",
              textAlign: TextAlign.center
          ),
        ],
      ),
      actionsPadding: EdgeInsets.only(right:30),
      actions: <Widget>[
        new FlatButton(
          onPressed: () async {
            Navigator.pop(context, false);
          },
          child: Icon(
            Icons.cancel_rounded,
            color: Colors.red,
          ),
        ),
        new FlatButton(
          onPressed: (){
            MyNavigator.goToDriverHomePage(context);
          },
          child: const Text('Done'),
        ),

      ],
    );
  }

  void setSourceAndDestinationIcons() async {
    sourceIcon = await BitmapDescriptor.fromAssetImage(
        ImageConfiguration(devicePixelRatio: 2.5,size: Size(5,5)),
        'assets/driver.png');

    destinationIcon = await BitmapDescriptor.fromAssetImage(
        ImageConfiguration(devicePixelRatio: 2.5),
        'assets/pin.png');
  }

  void setInitialLocation() async {
    print("_____________________________2");
    // set the initial location by pulling the user's
    // current location from the location's getLocation()
    //currentLocation = await location.getLocation();
    currentLocation=LocationData.fromMap({
      "latitude": SOURCE_LOCATION.latitude,
      "longitude": SOURCE_LOCATION.longitude

    });
    // hard-coded destination for this example
    destinationLocation = LocationData.fromMap({
      "latitude": DEST_LOCATION.latitude,
      "longitude": DEST_LOCATION.longitude
    });
  }

  void setPolylines() async {
    PointLatLng origin = PointLatLng(
        currentLocation.latitude, currentLocation.longitude);
    PointLatLng desination = PointLatLng(
        destinationLocation.latitude, destinationLocation.longitude);
    PolylineResult result = await polylinePoints.getRouteBetweenCoordinates(
        googleAPIKey,
        origin,
        desination,
        travelMode: TravelMode.driving,
        // wayPoints: [PolylineWayPoint(location: "Woolworthes, Hillcrest Boulevard")]
    );
    if (result.points.isNotEmpty) {
      result.points.forEach((PointLatLng point) {
        polylineCoordinates.add(LatLng(point.latitude, point.longitude));
      });
      _addPolyLine();
    }
  }

  _addPolyLine() {
    print("_____________________________4");
    PolylineId id = PolylineId("poly");
    Polyline polyline = Polyline(
        polylineId: id, color: Colors.red, points: polylineCoordinates);
    polylines[id] = polyline;
    setState(() {});
    simulateMoving();
  }

  void showPinsOnMap() {
    print("_____________________________5");
    // get a LatLng for the source location
    // from the LocationData currentLocation object
    var pinPosition = LatLng(currentLocation.latitude,
        currentLocation.longitude);
    // get a LatLng out of the LocationData object
    var destPosition = LatLng(destinationLocation.latitude,
        destinationLocation.longitude);
    // add the initial source location pin
    _markers.add(Marker(
        markerId: MarkerId('sourcePin'),
        position: pinPosition,
        icon: sourceIcon
    ));
    // destination pin
    _markers.add(Marker(
        markerId: MarkerId('destPin'),
        position: destPosition,
        icon: destinationIcon
    ));
    // set the route lines on the map from source to destination
    // for more info follow this tutorial
    setPolylines();
  }

  void updatePinOnMap() async {
    print("_____________________________6");
    // create a new CameraPosition instance
    // every time the location changes, so the camera
    // follows the pin as it moves with an animation
    CameraPosition cPosition = CameraPosition(
      zoom: CAMERA_ZOOM,
      tilt: CAMERA_TILT,
      bearing: CAMERA_BEARING,
      target: LatLng(currentLocation.latitude,
          currentLocation.longitude),
    );
    final GoogleMapController controller = await _controller.future;
    controller.animateCamera(CameraUpdate.newCameraPosition(cPosition));
    // do this inside the setState() so Flutter gets notified
    // that a widget update is due
    setState(() {
      // updated position
      var pinPosition = LatLng(currentLocation.latitude,
          currentLocation.longitude);
      // the trick is to remove the marker (by id)
      // and add it again at the updated location
      _markers.removeWhere(
              (m) => m.markerId.value == "sourcePin");
      _markers.add(Marker(
          markerId: MarkerId("sourcePin"),
          position: pinPosition, // updated position
          icon: sourceIcon
      ));
    });
  }
  Timer? _timer;
  void simulateMoving() {
    print("_____________________________8");
    int i=0;
    int j=0;
    _timer=Timer.periodic(Duration(milliseconds: 350), (Timer t) {
      if(mounted){
        setState((){
          if(i<polylineCoordinates.length){
            currentLocation=LocationData.fromMap({
              "latitude": polylineCoordinates[i].latitude,
              "longitude": polylineCoordinates[i].longitude,
            });
            updatePinOnMap();
            i+=3;
            j++;
            if(j==3){
              _userService.updateDriverLocation(currentLocation.latitude, currentLocation.longitude, context).then((value) =>
                  {
                    if(value==true){
                      if(mounted){
                        setState(() {
                          _updatingOnline = true;
                        })
                      }
                    }else
                      {
                        if(mounted){
                          setState(() {
                            _updatingOnline = false;
                          })
                        }
                      }
                  }
              );
              j=0;
            }
          }else {
            t.cancel();
            showDialog(
              context: context,
              builder: (BuildContext context) =>
                  _popUpSuccessfulEndLocation(
                      context),
            );
          }

        });}},
    );
  }

  Widget buildMap(BuildContext context) {
    print("_____________________________7");
    CameraPosition initialCameraPosition = CameraPosition(
        zoom: CAMERA_ZOOM,
        tilt: CAMERA_TILT,
        bearing: CAMERA_BEARING,
        target: SOURCE_LOCATION
    );

    if (currentLocation != null) {
      initialCameraPosition = CameraPosition(
          target: LatLng(currentLocation.latitude,
              currentLocation.longitude),
          zoom: CAMERA_ZOOM,
          tilt: CAMERA_TILT,
          bearing: CAMERA_BEARING
      );
    }
    return SizedBox(
        height: MediaQuery.of(context).size.height-130,
        width: MediaQuery.of(context).size.width,
        child: GoogleMap(
            myLocationEnabled: true,
            compassEnabled: true,
            tiltGesturesEnabled: false,
            markers: _markers,
            polylines: Set<Polyline>.of(polylines.values),
            mapType: MapType.normal,
            initialCameraPosition: initialCameraPosition,
            onMapCreated: (GoogleMapController controller) {
              _controller.complete(controller);
              print(_delivery.deliveryStatus);
              if(_delivery.deliveryStatus=="CollectingFromStore" ){
                showPinsOnMap();
              } else if(_delivery.deliveryStatus=="DeliveringToCustomer"){
                showPinsOnMap();
              }

            })
    );

  }
  @override
  Widget build(BuildContext context) {
    print("_____________________________9");
    _delivery=Provider.of<DeliveryProvider>(context,listen: false).delivery;
    _delivery.deliveryStatus="DeliveringToCustomer";
    if(_delivery.deliveryStatus=="CollectingFromStore"){
      DEST_LOCATION=LatLng(_delivery.pickUpLocation.latitude, _delivery.pickUpLocation.longitude);
    }else if(_delivery.deliveryStatus=="DeliveringToCustomer"){
      print(_delivery.pickUpLocation.latitude);
      print(_delivery.pickUpLocation.longitude);
      SOURCE_LOCATION=LatLng(_delivery.pickUpLocation.latitude, _delivery.pickUpLocation.longitude);
      print("???????????????????????????????????????????");
      print(SOURCE_LOCATION.latitude);
      print(SOURCE_LOCATION.longitude);
      DEST_LOCATION=LatLng(_delivery.dropOffLocation.latitude, _delivery.dropOffLocation.longitude);
    }
    return Scaffold(
      body: Column(
        children: [
          Stack(
            children: [
              buildMap(context),

            ],
          ),
          Column(
            children: [
              Text("Status (updating on customer app): \n"+_updatingOnline.toString()),
              Text("Currently driving to: \n"+_delivery.deliveryStatus=="CollectingFromStore"?_delivery.pickUpLocation.address:_delivery.deliveryStatus=="DeliveringToCustomer"?_delivery.dropOffLocation.address:""),
            ],
          )
        ],
      ),
    );
  }


}