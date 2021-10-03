import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/GeoPoint.dart';
import 'package:flutter_employee_app/pages/driver/driver_main_screen.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_polyline_points/flutter_polyline_points.dart';
import 'package:get_it/get_it.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:location/location.dart';
import 'package:percent_indicator/percent_indicator.dart';
import 'package:provider/provider.dart';

class DriverMapScreen extends StatefulWidget {
  @override
  _DriverMapScreenState createState() => _DriverMapScreenState();
}

class _DriverMapScreenState extends State<DriverMapScreen> {
  UserService _userService = GetIt.I.get();
  Delivery _delivery = new Delivery(
      "",
      new GeoPoint(0.0, 0.0, ""),
      new GeoPoint(0.0, 0.0, ""),
      "",
      "",
      "",
      "",
      "",
      0.0,
      false,
      new GeoPoint(0.0, 0.0, ""),
      new GeoPoint(0.0, 0.0, ""));
  bool _isOne = false;
  bool _isTwo = false;
  bool _isThree = false;
  bool _completedOne = false;
  bool _completedTwo = false;
  bool _completedThree = false;
  GeoPoint _drivingTo = GeoPoint(0, 0, "");
  bool _done = false;

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
  Location _location = Location();

  String googleAPIKey = "AIzaSyBJOzTh0UALMl2qEb-c4YKU62_1s_sQHtU";

// for my custom marker pins
  late BitmapDescriptor sourceIcon;
  late BitmapDescriptor destinationIcon;
  late LocationData currentLocation = LocationData.fromMap({
    "latitude": DEST_LOCATION.latitude,
    "longitude": DEST_LOCATION.longitude
  });

// a reference to the destination location
  late LocationData destinationLocation = LocationData.fromMap({
    "latitude": DEST_LOCATION.latitude,
    "longitude": DEST_LOCATION.longitude
  });

// wrapper around the location API
  late Location location;

