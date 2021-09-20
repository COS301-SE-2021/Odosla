
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';

class BarcodeScanPage extends StatelessWidget {
  String barcode = 'Unknown barcode';
  bool _correctBarcode=false;
  final String barcodeExpected;
  final String productImageURL;
  final String productName;
  final String brand;
  BarcodeScanPage(BuildContext context, {Key? key, required this.barcodeExpected, required this.productImageURL, required this.productName, required this.brand}) : super(key: key);

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text("Barcode scanner"),
    ),
    body: Container(
      height: MediaQuery.of(context).size.height,
      alignment: Alignment.center,
      child: ListView(
        children: <Widget>[
          Center(
            child: Container(
              height:MediaQuery.of(context).size.height*0.22,
              child:Image(
                fit: BoxFit.fill,
                image: AssetImage("assets/"+productImageURL),
              ),
            ),
          ),
          Container(
            height: MediaQuery.of(context).size.height*0.07,
            child: Column(
              children: [
                Text(
                  productName,
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Text(
                  brand,
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
          ),
          SizedBox(height: 10,),
          Container(
            height: MediaQuery.of(context).size.height*0.09,
            color: Colors.deepOrangeAccent,
            alignment: Alignment.center,
            child: Text(
              _correctBarcode==false?"Barcode expected: "+barcodeExpected:"Correct barcode scanned",
              style: TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                  color: Colors.white
              ),
              textAlign: TextAlign.center,
            ),
          ),
          SizedBox(height: 40,),
          Center(
            child: ButtonWidget(
              text: 'Start Barcode scan',
              onClicked: () async {
                await scanBarcode(context);},
            ),
          ),
          SizedBox(height: 10,),
          Center(
            child: Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(5.0),
                border:Border.all(
                    color: Colors.deepOrangeAccent
                ),
              ),
              alignment: Alignment.topCenter,
              width: MediaQuery.of(context).size.width*0.7,
              child: TextField(
                textAlign: TextAlign.center,
                keyboardType: TextInputType.emailAddress,
                style: TextStyle(
                    fontFamily: 'OpenSans',
                    fontSize: 25
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
                  hintText: 'Product ID',
                ),
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
    padding: EdgeInsets.symmetric(horizontal: 16, vertical: 3),
    onPressed: onClicked,
  );
}