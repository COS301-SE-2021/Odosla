import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/models/cart_item.dart';
import 'package:flutter_employee_app/pages/shopper/current_order_page.dart';
import 'package:flutter_employee_app/pages/shopper/list_of_stores_screen.dart';
import 'package:flutter_employee_app/pages/shopper/out_of_stock_page.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

class ItemDetailPage extends StatefulWidget {

   CartItem item;
  final String storeID;
  final Map<String, double> location;
  final bool _isAlternative;
  final String currentBarcode;

   ItemDetailPage(this.item, this.storeID, this.location,
      this._isAlternative, this.currentBarcode); // : super(key: key);

  @override
  _ItemDetailPage createState() => _ItemDetailPage(storeID, location, item);
}

class _ItemDetailPage extends State<ItemDetailPage> {
  int _count = 1;

  final ShoppingService _shoppingService = GetIt.I.get();
  final UserService _userService = GetIt.I.get();
  final String storeID;
  final Map<String, double> location;
  CartItem item;

  _ItemDetailPage(this.storeID, this.location, this.item);

  @override
  void initState() {
    item=widget.item;
  }

  @override
  Widget build(BuildContext context) {
    Store store=Provider.of<ShopProvider>(context, listen: false).store;
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Stack(
          children: [
            Padding(
              padding: const EdgeInsets.all(12),
              child: buildProduct(context),
            ),
            Positioned(
                top: 5,
                left: 5,
                child: IconButton(
                  icon: Icon(Icons.arrow_back_ios, color: Colors.black),
                  onPressed: () =>  Navigator.of(context).push(MaterialPageRoute(
                      builder: (BuildContext context) =>
                          ItemsPage(widget.storeID,store.name,location,widget._isAlternative,widget.currentBarcode)))
                ))
          ],
        ),
      ),
    );
  }

  Widget buildProduct(BuildContext context) {
    return Column(
      children: [
        SizedBox(
          height: 20,
        ),
        Text(
          widget.item.title,
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 32),
        ),
        SizedBox(
          height: 4,
        ),
        Text(
          widget.item.size, //item.size,
          style: TextStyle(
              color: Colors.black54, fontStyle: FontStyle.italic, fontSize: 18),
        ),
        Text(
          widget.item.soldOut ? "SOLD OUT" : "IN STOCK", //item.size,
          style: TextStyle(
              color: Colors.black54, fontStyle: FontStyle.italic, fontSize: 18),
        ),
        Expanded(
          child: Container(
            margin: EdgeInsets.symmetric(vertical: 30),
            child: Image.asset(
              'assets/' + widget.item.imgUrl,
              fit: BoxFit.cover,
            ),
          ),
        ),
        Container(
            margin: EdgeInsets.symmetric(horizontal: 10),
            child: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
              Text(
                'Barcode: ' + widget.item.barcode,
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 25),
              ),
              SizedBox(
                width: 35,
              ),
            ])),
        createDetails(),
        SizedBox(height: 20),
        Container(
            margin: EdgeInsets.symmetric(horizontal: 10),
            child: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
              Text(
                'R' + widget.item.price.toStringAsFixed(2),
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 25),
              ),
              SizedBox(
                width: 35,
              ),
            ])),
        Container(
          child: Text("Update details of item to:"),
        ),
        SizedBox(
          height: 20,
        ),
        Container(
            height: 50,
            width: 250,
            child: ElevatedButton(
                onPressed: () => {
                      if (widget._isAlternative)
                        {
                          _userService.itemNotAvailable(Provider.of<OrderProvider>(context, listen: false).order.orderID, widget.currentBarcode, widget.item.barcode, context).then((value) =>
                          {
                            if(value==true){
                              ScaffoldMessenger.of(context).showSnackBar(
                                  SnackBar(content: Text("Customer notified"))),

                                Navigator.of(context).push(MaterialPageRoute(
                                builder: (BuildContext context) =>
                                CurrentOrderScreen(context, store: Provider.of<ShopProvider>(context, listen: false).store)))
                            }
                          }
                          )
                        }
                      else
                        {
                          _shoppingService.itemIsInStock(context, widget.storeID, widget.item.barcode, !widget.item.soldOut).then((value) =>
                          {
                            if(value==true){
                              setState(() {
                                 item.soldOut=!widget.item.soldOut;
                                }
                              )
                            }
                          }

                          )}

                    },
                child: Text(
                  widget._isAlternative
                      ? "Offer alternative"
                      : widget.item.soldOut
                          ? "In Stock"
                          : "Out of stock",
                  style: TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                      fontSize: 18),
                ),
                style: ButtonStyle(
                    backgroundColor:
                        MaterialStateProperty.all(Colors.deepOrange),
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18.0),
                            side: BorderSide(color: Colors.orange)))))),
        SizedBox(
          height: 30,
        )
      ],
    );
  }

  Widget createDetails() => Padding(
        padding: EdgeInsets.symmetric(horizontal: 10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(
              height: 12,
            ),
            Text(
              "About",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 22),
            ),
            SizedBox(
              height: 8,
            ),
            Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Brand: " + widget.item.brand,
                    style: TextStyle(fontSize: 17),
                  ),
                  Text(
                    widget.item.description, //item.description,
                    style: TextStyle(fontSize: 17),
                  ),
                ]),
            SizedBox(
              height: 30,
            )
          ],
        ),
      );
}
