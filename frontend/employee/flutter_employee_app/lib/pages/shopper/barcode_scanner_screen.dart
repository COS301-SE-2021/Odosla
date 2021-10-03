import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

import 'out_of_stock_page.dart';

class BarcodeScanPage extends StatelessWidget {
  String barcode = 'Unknown barcode';
  bool _correctBarcode = false;
  final String barcodeExpected;
  final String productImageURL;
  final String productName;
  final String brand;
  final Store store;
  final ShoppingService _shoppingService = GetIt.I.get();
  final UserService _userService = GetIt.I.get();

  BarcodeScanPage(BuildContext context,
      {Key? key,
      required this.barcodeExpected,
      required this.productImageURL,
      required this.productName,
      required this.brand,
      required this.store})
      : super(key: key);

  Widget _popUpItemNotAvailable(BuildContext context) {
    return new AlertDialog(
      title: const Text('OUT OF STOCK',textAlign: TextAlign.center,),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Would you like to offer an alternative? ",
              textAlign: TextAlign.center),
        ],
      ),
      actions: <Widget>[
        new FlatButton(

          onPressed: () async {
            Navigator.pop(context, false);
            showDialog(
              context: context,
              builder: (BuildContext context) => _offerAlternative(context),
            );

          },
          child: const Text('Alternative'),
        ),
        new FlatButton(
          onPressed: () async {
            await _userService.itemNotAvailable(Provider.of<OrderProvider>(context,
                listen: false)
                .order.orderID, barcodeExpected, "", context).then((value) =>
                {
                  if(value==true){
                    ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text("Item's unavailability reported to customer")))
                  } else{
                    ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text("Could not update unavailability of item")))
                  }
                }
            );
            Navigator.pop(context, false);
          },
          child: Icon(
            Icons.cancel_rounded,
            color: Colors.red,
          ),
        ),
      ],
    );
  }

  Widget _offerAlternative(BuildContext context) {
    return new AlertDialog(
      title: const Text('Alternative',textAlign: TextAlign.center,),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
              "Would you prefer to select an item from the page or scan its barcode? ",
              textAlign: TextAlign.center),
        ],
      ),
      actions: <Widget>[
        new FlatButton(
          onPressed: () async {
            Navigator.of(context).pushReplacement(MaterialPageRoute(
                builder: (BuildContext context) =>
                    ItemsPage(store.id, store.name, {
                      "lat": double.parse(store.storeLocationLatitude),
                      "long": double.parse(store.storeLocationLongitude)
                    },true,barcodeExpected)));
          },
          child: const Text('Items page'),
        ),
        new FlatButton(
          onPressed: () async {
            //send request still
            Navigator.pop(context, false);
          },
          child: const Text('Barcode scanner'),
        ),
      ],
    );
  }

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
                  height: MediaQuery.of(context).size.height * 0.22,
                  child: Image(
                    fit: BoxFit.fill,
                    image: AssetImage("assets/" + productImageURL),
                  ),
                ),
              ),
              Container(
                height: MediaQuery.of(context).size.height * 0.07,
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
              SizedBox(
                height: 10,
              ),
              Container(
                height: MediaQuery.of(context).size.height * 0.09,
                color: Colors.deepOrangeAccent,
                alignment: Alignment.center,
                child: Text(
                  _correctBarcode == false
                      ? "Barcode expected: " + barcodeExpected
                      : "Correct barcode scanned",
                  style: TextStyle(
                      fontSize: 28,
                      fontWeight: FontWeight.bold,
                      color: Colors.white),
                  textAlign: TextAlign.center,
                ),
              ),
              SizedBox(
                height: 40,
              ),
              Center(
                child: ButtonWidget(
                  text: 'Start Barcode scan',
                  onClicked: () async {
                    await scanBarcode(context);
                  },
                ),
              ),
              SizedBox(
                height: 10,
              ),
              Center(
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(5.0),
                    border: Border.all(color: Colors.deepOrangeAccent),
                  ),
                  alignment: Alignment.topCenter,
                  width: MediaQuery.of(context).size.width * 0.7,
                  child: TextField(
                    textAlign: TextAlign.center,
                    keyboardType: TextInputType.emailAddress,
                    style: TextStyle(fontFamily: 'OpenSans', fontSize: 25),
                    onChanged: (value) => {
                      barcode = value,
                      if (value == barcodeExpected)
                        {Navigator.pop(context, true)}
                    },
                    decoration: InputDecoration(
                      border: InputBorder.none,
                      contentPadding: EdgeInsets.only(top: 14.0),
                      hintText: 'Product ID',
                    ),
                  ),
                ),
              ),
              GestureDetector(
                onTap: () {
                  showDialog(
                    context: context,
                    builder: (BuildContext context) =>
                        _popUpItemNotAvailable(context),
                  );
                },
                child: Container(
                    height: 30,
                    alignment: Alignment.bottomCenter,
                    child: Text(
                      "ITEM OUT OF STOCK?",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                          fontStyle: FontStyle.italic,
                          decoration: TextDecoration.underline,
                          fontWeight: FontWeight.w600),
                    )),
              )
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
      if (barcode == barcodeExpected) {
        this._correctBarcode = true;
        Navigator.pop(context, true);
      } else {
        this._correctBarcode = false;
      }
    } on Exception {
      barcode = 'Failed to get platform version.';
    }
  }
}

class ButtonWidget extends StatelessWidget {
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
