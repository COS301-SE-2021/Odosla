import 'package:flutter/material.dart';
import 'package:odosla/data/products.dart';
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/provider/status_provider.dart';
import 'package:provider/provider.dart';
import 'package:rflutter_alert/rflutter_alert.dart';

class ItemDetailPage extends StatefulWidget {
  final CartItem item;
  final String storeID;
  const ItemDetailPage(this.item, this.storeID
      //Key key,
      ); // : super(key: key);

  @override
  _ItemDetailPage createState() => _ItemDetailPage(storeID);
}

class _ItemDetailPage extends State<ItemDetailPage> {
  int _count = 1;

  final String storeID;

  _ItemDetailPage(this.storeID);

  void add() {
    if (_count < 9) {
      setState(() {
        _count++;
      });
    }
  }

  void sub() {
    if (_count > 1) {
      setState(() {
        _count--;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
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
                  onPressed: () => Navigator.pop(context),
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
        Expanded(
          child: Container(
            margin: EdgeInsets.symmetric(vertical: 30),
            child: Image.asset(
              './' + widget.item.imgUrl,
              fit: BoxFit.cover,
            ),
          ),
        ),
        createDetails(),
        SizedBox(height: 20),
        Container(
            margin: EdgeInsets.symmetric(horizontal: 10),
            child: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
              Text(
                'R' + widget.item.price.toStringAsFixed(2),
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 28),
              ),
              SizedBox(
                width: 35,
              ),
              createQuantity(),
            ])),
        SizedBox(
          height: 30,
        ),
        Container(
            height: 50,
            width: 250,
            child: ElevatedButton(
                onPressed: () => {
                      if (storeID ==
                              Provider.of<CartProvider>(context, listen: false)
                                  .store ||
                          Provider.of<CartProvider>(context, listen: false)
                                  .items
                                  .length ==
                              0 ||
                          Provider.of<CartProvider>(context, listen: false)
                                  .store ==
                              "")
                        {
                          debugPrint("!!"),
                          Provider.of<CartProvider>(context, listen: false)
                              .addItem(widget.item, _count),
                          Provider.of<CartProvider>(context, listen: false)
                              .store = storeID,
                          Navigator.pop(context),
                        }
                      else
                        {
                          Alert(
                            context: context,
                            title: "Error:",
                            desc:
                                "You can only order items from one store at a time, please review your cart.",
                            buttons: [
                              DialogButton(
                                color: Colors.deepOrange,
                                child: Text(
                                  "OK",
                                  style: TextStyle(
                                      color: Colors.white, fontSize: 20),
                                ),
                                onPressed: () => Navigator.pop(context),
                                width: 120,
                              )
                            ],
                          ).show()
                        }
                    },
                child: Text(
                  "Add to Cart",
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

  Widget createQuantity() {
    return Container(
        decoration: BoxDecoration(
            borderRadius: BorderRadius.all(Radius.circular(4)),
            border: Border.all(color: Colors.black54)),
        child: Row(
          children: [
            MaterialButton(
                onPressed: () => sub(),
                minWidth: 50,
                child: Text('-',
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 26))),
            Text(_count.toString(),
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 28)),
            MaterialButton(
                onPressed: () => add(),
                minWidth: 50,
                child: Text('+',
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 26))),
          ],
        ));
  }
}
