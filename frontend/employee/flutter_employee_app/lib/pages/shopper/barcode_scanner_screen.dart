
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';

// class BarcodeScanPage extends StatefulWidget {
//
//   final String barcodeExpected;
//   const BarcodeScanPage(BuildContext context, {Key? key, required this.barcodeExpected}) : super(key: key);
//   @override
//   _BarcodeScanPageState createState() => _BarcodeScanPageState();
// }

class BarcodeScanPage extends StatelessWidget {
  String barcode = 'Unknown barcode';
  bool _correctBarcode=false;
  final String barcodeExpected;
  BarcodeScanPage(BuildContext context, {Key? key, required this.barcodeExpected}) : super(key: key);

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text("Barcode scanner"),
    ),
    body: Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Text(
            'Scan Result',
            style: TextStyle(
              fontSize: 16,
              color: Colors.white54,
              fontWeight: FontWeight.bold,
            ),
          ),
          SizedBox(height: 8),
          Text(
            _correctBarcode==false?"Barcode expected: "+barcodeExpected:"Correct barcode scanned",
            style: TextStyle(
              fontSize: 28,
              fontWeight: FontWeight.bold,
            ),
            textAlign: TextAlign.center,
          ),
          SizedBox(height: 72),
          ButtonWidget(
            text: 'Start Barcode scan',
            onClicked: () async {
              await scanBarcode(context);},
          ),
        SizedBox(height: 10.0),
        Container(
          alignment: Alignment.centerLeft,
          height: 60.0,
          width: 80,
          color: Colors.deepOrangeAccent,
          child: TextField(
            keyboardType: TextInputType.emailAddress,
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
            onChanged: (value) =>
            {
              barcode=value,
              if(value==barcodeExpected){
                Navigator.pop(context, true)
              }
            },
            decoration: InputDecoration(
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              // prefixIcon: Icon(
              //   Icons.wri,
              //   color: Colors.white,
              // ),
              hintText: 'Product ID',
            ),
          ),
        ),
        ],
      ),
    ),
  );

  Future scanBarcode(BuildContext context) async {
    try {
      final barcode = await FlutterBarcodeScanner.scanBarcode(
        '#ff6666',
        'Cancel',
        true,
        ScanMode.BARCODE,
      );
        if(barcode==barcodeExpected){
          this._correctBarcode=true;
          Navigator.pop(context, true);
        }
        else{
          this._correctBarcode=false;
        }
      }
      on Exception {
      barcode = 'Failed to get platform version.';
    }
  }
}

class ButtonWidget extends StatelessWidget{

  final String text;
  final VoidCallback onClicked;

  const ButtonWidget({
    required this.text,
    required this.onClicked,
  });

  @override
  Widget build(BuildContext context) => RaisedButton(
    child: Text(
      text,
      style: TextStyle(fontSize: 24),
    ),
    shape: StadiumBorder(),
    color: Theme.of(context).primaryColor,
    padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
    onPressed: onClicked,
  );
}