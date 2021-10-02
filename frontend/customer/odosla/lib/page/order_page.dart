import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_phone_direct_caller/flutter_phone_direct_caller.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:liquid_progress_indicator_ns/liquid_progress_indicator.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/provider/driver_provider.dart';
import 'package:odosla/provider/status_provider.dart';
import 'package:odosla/services/api_service.dart';
import 'package:provider/provider.dart';

class OrderPage extends StatefulWidget {
  final String orderID;
  const OrderPage(this.orderID
      //Key key,
      ); // : super(key: key);

  @override
  _OrderPage createState() => _OrderPage();
}

class _OrderPage extends State<OrderPage> {
  _OrderPage() {}
  ApiService api = ApiService();

  late Future<String> _status;
  late Timer _timer = Timer(Duration.zero, () {});
  late Timer _timerDriver = Timer(Duration.zero, () {});

  Completer<GoogleMapController> _controller = Completer();

  late List<Marker> markers = [];

  late BitmapDescriptor sourceIcon;
  late BitmapDescriptor destinationIcon;

  void setStatus(Future<String> s) {
    setState(() {
      _status = s;
    });
  }

  void setSourceAndDestinationIcons() async {
    sourceIcon = await BitmapDescriptor.fromAssetImage(
        ImageConfiguration(devicePixelRatio: 2.5, size: Size(5, 5)),
        'assets/shop/me.png');

    destinationIcon = await BitmapDescriptor.fromAssetImage(
        ImageConfiguration(devicePixelRatio: 2.5), 'assets/map/shop.png');
  }

  @override
  void initState() {
    if (Provider.of<CartProvider>(context, listen: false).activeOrder)
      setStatus(api.getStatus(context, widget.orderID));
    //debugPrint("_A_SD_ASD: " + widget.orderID);
    if (Provider.of<CartProvider>(context, listen: false).activeOrder) {
      setStatus(api.getStatus(context, widget.orderID));
      _timer = new Timer.periodic(
          Duration(milliseconds: 950),
          (_) => {
                setStatus(api.getStatus(context, widget.orderID)),
                if (!Provider.of<CartProvider>(context, listen: false)
                    .activeOrder)
                  {_timer.cancel(), debugPrint("CANCELLED")},
//.

                if (Provider.of<DriverProvider>(context, listen: false)
                        .allocated &&
                    !Provider.of<DriverProvider>(context, listen: false)
                        .tracking)
                  {
                    Provider.of<DriverProvider>(context, listen: false)
                        .tracking = true,
                    api.updateDriverLocation(
                        context,
                        Provider.of<DriverProvider>(context, listen: false)
                            .deliveryID),
                    updatePinOnMap(),
                    _timerDriver = new Timer.periodic(
                        Duration(milliseconds: 800),
                        (_) => {
                              debugPrint("tick"),
                              updatePinOnMap(),
                              api.updateDriverLocation(
                                  context,
                                  Provider.of<DriverProvider>(context,
                                          listen: false)
                                      .deliveryID),
                              updatePinOnMap(),
                              if (!Provider.of<CartProvider>(context,
                                      listen: false)
                                  .activeOrder)
                                {
                                  _timerDriver.cancel(),
                                  Provider.of<DriverProvider>(context,
                                          listen: false)
                                      .tracking = false,
                                  debugPrint("CANCELLED")
                                },
                            }),
                  }
              });
    }

    markers = [
      Marker(
          markerId: MarkerId("you"),
          position: LatLng(-25.763428, 28.260879),
          icon: BitmapDescriptor.defaultMarker,
          infoWindow: InfoWindow(title: "me")),
      Marker(
          markerId: MarkerId("shop"),
          position: LatLng(
              Provider.of<CartProvider>(context, listen: false)
                  .activeStoreLocation['lat']!,
              Provider.of<CartProvider>(context, listen: false)
                  .activeStoreLocation['long']!),
          icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueCyan),
          infoWindow: InfoWindow(title: "store")),
    ];

