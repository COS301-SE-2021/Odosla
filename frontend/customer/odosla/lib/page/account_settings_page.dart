import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:odosla/page/wallet_page.dart';
import 'package:odosla/provider/wallet_provider.dart';
import 'package:provider/provider.dart';

class AccountSettingsPage extends StatefulWidget {
  const AccountSettingsPage(
      //Key key,
      ); // : super(key: key);

  @override
  _AccountSettingsPage createState() => _AccountSettingsPage();
}

class _AccountSettingsPage extends State<AccountSettingsPage> {
  _AccountSettingsPage();

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
    return Container(
        child: Column(
            // mainAxisAlignment: MainAxisAlignment.center,
            // crossAxisAlignment: CrossAxisAlignment.center,
            children: [
          Center(
              child: Text(
            'Account Settings',
            style: TextStyle(fontSize: 27, fontWeight: FontWeight.bold),
          )),
          buildHeader(context),
          buildSettings(context),
          SizedBox(height: 100),
        ]));
  }

  Widget buildHeader(BuildContext context) {
    return Expanded(
        child: Column(
      children: [
        SizedBox(
          height: 25,
        ),
        Icon(
          Icons.person_sharp,
          size: 200,
          color: Colors.deepOrange,
        ),
        SizedBox(height: 5),
        Text("Japhet T", style: TextStyle(fontSize: 28)),
        Text(
          "japh.tang@goatmail.com",
          style: TextStyle(fontSize: 16, color: Colors.black87),
        ),
        SizedBox(
          height: 75,
        )
      ],
    ));
  }

  Widget buildSettings(BuildContext context) {
    return Column(
      children: [
        Text("Payment Method",
            style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
        SizedBox(height: 3),
        Provider.of<WalletProvider>(context).present
            ? Text(
                "Card ending in: " + Provider.of<WalletProvider>(context).card,
                style: TextStyle(fontSize: 19))
            : Text("Not set", style: TextStyle(fontSize: 19)),
        SizedBox(height: 12),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            SizedBox(
              width: 10,
            ),
            SizedBox(
              width: 10,
            ),
            Container(
                width: 140,
                height: 34,
                child: ElevatedButton(
                    style: ButtonStyle(
                        backgroundColor:
                            MaterialStateProperty.all(Colors.blueAccent)),
                    onPressed: () => Navigator.of(context).push(
                        MaterialPageRoute(
                            builder: (BuildContext context) => WalletPage())),
                    child: Text("Update", style: TextStyle(fontSize: 18)))),
            Container(
                width: 140,
                height: 34,
                child: ElevatedButton(
                    style: ButtonStyle(
                        backgroundColor: MaterialStateProperty.all(Colors.red)),
                    onPressed: () => {
                          Provider.of<WalletProvider>(context, listen: false)
                              .present = false
                        },
                    child: Text("Remove", style: TextStyle(fontSize: 18)))),
            SizedBox(
              width: 10,
            ),
            SizedBox(
              width: 10,
            ),
          ],
        ),
      ],
    );
  }
}