  @override
  void initState() {
    super.initState();
    _done =false;
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
  void dispose() {
    _timer?.cancel();
    _timer = null;
    super.dispose();
  }

  Widget _popUpSuccessfulEndLocation(BuildContext context) {
    return new AlertDialog(
      title: const Text('Looks like you are at your destination Location'),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Update status of delivery on work page",
              textAlign: TextAlign.center),
        ],
      ),
      actionsPadding: EdgeInsets.only(right: 30),
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
          onPressed: () {
            MyNavigator.goToDriverHomePage(context);
          },
          child: const Text('Done'),
        ),
      ],
    );
  }

  void setSourceAndDestinationIcons() async {
    sourceIcon = await BitmapDescriptor.fromAssetImage(
        ImageConfiguration(devicePixelRatio: 2.5, size: Size(5, 5)),
        'assets/driver.png');

    destinationIcon = await BitmapDescriptor.fromAssetImage(
        ImageConfiguration(devicePixelRatio: 2.5), 'assets/pin.png');
  }

  void setInitialLocation() async {
    // set the initial location by pulling the user's
    // current location from the location's getLocation()
    //currentLocation = await location.getLocation();
    currentLocation = LocationData.fromMap({
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
    PointLatLng origin =
        PointLatLng(currentLocation.latitude, currentLocation.longitude);
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
    PolylineId id = PolylineId("poly");
    Polyline polyline = Polyline(
        polylineId: id, color: Colors.red, points: polylineCoordinates);
    polylines[id] = polyline;
    setState(() {});
    simulateMoving();
  }

  void showPinsOnMap() {
    // get a LatLng for the source location
    // from the LocationData currentLocation object
    var pinPosition =
        LatLng(currentLocation.latitude, currentLocation.longitude);
    // get a LatLng out of the LocationData object
    var destPosition =
        LatLng(destinationLocation.latitude, destinationLocation.longitude);
    // add the initial source location pin
    _markers.add(Marker(
        markerId: MarkerId('sourcePin'),
        position: pinPosition,
        icon: sourceIcon));
    // destination pin
    _markers.add(Marker(
        markerId: MarkerId('destPin'),
        position: destPosition,
        icon: destinationIcon));
    // set the route lines on the map from source to destination
    // for more info follow this tutorial
    setPolylines();
  }

  void updatePinOnMap() async {
    // create a new CameraPosition instance
    // every time the location changes, so the camera
    // follows the pin as it moves with an animation
    CameraPosition cPosition = CameraPosition(
      zoom: CAMERA_ZOOM,
      tilt: CAMERA_TILT,
      bearing: CAMERA_BEARING,
      target: LatLng(currentLocation.latitude, currentLocation.longitude),
    );
    final GoogleMapController controller = await _controller.future;
    controller.animateCamera(CameraUpdate.newCameraPosition(cPosition));
    // do this inside the setState() so Flutter gets notified
    // that a widget update is due
    setState(() {
      // updated position
      var pinPosition =
          LatLng(currentLocation.latitude, currentLocation.longitude);
      // the trick is to remove the marker (by id)
      // and add it again at the updated location
      _markers.removeWhere((m) => m.markerId.value == "sourcePin");
      _markers.add(Marker(
          markerId: MarkerId("sourcePin"),
          position: pinPosition, // updated position
          icon: sourceIcon));
    });
  }

  Timer? _timer;

  void simulateMoving() {
    int i = 0;
    int j = 0;
    _timer = Timer.periodic(
      Duration(seconds: 1),
      (Timer t) {
        if (mounted) {
          setState(() {
            if (i < polylineCoordinates.length) {
              currentLocation = LocationData.fromMap({
                "latitude": polylineCoordinates[i].latitude,
                "longitude": polylineCoordinates[i].longitude,
              });
              updatePinOnMap();
              i += 3;
              j++;
              if (j == 1) {
                _userService.updateDriverLocation(currentLocation.latitude,
                    currentLocation.longitude, context).then((value) => {});
                j = 0;
              }
            } else {
              t.cancel();
              if (_delivery.deliveryStatus == "DeliveringToCustomer" ||
                  (_isOne) ||
                  (_isTwo && _completedOne) ||
                  (_isThree && _completedTwo))
                showDialog(
                  context: context,
                  builder: (BuildContext context) =>
                      _popUpSuccessfulEndLocation(context),
                );
            }
          });
        }
      },
    );
  }

  Widget buildMap(BuildContext context) {
    CameraPosition initialCameraPosition = CameraPosition(
        zoom: CAMERA_ZOOM,
        tilt: CAMERA_TILT,
        bearing: CAMERA_BEARING,
        target: SOURCE_LOCATION);

    print("IN BUILD MAP: SOURCE LOCATION:"+SOURCE_LOCATION.latitude.toString()+" "+SOURCE_LOCATION.longitude.toString());

    if (currentLocation != null) {
      initialCameraPosition = CameraPosition(
          target: LatLng(currentLocation.latitude, currentLocation.longitude),
          zoom: CAMERA_ZOOM,
          tilt: CAMERA_TILT,
          bearing: CAMERA_BEARING);
    }
    return Padding(
      padding: const EdgeInsets.fromLTRB(9, 25, 9, 9),
      child: SizedBox(
          height: MediaQuery.of(context).size.height - 90,
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
                if (_delivery.deliveryStatus == "CollectingFromStore") {
                  showPinsOnMap();
                } else if (_delivery.deliveryStatus == "DeliveringToCustomer") {
                  showPinsOnMap();
                }
              })),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (_done == false) {
      print("___!!!!!!__");
      print("IN DONE");
      Provider.of<DeliveryProvider>(context, listen: false).printer();
      setState(() {

        _delivery =
            Provider.of<DeliveryProvider>(context, listen: false).delivery;
        _isOne =
            Provider.of<DeliveryProvider>(context, listen: false).isOneDelivery;
        _isTwo = Provider.of<DeliveryProvider>(context, listen: false)
            .isTwoDeliveries;
        _isThree = Provider.of<DeliveryProvider>(context, listen: false)
            .isThreeDeliveries;

        _completedOne =
            Provider.of<DeliveryProvider>(context, listen: false).completedOne;
        _completedTwo =
            Provider.of<DeliveryProvider>(context, listen: false).completedTwo;

        if (_delivery.deliveryStatus == "CollectingFromStore") {
          GeoPoint pickUp = new GeoPoint(0, 0, "");
          if (_completedOne == false) {
            pickUp = _delivery.pickUpLocationOne;
            print("___1__");
          } else if (_isOne == false && _completedTwo == false) {
            SOURCE_LOCATION = LatLng(_delivery.pickUpLocationOne.latitude,
                _delivery.pickUpLocationOne.longitude);
            pickUp = _delivery.pickUpLocationTwo;
            print("___2__");
          } else if (_isTwo == false && _isOne && _completedThree == false) {
            SOURCE_LOCATION = LatLng(_delivery.pickUpLocationTwo.latitude,
                _delivery.pickUpLocationTwo.longitude);
            pickUp = _delivery.pickUpLocationThree;
          }
          print("___3__");
          _drivingTo = pickUp;
          DEST_LOCATION = LatLng(pickUp.latitude, pickUp.longitude);
        } else if (_delivery.deliveryStatus == "DeliveringToCustomer") {
          GeoPoint pickUp = new GeoPoint(0, 0, "");
          if (_isOne) {
            pickUp = _delivery.pickUpLocationOne;
          } else if (_isTwo) {
            pickUp = _delivery.pickUpLocationTwo;
          } else if (_isThree) {
            pickUp = _delivery.pickUpLocationThree;
          }
          SOURCE_LOCATION = LatLng(pickUp.latitude, pickUp.longitude);
          DEST_LOCATION = LatLng(_delivery.dropOffLocation.latitude,
              _delivery.dropOffLocation.longitude);
          _drivingTo = _delivery.dropOffLocation;
        }
      });
      setInitialLocation();
      setState(() {
        print("___4__");
        _done = true;
      });
    }

    return Scaffold(
      body: Stack(
        children: [
          buildMap(context),
          Column(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              SizedBox(
                height: 19,
              ),
              Container(
                alignment: Alignment.bottomCenter,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    _delivery.deliveryStatus == "CollectingFromStore" ||
                            _delivery.deliveryStatus == "DeliveringToCustomer"
                        ? Card(
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(20.0),
                            ),
                            margin: EdgeInsets.symmetric(
                                horizontal: 10, vertical: 20),
                            clipBehavior: Clip.antiAlias,
                            color: Theme.of(context).backgroundColor,
                            shadowColor: Colors.grey,
                            elevation: 5.0,
                            child: Padding(
                                padding: const EdgeInsets.symmetric(
                                    horizontal: 1.0, vertical: 10.0),
                                child: Row(
                                  children: <Widget>[
                                    Container(
                                        height: _delivery.deliveryStatus ==
                                                "CollectingFromStore"
                                            ? MediaQuery.of(context)
                                                    .size
                                                    .height *
                                                0.14
                                            : MediaQuery.of(context)
                                                    .size
                                                    .height *
                                                0.05,
                                        width:
                                            MediaQuery.of(context).size.width *
                                                0.9,
                                        alignment: Alignment.center,
                                        child: Column(
                                          mainAxisAlignment:
                                              MainAxisAlignment.spaceEvenly,
                                          children: <Widget>[
                                            _delivery.deliveryStatus ==
                                                    "CollectingFromStore"
                                                ? Padding(
                                                    padding:
                                                        EdgeInsets.symmetric(
                                                            horizontal: 15.0),
                                                    child:
                                                        new LinearPercentIndicator(
                                                      animation: true,
                                                      lineHeight: 20.0,
                                                      animationDuration: 2000,
                                                      percent: (_completedThree
                                                              ? 3
                                                              : _completedTwo
                                                                  ? 2
                                                                  : _completedOne
                                                                      ? 1
                                                                      : 0) /
                                                          (_isThree
                                                              ? 3
                                                              : _isTwo
                                                                  ? 2
                                                                  : 1),
                                                      center: Text((_completedThree
                                                              ? "3"
                                                              : _completedTwo
                                                                  ? "2"
                                                                  : _completedOne
                                                                      ? "1"
                                                                      : "0") +
                                                          (_isThree
                                                              ? "/3"
                                                              : _isTwo
                                                                  ? "/2"
                                                                  : "/1")),
                                                      linearStrokeCap:
                                                          LinearStrokeCap
                                                              .roundAll,
                                                      progressColor:
                                                          Colors.greenAccent,
                                                    ),
                                                  )
                                                : Container(),
                                            Text(
                                              "Currently driving to:",
                                              style: kTitleTextStyle.copyWith(
                                                fontWeight: FontWeight.w500,
                                              ),
                                              overflow: TextOverflow.ellipsis,
                                            ),
                                            Expanded(
                                              child: Text(
                                                _drivingTo.address,
                                                style: kTitleTextStyle.copyWith(
                                                  fontWeight: FontWeight.w300,
                                                ),
                                                overflow: TextOverflow.ellipsis,
                                              ),
                                            ),
                                            _delivery.deliveryStatus ==
                                                    "CollectingFromStore"
                                                ? RaisedButton(
                                                    onPressed: () {
                                                      bool completed = Provider
                                                              .of<DeliveryProvider>(
                                                                  context,
                                                                  listen: false)
                                                          .collected();
                                                      if (completed == true) {
                                                        setState(() {
                                                          _done = false;
                                                          if(_isTwo){
                                                            print("WE GET HERE XXX");
                                                            _completedTwo=true;
                                                          }
                                                          if(_isThree){
                                                            _completedThree=true;
                                                          }
                                                        });
                                                        showDialog(
                                                          context: context,
                                                          builder: (BuildContext
                                                                  context) =>
                                                              _popUpSuccessfulEndLocation(
                                                                  context),
                                                        );
                                                      } else {
                                                        setState(() {
                                                          _done = false;
                                                        });
                                                        Navigator.of(context).pushReplacement(MaterialPageRoute(
                                                            builder: (BuildContext context) =>
                                                                DriverHomeScreen(0)));
                                                      }
                                                    },
                                                    child: Text(
                                                      "Collected",
                                                      style: TextStyle(
                                                          color: Colors
                                                              .deepOrangeAccent,
                                                          fontWeight:
                                                              FontWeight.w900),
                                                    ),
                                                    color: Theme.of(context)
                                                        .backgroundColor,
                                                    splashColor: Colors.grey,
                                                    elevation: 5.0,
                                                    shape:
                                                        RoundedRectangleBorder(
                                                      borderRadius:
                                                          BorderRadius.circular(
                                                              10.0),
                                                    ),
                                                    // color: Color(0xFFFA8940),
                                                  )
                                                : Container(),
                                          ],
                                        )),
                                    // Container(
                                    //     height: MediaQuery.of(context).size.height*0.15,
                                    //     width: MediaQuery.of(context).size.width*0.42,
                                    //     child:Column(
                                    //       mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    //       children: <Widget>[
                                    //         Text(
                                    //           "DRIVER",
                                    //           style: kTitleTextStyle.copyWith(
                                    //             fontWeight: FontWeight.w500,
                                    //             fontSize: kSpacingUnit.w*2,
                                    //           ),
                                    //         ),
                                    //         Container(
                                    //           height: MediaQuery.of(context).size.height*0.08,
                                    //           child:Image(
                                    //             fit: BoxFit.fill,
                                    //             image: _onShift?AssetImage('assets/gifs/deliveryTruck.gif'):AssetImage('assets/delivery/staticDeliveryTruck.png'),
                                    //           ),
                                    //         ),
                                    //         SizedBox(height: 5,),
                                    //         Text(
                                    //           _onShift?"• On Shift":"• Not on shift",
                                    //           style: kTitleTextStyle.copyWith(
                                    //             fontWeight: FontWeight.w500,
                                    //             fontSize: kSpacingUnit.w*1.5,
                                    //           ),
                                    //           textAlign: TextAlign.center,
                                    //         ),
                                    //       ],
                                    //     )
                                    // ),
                                  ],
                                )),
                          )
                        : Container(),
                  ],
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