    super.initState();
  }

  // void driver() {
  //   setState(() {
  //     if (markers.length > 2) markers.removeLast();
  //     markers.add(Marker(
  //       markerId: MarkerId("driver"),
  //       position: LatLng(
  //           Provider.of<DriverProvider>(context, listen: false).lat,
  //           Provider.of<DriverProvider>(context, listen: false).lat),
  //       icon: BitmapDescriptor.defaultMarker,
  //       infoWindow: InfoWindow(
  //           title: Provider.of<DriverProvider>(context, listen: false).name),
  //     ));
  //   });
  //   debugPrint(markers.length.toString());
  // }

  void updatePinOnMap() async {
    // create a new CameraPosition instance
    // every time the location changes, so the camera
    // follows the pin as it moves with an animation
    // CameraPosition cPosition = CameraPosition(
    //   zoom: CAMERA_ZOOM,
    //   tilt: CAMERA_TILT,
    //   bearing: CAMERA_BEARING,
    //   target: LatLng(currentLocation.latitude,
    //       currentLocation.longitude),
    // );
    final GoogleMapController controller = await _controller.future;
    // controller.animateCamera(CameraUpdate.newCameraPosition(cPosition));
    // do this inside the setState() so Flutter gets notified
    // that a widget update is due
    setState(() {
      // updated position
      var pinPosition = LatLng(
          Provider.of<DriverProvider>(context, listen: false).lat,
          Provider.of<DriverProvider>(context, listen: false).long);
      // the trick is to remove the marker (by id)
      // and add it again at the updated location
      markers.removeWhere((m) => m.markerId.value == "driver");
      markers.add(Marker(
          markerId: MarkerId("driver"),
          position: pinPosition, // updated position
          icon:
              BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueViolet),
          infoWindow: InfoWindow(title: "driver")));
    });
  }

  @override
  void dispose() {
    _timer.cancel();
    super.dispose();
  }

  static const _initialCameraPosition =
      CameraPosition(target: LatLng(-25.76, 28.24), zoom: 12.5);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Stack(
          children: [
            Padding(
              padding: const EdgeInsets.all(12),
              child: buildPage(context),
            ),
            Positioned(
                top: 5,
                left: 5,
                child: IconButton(
                  icon: Icon(Icons.arrow_back_ios, color: Colors.black),
                  onPressed: () => Navigator.pop(context),
                ))
          ],
        ),
      ),
    );
  }

  Widget buildPage(BuildContext context) {
    if (Provider.of<CartProvider>(context).activeOrder)
      return Container(
          height: MediaQuery.of(context).size.height,
          child: Column(
              // mainAxisAlignment: MainAxisAlignment.center,
              // crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Center(
                    child: Text(
                  'My Order',
                  style: TextStyle(fontSize: 27, fontWeight: FontWeight.bold),
                )),
                //Expanded(
                Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      buildProgressBar(context),
                      SizedBox(height: 10),
                      buildEmployeeInfo(context),
                      buildMap(context),
                      SizedBox(height: 70),
                    ]),
              ]));
    else
      return Container(
          child: Column(
              // mainAxisAlignment: MainAxisAlignment.center,
              // crossAxisAlignment: CrossAxisAlignment.center,
              children: [
            Center(
                child: Text(
              'My Order',
              style: TextStyle(fontSize: 27, fontWeight: FontWeight.bold),
            )),
            Expanded(
                child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                  Icon(
                    Icons.remove_shopping_cart_sharp,
                    size: 120.0,
                  ),
                  SizedBox(height: 30),
                  Text(
                    'No active order',
                    style: TextStyle(fontSize: 27, fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 70),
                ])),
          ]));
  }

  Widget buildProgressBar(BuildContext context) => Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Padding(
                padding: EdgeInsets.symmetric(horizontal: 20),
                child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      // Text(Provider.of<StatusProvider>(context).status,
                      //     style: TextStyle(
                      //         fontSize: 27, fontWeight: FontWeight.normal)
                      //                               ),
                      FutureBuilder<String>(
                        future: _status,
                        builder: (context, snapshot) {
                          if (!snapshot.hasError) {
                            String s =
                                Provider.of<StatusProvider>(context).status;
                            return Text(s.toString(),
                                style: TextStyle(
                                    fontSize: 27,
                                    fontWeight: FontWeight.normal));
                          } else {
                            debugPrint("SNAP: " + snapshot.error.toString());
                            return Text("Pending (?)");
                          }
                        },
                      ),

                      // Text("eta: 43m",
                      //     style: TextStyle(
                      //         fontSize: 23, fontWeight: FontWeight.normal)),
                    ])),
            SizedBox(
              height: 10,
            ),
            Container(
              height: 50,
              child: LiquidLinearProgressIndicator(
                value: Provider.of<StatusProvider>(context).progress,
                borderColor: Colors.white,
                borderRadius: 25,
                borderWidth: 1,
                valueColor: AlwaysStoppedAnimation(Colors.deepOrange),
                backgroundColor: Colors.grey.shade300,
              ),
            ),
          ]);

  Widget buildMap(BuildContext context) {
    return SizedBox(
      height: 280,
      width: MediaQuery.of(context).size.width,
      child: GoogleMap(
        onMapCreated: _onMapCreated,
        myLocationButtonEnabled: false,
        zoomControlsEnabled: true,
        markers: markers.toSet(),
        initialCameraPosition: _initialCameraPosition,
      ),
    );
  }

  void _onMapCreated(GoogleMapController control) {
    _controller.complete(control);
    // (_controller as GoogleMapController).showMarkerInfoWindow(MarkerId("you"));
    // _controller.showMarkerInfoWindow(MarkerId("shop"));
    // _controller.showMarkerInfoWindow(MarkerId("driver"));
    //showPinsOnMap();
  }

  Widget buildEmployeeInfo(BuildContext context) {
    if (Provider.of<DriverProvider>(context).allocated) {
      return Container(
        padding: EdgeInsets.symmetric(horizontal: 20),
        height: 140,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("Driver: ",
                    style:
                        TextStyle(fontSize: 17, fontWeight: FontWeight.bold)),
                Text(Provider.of<DriverProvider>(context).name,
                    style: TextStyle(fontSize: 17))
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("Deliveries completed: ",
                    style:
                        TextStyle(fontSize: 17, fontWeight: FontWeight.bold)),
                Text(Provider.of<DriverProvider>(context).trips.toString(),
                    style: TextStyle(
                      fontSize: 17,
                    ))
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("Rating: ",
                    style:
                        TextStyle(fontSize: 17, fontWeight: FontWeight.bold)),
                Text(
                    Provider.of<DriverProvider>(context).rating.toString() +
                        "/5",
                    style: TextStyle(fontSize: 17))
              ],
            ),
            TextButton(
                onPressed: () => {
                      _callNumber(
                          Provider.of<DriverProvider>(context, listen: false)
                              .contact)
                    },
                child: Text("Call Driver",
                    style: TextStyle(fontSize: 18, color: Colors.blueGrey)))
          ],
        ),
      );
    } else
      return Container(
          height: 100,
          child: Text("A driver will be assigned shortly",
              style: TextStyle(fontSize: 16)));
  }

  _callNumber(String num) async {
    String number = num; //set the number here
    await FlutterPhoneDirectCaller.callNumber(number);
  }
}
