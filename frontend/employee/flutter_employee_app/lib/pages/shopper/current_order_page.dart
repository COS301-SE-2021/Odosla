import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/pages/shopper/barcode_scanner_screen.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_main_page.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

class CurrentOrderScreen extends StatefulWidget {
  final Store store;

  const CurrentOrderScreen(BuildContext context,
      {Key? key, required this.store})
      : super(key: key);

  @override
  _CurrentOrderScreenState createState() => _CurrentOrderScreenState();
}

class _CurrentOrderScreenState extends State<CurrentOrderScreen> {
  var currentAmountPacked = <int>[];

  UserService _userService = GetIt.I.get();
  var completePackagingForItem = <bool>[];

  int counter = 0;
  int c = 0;

  bool allItemsPacked = false;

  Order order = Order("", "", "", "", 0.0, List.empty(), "");

  initState() {
    order = Provider.of<OrderProvider>(context, listen: false).order;
    for (int i = 0; i < order.items.length; i++) {
      currentAmountPacked.add(0);
    }
    c = 0;
  }

  Widget _completePackingBTN() {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 25.0),
      width: double.infinity,
      child: RaisedButton(
        elevation: 5.0,
        onPressed: () {
          bool completedOrder = true;
          for (int i = 0; i < completePackagingForItem.length; i++) {
            if (completePackagingForItem[0] == false) {
              completedOrder = false;
            }
          }
          if (completedOrder) {
            Order order =
                Provider.of<OrderProvider>(context, listen: false).order;
            _userService
                .completePackagingOrder(order.orderID, context)
                .then((value) => {
                      if (value == false)
                        {
                          ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text("Couldn't send request")))
                        }
                      else
                        {
                          Navigator.of(context).push(MaterialPageRoute(
                              builder: (BuildContext context) =>
                                  ShopperHomeScreen(1)))
                        }
                    });
          } else {
            ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text("Please finish packing order first")));
          }
        },
        padding: EdgeInsets.all(15.0),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(30.0),
        ),
        color: Colors.white,
        child: Text(
          'COMPLETE PACKAGING',
          style: TextStyle(
            color: Colors.deepOrangeAccent,
            letterSpacing: 1.5,
            fontSize: 18.0,
            fontWeight: FontWeight.bold,
            fontFamily: 'OpenSans',
          ),
        ),
      ),
    );
  }

  Widget customMenuItem(var img, var text, var price, var brand, var quantity,
      var barcode, int currentAmount, int count) {
    setState(() {
      c = c + 1;
      if (c == order.items.length) {
        c = 0;
      }
    });

    return SizedBox(
      height: MediaQuery.of(context).size.height * 0.14,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          GestureDetector(
            onTap: () async {
              if (currentAmountPacked[count] == quantity) {
                ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                    content: Text("Already finished packing for item")));
              } else {
                await Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => BarcodeScanPage(
                            context,
                            barcodeExpected: barcode,
                            productImageURL: img,
                            productName: text,
                            brand: brand,
                            store: widget.store,
                          )),
                ).then((value) => {
                      if (value == true)
                        {
                          setState(() {
                            currentAmountPacked[count] =
                                currentAmountPacked[count] + 1;
                            if (currentAmountPacked[count] == quantity) {
                              completePackagingForItem[count] = true;
                            }
                          }),
                        }
                    });
              }
            },
            child: Container(
              child: Row(
                children: <Widget>[
                  Container(
                    height: 75.0,
                    width: 75.0,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(12.0),
                    ),
                    child: Image(
                        fit: BoxFit.fill, image: AssetImage("assets/" + img)),
                  ),
                  SizedBox(
                    width: 20.0,
                  ),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Padding(
                        padding: const EdgeInsets.all(3.0),
                        child: Container(
                          padding: EdgeInsets.only(right: 15),
                          width: MediaQuery.of(context).size.width * 0.7,
                          alignment: Alignment.centerLeft,
                          child: Text(
                            text,
                            overflow: TextOverflow.ellipsis,
                            maxLines: 3,
                            softWrap: false,
                            style: TextStyle(
                              fontWeight: FontWeight.w900,
                              fontSize: 15.0,
                              color: Colors.deepOrangeAccent,
                              letterSpacing: 0.75,
                            ),
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left: 5),
                        child: Row(
                          children: <Widget>[
                            Text(
                              brand.toString(),
                              style: TextStyle(
                                //color: Colors.orangeAccent,
                                fontWeight: FontWeight.w500,
                                fontSize: 15.0,
                              ),
                            ),
                            SizedBox(
                              width: 12.0,
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top: 3, left: 5),
                        child: Row(
                          children: <Widget>[
                            Text(
                              "Price",
                              style: TextStyle(
                                // color: Colors.orangeAccent,
                                fontWeight: FontWeight.w500,
                                fontSize: 15.0,
                              ),
                            ),
                            SizedBox(
                              width: 12.0,
                            ),
                            Text(
                              "R " + price.toString(),
                              style: TextStyle(
                                // color: Colors.grey,
                                fontWeight: FontWeight.w500,
                                fontSize: 12.0,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top: 3, left: 5),
                        child: Row(
                          children: <Widget>[
                            Text(
                              "Quantity",
                              style: TextStyle(
                                //  color: Colors.orangeAccent,
                                fontWeight: FontWeight.w500,
                                fontSize: 16.0,
                              ),
                            ),
                            SizedBox(
                              width: 12.0,
                            ),
                            Text(
                              currentAmountPacked[count].toString() +
                                  "/$quantity",
                              style: TextStyle(
                                // color: Colors.grey,
                                fontWeight: FontWeight.w500,
                                fontSize: 12.0,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
    @override
    Widget build(BuildContext context) {
      // TODO: implement build
      throw UnimplementedError();
    }
  }

  @override
  Widget build(BuildContext context) {
    order = Provider.of<OrderProvider>(context).order;
    if (currentAmountPacked.length == 0) {
      for (int i = 0; i < order.items.length; i++) {
        currentAmountPacked.add(0);
      }
    }
    if (completePackagingForItem.length == 0) {
      for (int i = 0; i < order.items.length; i++) {
        completePackagingForItem.add(false);
      }
    }
    return Scaffold(
      body: Container(
        padding: EdgeInsets.all(8),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            Column(
              children: [
                Stack(
                  children: [
                    ConstrainedBox(
                        constraints: new BoxConstraints(
                            minHeight: MediaQuery.of(context).size.height * 0.2,
                            maxHeight: MediaQuery.of(context).size.height * 0.3,
                            maxWidth: double.infinity,
                            minWidth: double.infinity),
                        child: Image(
                          fit: BoxFit.fitWidth,
                          image: AssetImage(
                            "assets/" + widget.store.imageUrl,
                          ),
                        )),
                    Column(
                      children: [
                        SizedBox(
                            height: MediaQuery.of(context).size.height * 0.03),
                        Container(
                            height: 30,
                            child: GestureDetector(
                              onTap: () {
                                Navigator.of(context).push(MaterialPageRoute(
                                    builder: (BuildContext context) =>
                                        ShopperHomeScreen(1)));
                              },
                              child: Icon(
                                Icons.chevron_left,
                                color: Colors.deepOrangeAccent,
                                size: 30,
                              ),
                            )),
                      ],
                    ),
                  ],
                ),
                Container(
                  color: Colors.deepOrangeAccent,
                  width: double.infinity,
                  height: MediaQuery.of(context).size.height * 0.045,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        "Order Details ",
                        style: TextStyle(
                            fontWeight: FontWeight.w700,
                            fontSize: 27,
                            color: Colors.white),
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ),
                ),
              ],
            ),
            Container(
              height: MediaQuery.of(context).size.height * 0.5,
              child: ListView(
                children: [
                  Column(
                    children: [
                      Text(
                        "Created Date: " + order.createdDate,
                        style: TextStyle(fontSize: 12),
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            "Total:",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                                fontWeight: FontWeight.w600, fontSize: 20),
                          ),
                          SizedBox(
                            width: MediaQuery.of(context).size.width * 0.03,
                          ),
                          Text(
                            "R" + order.totalCost.toString(),
                            textAlign: TextAlign.center,
                            style: TextStyle(
                                fontWeight: FontWeight.w600, fontSize: 20),
                          ),
                        ],
                      ),
                      Text(
                        "Discount: R" + order.discount.toString(),
                        style: TextStyle(fontWeight: FontWeight.w600),
                      ),
                    ],
                  ),
                  SizedBox(
                    height: MediaQuery.of(context).size.height * 0.03,
                  ),
                  for (var i in order.items)
                    customMenuItem(
                      i.imgUrl,
                      i.name,
                      i.price,
                      i.brand,
                      i.quantity,
                      i.barcode,
                      currentAmountPacked[c],
                      c,
                    )
                ],
              ),
            ),
            _completePackingBTN(),
          ],
        ),
      ),
    );
  }
}
