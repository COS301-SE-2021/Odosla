import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
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

  void setStatus(Future<String> s) {
    setState(() {
      _status = s;
    });
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
                  {_timer.cancel(), debugPrint("CANCELLED")}
              });
    }

    super.initState();
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
                  buildProgressBar(context),
                  SizedBox(height: 10),
                  buildEmployeeInfo(context),
                  SizedBox(height: 10),
                  buildMap(context),
                  SizedBox(height: 70),
                ])),
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
                            return Text(s,
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
        myLocationButtonEnabled: false,
        zoomControlsEnabled: false,
        initialCameraPosition: _initialCameraPosition,
      ),
    );
  }

  Widget buildEmployeeInfo(BuildContext context) {
    if (Provider.of<DriverProvider>(context).allocated) {
      return Container(
        height: 100,
        child: Text(Provider.of<DriverProvider>(context).name +
            "\n" +
            Provider.of<DriverProvider>(context).rating.toString() +
            "\n" +
            Provider.of<DriverProvider>(context).lat.toString()),
      );
    } else
      return Container(
          height: 100, child: Text("A driver will be assigned shortly"));
  }
}
